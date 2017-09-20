package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class CheckResultActivity extends BaseActivity {

    private DatabaseHelper databaseHelper;
    private String corpId;
    private String ztlx;
    private String checkAll;
    private String uid;
    private String address;
    private AMapLocationClient locationClient = null;
    private boolean isSign1=false;
    private boolean isSign2=false;
    private String signPath1;
    private String signPath2;
    private String corpname;
    private ImageView iv_addsign;
    private ImageView iv_addsign2;
    private TextView ed_date;
    private TextView ed_date2;
    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG = 1;
    private final int DATE_DIALOG2 =2;
    private final int DATE_DIALOG3 = 3;
    private EditText ed_fuzeren;
    private EditText ed_phone;
    private TextView tv_corpname;
    private TextView tv_address;
    private TextView tv_permits;
    private TextView tv_checks;
    private TextView tv_checkdetail;
    private RadioGroup rg_result;
    private RadioButton rb_yes;
    private RadioButton rb_rational;
    private RadioButton rb_no;
    private RadioGroup rg_result1;
    private RadioButton rb_ok;
    private RadioButton rb_ok1;
    private RadioButton rb_ok2;
    private EditText ed_note;
    private EditText ed_advice;
    private String fuzeren;
    private String phone;
    private String permits;
    private  int dateid;
    private String data;
    private String result;
    private String ndjccs;
    private String inspectResult;
    private String jcnr;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);
        TopBean topBean=new TopBean("检查结果","上一步","提交",true,true);
        getTopView(topBean);
        databaseHelper=new DatabaseHelper(this);
        Intent intent=getIntent();
        corpId=intent.getStringExtra("corpId");
        ztlx=intent.getStringExtra("ztlx");
        checkAll=intent.getStringExtra("checkAll");
        uid=intent.getStringExtra("uid");
        address=intent.getStringExtra("address");
        corpname=intent.getStringExtra("corpname");
        fuzeren=intent.getStringExtra("fuzeren");
        phone=intent.getStringExtra("phone");
        permits=intent.getStringExtra("permits");
        data=intent.getStringExtra("data");
        Log.e("data data ",data);
        Log.e("data size ", data.length() + "");
        getData();
        iv_addsign=(ImageView)findViewById(R.id.iv_addsign);
        iv_addsign2=(ImageView)findViewById(R.id.iv_addsign2);
        tv_corpname=(TextView)findViewById(R.id.tv_corpname);
        tv_address=(TextView)findViewById(R.id.tv_address);
        ed_date=(TextView)findViewById(R.id.ed_date);
        ed_date2=(TextView)findViewById(R.id.ed_date2);
        tv_permits=(TextView)findViewById(R.id.tv_permits);
        tv_checks=(TextView)findViewById(R.id.tv_checks);
        tv_checkdetail=(TextView)findViewById(R.id.tv_checkdetail);
        ed_fuzeren=(EditText)findViewById(R.id.ed_fuzeren);
        ed_phone=(EditText)findViewById(R.id.ed_phone);
        ed_note=(EditText)findViewById(R.id.ed_note);
        ed_advice=(EditText)findViewById(R.id.ed_advice);
        rg_result=(RadioGroup)findViewById(R.id.rg_result);
        rb_yes=(RadioButton)findViewById(R.id.rb_yes);
        rb_no=(RadioButton)findViewById(R.id.rb_no);
        rb_rational=(RadioButton)findViewById(R.id.rb_rational);
        rg_result1=(RadioGroup)findViewById(R.id.rg_result1);
        rb_ok=(RadioButton)findViewById(R.id.rb_ok);
        rb_ok1=(RadioButton)findViewById(R.id.rb_ok1);
        rb_ok2=(RadioButton)findViewById(R.id.rb_ok2);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        ed_date.setText(sdf.format(new Date()));
        ed_date2.setText(sdf.format(new Date()));
        tv_corpname.setText(corpname);
        tv_address.setText(address);
        ed_fuzeren.setText(fuzeren);
        ed_phone.setText(phone);
        tv_permits.setText(permits);
        ed_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateid = DATE_DIALOG;
                showDialog(dateid);
            }
        });
        ed_date2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateid = DATE_DIALOG2;
                showDialog(dateid);
            }
        });
        iv_addsign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CheckResultActivity.this, SignActivity.class);
                CheckResultActivity.this.startActivityForResult(intent, 0);
            }
        });
        iv_addsign2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CheckResultActivity.this, SignActivity.class);
                CheckResultActivity.this.startActivityForResult(intent, 1);
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        rg_result.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_yes.getId() == checkedId) {
                    inspectResult = "1";
                }
                if (rb_no.getId() == checkedId) {
                    inspectResult = "2";
                }
                if (rb_rational.getId() == checkedId) {
                    inspectResult = "3";
                }
            }
        });
        rg_result1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_ok.getId() == checkedId) {
                    result="1";
                }
                if (rb_ok1.getId() == checkedId) {
                    result="2";
                }
                if (rb_ok2.getId() == checkedId) {
                    result="3";
                }
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog("提示", "返回上一步将清空编辑内容，是否确定？");
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog1("提示", "是否提交检查表？");
            }
        });
        //初始化client
        locationClient = new AMapLocationClient(CheckResultActivity.this);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {

                if (null != loc) {
                    if (loc.getErrorCode() == 0) {
                       latitude=loc.getLatitude()+"";//纬度
                       longitude=loc.getLongitude()+"";//经度
                        Toast.makeText(CheckResultActivity.this, " 当前定位的地点是：" + loc.getAddress(), Toast.LENGTH_SHORT).show();
                        Log.e("ok", loc.getAddress());
                    } else {
                        Log.e("getErrorCode", loc.getErrorCode() + "");
                        Log.e("errorInfo", loc.getErrorInfo());
                    }
                } else {
                    Log.e("no", "定位失败");
                }
            }
        });
        locationClient.startLocation();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            if (requestCode==0)
            {
                signPath1=data.getStringExtra("path");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(signPath1, options);
                iv_addsign. setBackgroundResource(0);
                iv_addsign.setImageBitmap(bm);
                isSign1=true;
            }else if(requestCode==1)
            {
                signPath2=data.getStringExtra("path");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(signPath2, options);
                iv_addsign2. setBackgroundResource(0);
                iv_addsign2.setImageBitmap(bm);
                isSign2=true;
            }
        }


    }
    @Override
    public void updateView() {
        tv_checks.setText(ndjccs);
        tv_checkdetail.setText(Html.fromHtml(jcnr));
        switch (Integer.parseInt(inspectResult)){
            case 1:
                rb_yes.setChecked(true);
                break;
            case 2:
                rb_rational.setChecked(true);
                break;
            case 3:
                rb_no.setChecked(true);
                break;
        }
        switch (Integer.parseInt(result)){
            case 1:
                rb_ok.setChecked(true);
                break;
            case 2:
                rb_ok1.setChecked(true);
                break;
            case 3:
                rb_ok2.setChecked(true);
                break;
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);

    }
    public void display(int id) {

        switch (id){
            case 1:
                ed_date.setText(new StringBuffer().append(mYear).append(getString(R.string.year)).append(mMonth + 1).append(getString(R.string.month)).append(mDay).append(getString(R.string.day)));
                break;
            case 2:
                ed_date2.setText(new StringBuffer().append(mYear).append(getString(R.string.year)).append(mMonth + 1).append(getString(R.string.month)).append(mDay).append(getString(R.string.day)));
                break;
        }
    }
    //    public void display2() {
