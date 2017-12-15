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
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.MainActivity;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
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
        File f=new File("/sdcard/tjcheck/some.txt");
        try{
        if(f.exists()){
            uid=readFileByLines("/sdcard/tjcheck/some.txt");
            f.deleteOnExit();
            //Rea
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        //获取用户id
        if(uid!=null&&!"".equals(uid)){
            Log.e("位置用户：",uid);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                locationClient = new AMapLocationClient(null);
                locationClient.setLocationOption(getDefaultOption());
                locationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation loc) {
                        if (null != loc) {
                            final  double lat=loc.getLatitude();
                            final  double lng=loc.getLongitude();
                            //getData(username, passWord,lat,lng);
                        }
                    }
                });
                locationClient.startLocation();
                //获取当前的位置
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonQuery = new JSONObject();
                    try{

                     /*   jsonQuery.put("username",username);
                        jsonQuery.put("password", password);
                        jsonQuery.put("lng",lng);
                        jsonQuery.put("lat", lat);
                        String data=  jsonQuery.toString();
                        data= Base64Coder.encodeString(data);
                        map.put("data", data);*/
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    NetUtil net = new NetUtil();
                    String res = net.posturl(ConstUtil.METHOD_LOGIN, map);
                    ///Log.e("res",res);
                    if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                        handler.sendEmptyMessage(4);
                    } else {
                        Message msg = new Message();
                        msg.what = 15;
                        if (!res.isEmpty()) {
                            JSONObject jsonObj;
                            try {
                                jsonObj = new JSONObject(res);
                                String msg_code = testStringNull(jsonObj.optString("msg"));
                                String code = testStringNull(jsonObj.optString("code"));
                                if ("0".equals(code)) {
                                    String  data=jsonObj.getString("data");
                                    data =new String(Base64Coder.decodeString(data));
                                    JSONObject   dataArray = new JSONObject(data);
                                    UserDateBean.getInstance().setUsername(JSONUtils.getString(dataArray, "loginName", ""));
                                    UserDateBean.getInstance().setId(JSONUtils.getString(dataArray, "uid", "0"));
                                    UserDateBean.getInstance().setName(JSONUtils.getString(dataArray, "name", ""));
                                 //   UserDateBean.getInstance().setIMEI(IMEI);
                                    UserDateBean.getInstance().setPhone(JSONUtils.getString(dataArray, "phone", ""));
                                    UserDateBean.getInstance().setPhotoimage(JSONUtils.getString(dataArray, "photo", ""));
                                    UserDateBean.getInstance().setMobile(JSONUtils.getString(dataArray, "mobile", ""));
                                    UserDateBean.getInstance().setNo(JSONUtils.getString(dataArray, "no", ""));
                                    UserDateBean.getInstance().setEmail(JSONUtils.getString(dataArray, "email", ""));
                                    UserDateBean.getInstance().setOffice(JSONUtils.getString(dataArray, "office", ""));
                                    UserDateBean.getInstance().setDistance(JSONUtils.getString(dataArray, "scope", "0") == null || JSONUtils.getString(dataArray, "scope", "0").length() < 1 ? 0 : Double.parseDouble(JSONUtils.getString(dataArray, "scope", "0")));
                                    UserDateBean.getInstance().setAddress(JSONUtils.getString(dataArray, "address", "").length() == 0 ? "  " : JSONUtils.getString(dataArray, "address", ""));
                                   // UserDateBean.getInstance().setPassword(password);

                                    File dirFirstFolder = new File("/sdcard/tjcheck");// 方法一：直接使用字符串，如果是安装在存储卡上面，则需要使用sdcard2，但是需要确认是否有存储卡
                                    // File dirFirstFolder = new File(FileUnit.Folder_NAME);//方法二：通过变量文件来获取需要创建的文件夹名字
                                    if(!dirFirstFolder.exists())
                                    { //如果该文件夹不存在，则进行创建
                                        dirFirstFolder.mkdirs();//创建文件夹
                                    }
                                    try{
                                        PrintWriter pw =new PrintWriter(new File(dirFirstFolder.getPath()+"/some.txt"));
                                        pw.print(JSONUtils.getString(dataArray, "uid", "0"));
                                        pw.flush();
                                        pw.close();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    // UserlngTrajectoryService.instace.uid=JSONUtils.getString(dataArray, "uid", "0");
                                    // uids=JSONUtils.getString(dataArray, "uid", "0");
                               /* UserlngTrajectoryService.getInstance().setId(JSONUtils.getString(dataArray, "uid", "0"));
                                Intent i = new Intent(MainActivity.this, UserlngTrajectoryService.class);
                                MainActivity.this.startService(i);*/
                                    //   UserlngTrajectoryService.i.uid=JSONUtils.getString(dataArray, "uid", "0");
                                    msg.what = 13;
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
    private Map<String,String> getJson(Map<String,String> map) throws Exception{
        Map<String,String> m=encodeBase64File(map.get("imagePath").toString());
      /*  String str="{";
        str+="\"uid\":\""+ UserDateBean.getInstance().getId()+"\"";
        str+=",\"appUpTime\":\""+addTime+"\"";
        str+=",\"appItemId\":\""+map.get("checkId").toString()+"\"";
        str+=",\"appResultId\":\""+msgId+"\"";
        str+=",\"IMEI\":\""+IMEI+"\"";
        str+=",\"fileName\":\""+m.get("fileName").toString()+"\"";
        str+="}";
        m.put("image", m.get("base64").toString());
        m.put("str", str);*/
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
        map.put("fileName", file.getName());
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

