package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;

import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.view.PopWindow;

/**
 * test
 */
public class NewpasswordActivity extends BaseActivity {
    private AMapLocationClient locationClient = null;
    private TextView v_oldpassword;
    private TextView v_newpassword;
    private TextView v_againpassword;
    private Button btn_updatepassword;
    private PopWindow popWindow;
    private boolean  isPOPOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        initView();
        onOperationEvent();
    }
    private void initView(){
        TopBean topBean=new TopBean("修改密码","返回","保存",true,true);
        getTopView(topBean);
        v_oldpassword=(TextView)this.findViewById(R.id.oldpassword);
        v_newpassword=(TextView)this.findViewById(R.id.newpassword);
        v_againpassword=(TextView)this.findViewById(R.id.againpassword);
        /// btn_updatepassword=(Button)this.findViewById(R.id.btn_updatepassword);
    }
    public void onOperationEvent() {
        //监控筛选按钮
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldpassword=v_oldpassword.getText().toString();
                final  String newpassword=v_newpassword.getText().toString();
                final String againpassword=v_againpassword.getText().toString();
                //判断
                String msg="";
                boolean isok=true;
                if(oldpassword.length()==0&&isok){
                    msg="旧密码不能为空!";
                    isok=false;
                }
                if(newpassword.length()==0&&isok){
                    msg="新密码不能为空!";
                    isok=false;
                }
                if(againpassword.length()==0&&isok){
                    msg="确认密码不能为空!";
                    isok=false;
                }
                if(!againpassword.equals(newpassword)&&isok){
                    msg="新密码与确认密码不一致!";
                    isok=false;
                }
                if(!againpassword.equals(UserDateBean.getUser().getPassword())&&isok){
                    msg="旧密码不正确!";
                    isok=false;
                }
                if(isok){
                    locationClient = new AMapLocationClient(NewpasswordActivity.this);
                    locationClient.setLocationOption(getDefaultOption());
                    locationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation loc) {
                            if (null != loc) {
                                final  double lat=loc.getLatitude();
                                final  double lng=loc.getLongitude();
                                updatepassword(UserDateBean.getUser().getId(), oldpassword, newpassword,lat,lng);
                            } else {
                                Toast.makeText(NewpasswordActivity.this, " 无法获取当前的位置" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    locationClient.startLocation();

                }else{
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void updateView() {
        v_oldpassword.setText("");
        v_newpassword.setText("");
        v_againpassword.setText("");
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
        if(isPOPOpen){
            popWindow.dismiss();
            isPOPOpen=false;
        }

        // this.finish();
    }


    private void updatepassword(final String uid,final String oldpassword,final String newpassword,final double lat,final  double lng) {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{

                    jsonQuery.put("uid",uid);
                    jsonQuery.put("oldpassword", oldpassword);
                    jsonQuery.put("newpassword", newpassword);
                    jsonQuery.put("lat", lat);
                    jsonQuery.put("lng", lng);
                    String data=  jsonQuery.toString();
                    data= Base64Coder.encodeString(data);
                    map.put("data", data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_UPDATEPASS, map);
                //Log.e("res", res);
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
                                UserDateBean.getInstance().setPassword(newpassword);
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