//        ed_date2.setText(new StringBuffer().append(mYear2).append(getString(R.string.year)).append(mMonth2 + 1).append(getString(R.string.month)).append(mDay2).append(getString(R.string.day)));
//    }
//    public void display3() {
//        ed_date3.setText(new StringBuffer().append(mYear3).append(getString(R.string.year)).append(mMonth3 + 1).append(getString(R.string.month)).append(mDay3).append(getString(R.string.day)));
//    }
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display(dateid);
        }
    };
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

    /**
     * 获取数据
     */
    private void getData() {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
               // data= Base64Coder.encodeString(data);
                map.put("data",data);
;                NetUtil net = new NetUtil();
                String res = net.sendPost(ConstUtil.METHOD_GETJCNR, map);
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
                                Log.e("datadatadatadata",data);
                                JSONObject obj= new JSONObject(data);
                                result=JSONUtils.getString(obj, "result", "");
                                ndjccs=JSONUtils.getString(obj, "ndjccs", "");
                                inspectResult=JSONUtils.getString(obj, "inspectResult", "");
                                jcnr=JSONUtils.getString(obj, "jcnr", "");
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
    protected void showNormalDialog(String title,String context){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(context);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    protected void showNormalDialog1(String title,String context){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(context);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckDetailActivity.instance.finish();
                        InformActivity.instance.finish();
                        finish();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(200000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        mOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
}
