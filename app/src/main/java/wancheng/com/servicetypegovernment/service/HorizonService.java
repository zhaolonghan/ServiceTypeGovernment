package wancheng.com.servicetypegovernment.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Service;

import com.amap.api.services.nearby.UploadInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.activity.CheckResultActivity;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class HorizonService extends Service {

    public static final String TAG = "MyServicetime";

    private DatabaseHelper databaseHelper;
    private String addTime;
    private String IMEI;
    private String uid;
    private long  msgId;
    private boolean flag=false;
    private int count;
    private List<Map<String,String>> mapList;
    private boolean OnOffflag=false;
    int imageid=0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private int runCount=0;// 全局变量，用于判断是否是第一次执行
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
        if(!OnOffflag){
            if(mapList!=null&&mapList.size()>=0&&((mapList.size()-1)==count)){
                if(flag==true){
                    sendNotification("材料上传成功！");
                    databaseHelper.deleteById(imageid);
                }else{
                    sendNotification("材料上传失败！");
                }
            }
        }else{
            sendNotification("检查数据已同步完成！");
        }

        stopSelf();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseHelper=new DatabaseHelper(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "打印时间: " + new Date().toString());
                List<Map<String,String>> datamap=databaseHelper.findAllMsgId();
                Log.e("TAG", "结果: " +(datamap!=null?datamap.size():0));
                 datamap=databaseHelper.findAllImages();
                Log.e("TAG", "结果2: " +(datamap!=null?datamap.size():0));
                if(datamap!=null){
                   for(Map<String,String> map:datamap){
                       /*Intent intent = new Intent(null, SubmitImageService.class);
                       intent.putExtra("addtime", map.get("addtime")==null?new Date().getTime()+"":map.get("addtime").toString()+"");
                       intent.putExtra("IMEI",map.get("IMEI")==null?"":map.get("IMEI").toString()+"");
                       intent.putExtra("uid", map.get("uid")==null?"0":map.get("uid").toString()+"");
                       intent.putExtra("msgId", map.get("msgId") == null ? new Date().getTime() + "" : map.get("msgId").toString() + "");
                       startService(intent);*/
                       break;
                   }
                }

               // uploadImages(null);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 5000; // 这是5s
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
    public void uploadImages(Intent intent){
        Log.e(TAG, "onStartCommand() executed");
        databaseHelper=new DatabaseHelper(this);
        addTime= intent.getStringExtra("addtime");
        IMEI=  intent.getStringExtra("IMEI");
        uid=  intent.getStringExtra("uid");
        msgId= intent.getLongExtra("msgId", 0);
        try {
            mapList=databaseHelper.findImageByMsgId(msgId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(mapList!=null&&mapList.size()>0){
            for(int i=0;i<mapList.size();i++){
                count=i;
                Map<String,String> map=mapList.get(i);
                try{
                    imageid=Integer.parseInt(map.get("id").toString());
                    shbmitData(getJson(map).get("str"),getJson(map).get("image"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            //提交完毕，走开关
            openOnOff();

        }else{
            stopSelf();
        }
    }
    private void sendNotification(String yesOrNo){
        //实例化通知管理器
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化通知
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("系统通知");//设置通知标题
        builder.setContentText(yesOrNo);//设置通知内容
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//设置通知的方式，震动、LED灯、音乐等
        builder.setAutoCancel(true);//点击通知后，状态栏自动删除通知
        builder.setSmallIcon(android.R.drawable.ic_media_play);//设置小图标
        //    builder.setContentIntent(PendingIntent.getActivity(this, 0x102, new Intent(this, RaingRecived.class), 0));//设置点击通知后将要启动的程序组件对应的PendingIntent
        Notification notification=builder.build();

        //发送通知
        notificationManager.notify(0x101, notification);

    }
    private Map<String,String> getJson(Map<String,String> map) throws Exception{
        Map<String,String> m=encodeBase64File(map.get("imagePath").toString());
        String str="{";
        str+="\"uid\":\""+ UserDateBean.getInstance().getId()+"\"";
        str+=",\"appUpTime\":\""+addTime+"\"";
        str+=",\"appItemId\":\""+map.get("checkId").toString()+"\"";
        str+=",\"appResultId\":\""+msgId+"\"";
        str+=",\"IMEI\":\""+IMEI+"\"";
        str+=",\"fileName\":\""+m.get("fileName").toString()+"\"";
        str+="}";
        m.put("image", m.get("base64").toString());
        m.put("str", str);
        return m;
    }
    public static Map<String,String> encodeBase64File(String path) throws Exception {
        Map<String,String> map=new HashMap<String,String>();
        File file = new File(path);;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        map.put("base64", new String(Base64Coder.encodeLines(buffer)));
        map.put("fileName",file.getName());
        return map;
    }
    /**
     * 获取数据
     */
    private void shbmitData(final String str,final String image) {
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                // data= Base64Coder.encodeString(data);
                map.put("data",Base64Coder.encodeString(str));
                map.put("image", image);
                NetUtil net = new NetUtil();
                String res = net.sendPost(ConstUtil.METHOD_UPLOADIMAGE, map);
                if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                    handler.sendEmptyMessage(1);
                } else {
                    Message msg = new Message();
                    msg.what = 2;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = testStringNull(jsonObj.optString("msg"));
                            String code = testStringNull(jsonObj.optString("code"));
                            if ("0".equals(code)) {
//                                String  data=jsonObj.getString("data");
//                                data =new String(Base64Coder.decodeString(data));
//                                Log.e("datadatadatadata",data);
                                msg.what = 3;
                                msg.obj = msg_code;
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = msg_code;
                                else
                                    msg.obj = "请求异常，请稍后重试！";

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            msg.obj = "请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }

                }
            }
        }.start();
    }

    private void openOnOff() {
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("appResultUpTime", addTime);
                    jsonQuery.put("appResultId", msgId);
                    jsonQuery.put("uid", uid);
                    jsonQuery.put("IMEI", IMEI);
                    map.put("data", Base64Coder.encodeString(jsonQuery.toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }


                NetUtil net = new NetUtil();
                String res = net.sendPost(ConstUtil.METHOD_ONOFF, map);
                if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                    handler.sendEmptyMessage(1);
                } else {
                    Message msg = new Message();
                    msg.what = 2;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = testStringNull(jsonObj.optString("msg"));
                            String code = testStringNull(jsonObj.optString("code"));
                            if ("0".equals(code)) {
                                msg.what = 3;
                                msg.obj = "检查数据已同步完成";
                                OnOffflag=true;
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = msg_code;
                                else
                                    msg.obj = "请求异常，请稍后重试！";

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            msg.obj = "请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }

                }
            }
        }.start();
    }

    protected String testStringNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}

