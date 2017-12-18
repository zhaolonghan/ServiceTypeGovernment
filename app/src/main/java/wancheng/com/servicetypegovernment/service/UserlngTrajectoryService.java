package wancheng.com.servicetypegovernment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class UserlngTrajectoryService extends Service {

    public static final String TAG = "TrajectoryService";

    private DatabaseHelper databaseHelper;
    private boolean flag=false;
    private List<Map<String,String>> mapList;
    public  String uid = "";// 用户ID
    private AMapLocationClient locationClient = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    flag=false;
                    updata();
                    break;
                case 2:
                    flag=false;
                    updata();
                    break;
                case 3:
                    flag=true;
                    updata();
                    break;
            }
        }
    };
    public void updata(){

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseHelper=new DatabaseHelper(this);
        uid=intent.getStringExtra("uid");
      final double  lat=intent.getDoubleExtra("lat",0);
        final   double  lng=intent.getDoubleExtra("lng",0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //获取当前的位置
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonQuery = new JSONObject();
                    try{

                        jsonQuery.put("uid",uid);
                        jsonQuery.put("lng",lng);
                        jsonQuery.put("lat", lat);
                        String data=  jsonQuery.toString();
                        data= Base64Coder.encodeString(data);
                        map.put("data", data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    NetUtil net = new NetUtil();
                   String res = net.posturl(ConstUtil.METHOD_USERGPRS, map);

                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 1000*60*2; // 这是2分钟
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("uid", uid);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb=new StringBuffer();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return  sb.toString();
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

    protected String testStringNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}

