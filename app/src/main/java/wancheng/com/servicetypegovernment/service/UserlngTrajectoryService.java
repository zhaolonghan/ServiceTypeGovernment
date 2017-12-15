package wancheng.com.servicetypegovernment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class UserlngTrajectoryService extends Service {

    public static final String TAG = "TrajectoryService";
    private AMapLocationClient locationClient = null;

    private DatabaseHelper databaseHelper;
    public String uid= "0";
    private List<Map<String,String>> mapList;
 /*   public UserlngTrajectoryService(Intent intent){

    }*/
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseHelper=new DatabaseHelper(this);
        //获取当前的位置
        locationClient = new AMapLocationClient(this);
        uid=intent.getStringExtra("uid");
        Log.e("位置前：","11111111");

        Log.e("位置：",uid);
        locationClient.setLocationOption(getDefaultOption());
        new Thread(new Runnable() {
            @Override
            public void run() {

                locationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation loc) {
                        if (null != loc) {
                            final  double lat=loc.getLatitude();
                            final  double lng=loc.getLongitude();
                            //编写传送位置
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject jsonQuery = new JSONObject();
                            try{
                                if(uid!=null&&uid.length()>1){
                                    jsonQuery.put("uid",uid);
                                    jsonQuery.put("lng",lng);
                                    jsonQuery.put("lat", lat);
                                    String data=  jsonQuery.toString();
                                    Log.e("用户定位",data);
                                    data= Base64Coder.encodeString(data);
                                    map.put("data", data);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(uid.length()>1){
                                NetUtil net = new NetUtil();
                                net.posturl(ConstUtil.METHOD_USERGPRS, map);
                            }

                        }
                    }
                });
                locationClient.startLocation();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 10000; // 这是10s
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
    public AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//
        mOption.setGpsFirst(false);//
        mOption.setHttpTimeOut(30000);//
        mOption.setInterval(200000);//
        mOption.setNeedAddress(true);//
        mOption.setOnceLocation(true);//
        mOption.setOnceLocationLatest(false);//
        mOption.setLocationCacheEnable(true);
        return mOption;
    }

}

