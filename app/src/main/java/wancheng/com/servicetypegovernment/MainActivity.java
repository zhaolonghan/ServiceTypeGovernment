package wancheng.com.servicetypegovernment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import wancheng.com.servicetypegovernment.activity.BaseActivity;
import wancheng.com.servicetypegovernment.activity.CoreActivity;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.UpdateManager;

public class MainActivity extends BaseActivity {

   private Button btnLogin;
    private String username;
    private String passWord;
    private DatabaseHelper databaseHelper;
    private String IMEI;
    private HashMap<String, Object> map = new HashMap<String, Object>();
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        requestMultiplePermissions();
        btnLogin =(Button)findViewById(R.id.btn_login);
        final EditText ed=(EditText)findViewById(R.id.editText1);
        final EditText ed2=(EditText)findViewById(R.id.editText2);
        ed.setText("ceshi");
        ed2.setText("123456");
        databaseHelper=new DatabaseHelper(this);
//        getData();

        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                username = ed.getText().toString();
                passWord = ed2.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passWord)) {
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    getData(username, passWord);
                }

                //updateView();
            }
        });
        File destDir = new File("/sdcard/Wancheng/Photos/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }
    @Override
    public void updateView() {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.setClass(MainActivity.this, CoreActivity.class);
        MainActivity.this.startActivity(intent);
    }
    public void updataData(){
        String versionName=map.get("versionName").toString();
        String versionCoder=map.get("versionCode").toString();
        String versionURLr=map.get("versionUrl").toString();
        String versionDb=databaseHelper.findVersion();
 /*       Log.e("versionCoder",versionCoder);
        Log.e("versionDb", versionDb + "");*/
        if(versionDb==null||"".equals(versionDb)){
            boolean ok=databaseHelper.insertVersion(versionName,versionCoder);
            Log.e("versionDb insert",ok+"");
            Toast.makeText(MainActivity.this, "当前已经是最新版本！", Toast.LENGTH_SHORT).show();
        }else{
            if(!versionDb.equals(versionCoder)){
                databaseHelper.insertVersion(versionName,versionCoder);
                UpdateManager manager = new UpdateManager(this,versionURLr);
			    //检查软件更新
		        manager.checkUpdate();
            }else{
                Toast.makeText(MainActivity.this, "当前已经是最新版本！", Toast.LENGTH_SHORT).show();
            }
        }
//        boolean ok=databaseHelper.updataVersion(10, "2");
//        Log.e("ok",ok+"");
//        Log.e("ok",databaseHelper.findVersion());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
          //  Log.e("version",version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            int grantResult = grantResults[1];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            IMEI = TelephonyMgr.getDeviceId();
            Log.i("权限申请", "onRequestPermissionsResult granted=" + granted);
            Log.i("IMEI", "IMEId=" + IMEI);
        }
    }
    private void requestMultiplePermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        requestPermissions(permissions, REQUEST_CODE);
    }
    /**
     * 获取数据
     */
    private void getData() {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_GETVERSION, map);
                Log.e("返回值", res);
                if (res == null || "".equals(res)|| res.contains("Fail to establish http connection!")) {

                    handler.sendEmptyMessage(4);
                } else {
                    Message msg = new Message();
                    msg.what = 18;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = testStringNull(jsonObj.optString("msg"));
                            String code = testStringNull(jsonObj.optString("code"));
                            if ("0".equals(code)) {
                                map.put("versionCode", JSONUtils.getString(jsonObj,"versionCode", ""));
                                map.put("versionName", JSONUtils.getString(jsonObj,"versionName", ""));
                                map.put("versionUrl", JSONUtils.getString(jsonObj,"versionUrl", ""));
                                msg.what=18;
                                //						msg.obj=msg_code;
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = msg_code;
                                else
                                    msg.obj = "请求异常，请稍后重试！";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.obj = "请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }

                }
            };
        }.start();
    }
    /**
     * 获取数据
     */
    private void getData(final String username,final String password) {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{

                    jsonQuery.put("username",username);
                    jsonQuery.put("password", password);
                    String data=  jsonQuery.toString();
                    data= Base64Coder.encodeString(data);
                    map.put("data", data);
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
                                UserDateBean.getInstance().setIMEI(IMEI);
                                UserDateBean.getInstance().setPhone(JSONUtils.getString(dataArray, "phone", ""));
                                UserDateBean.getInstance().setPhotoimage(JSONUtils.getString(dataArray, "photo", ""));
                                UserDateBean.getInstance().setMobile(JSONUtils.getString(dataArray, "mobile", ""));
                                UserDateBean.getInstance().setNo(JSONUtils.getString(dataArray, "no", ""));
                                UserDateBean.getInstance().setEmail(JSONUtils.getString(dataArray, "email", ""));
                                UserDateBean.getInstance().setOffice(JSONUtils.getString(dataArray, "office", ""));
                                UserDateBean.getInstance().setAddress(JSONUtils.getString(dataArray, "address", "").length() == 0 ? "  " : JSONUtils.getString(dataArray, "address", ""));
                                UserDateBean.getInstance().setPassword(password);

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

            ;
        }.start();

    }
}
