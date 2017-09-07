package wancheng.com.servicetypegovernment.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class SubmitImageService extends Service {


    public static final String TAG = "MyService";


    public SubmitImageService() {


    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate() executed");

    }

    private int runCount=0;// 全局变量，用于判断是否是第一次执行
    private Handler handler = new Handler();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand() executed");
        List<Map<String,String>> list=( List<Map<String,String>>)intent.getSerializableExtra("datalist");
              for (Map<String, String> map : list) {
                    Log.e("id1", map.get("id"));
                    Log.e("checkId1", map.get("checkId"));
                    Log.e("imagePath1", map.get("imagePath"));
               }
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(runCount == 1){// 第一次执行则关闭定时执行操作
                    // 在此处添加执行的代码
                    Log.e("start", "sendNotification start");
                    sendNotification();
                    stopSelf();
                    Log.e("stop", "sendNotification stop");
                    handler.removeCallbacks(this);
                }
                handler.postDelayed(this, 20000);
                runCount++;
            }

        };
        handler.postDelayed(runnable, 20000);// 打开定时器，执行操作

        return super.onStartCommand(intent, flags, startId);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendNotification(){
        //实例化通知管理器
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化通知
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("系统通知");//设置通知标题
        builder.setContentText("材料上传成功！");//设置通知内容
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//设置通知的方式，震动、LED灯、音乐等
        builder.setAutoCancel(true);//点击通知后，状态栏自动删除通知
        builder.setSmallIcon(android.R.drawable.ic_media_play);//设置小图标
        //    builder.setContentIntent(PendingIntent.getActivity(this, 0x102, new Intent(this, RaingRecived.class), 0));//设置点击通知后将要启动的程序组件对应的PendingIntent
        Notification notification=builder.build();

        //发送通知
        notificationManager.notify(0x101, notification);

    }
}
