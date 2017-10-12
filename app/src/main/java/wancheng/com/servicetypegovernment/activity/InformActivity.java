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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class InformActivity extends BaseActivity {

    public static InformActivity instance = null;
    private TextView ed_date;
    private TextView ed_date2;
    private int mYear, mMonth, mDay;
    private TextView tv_corpname;
    private EditText tb_address;
    private TextView ed_date3;
    private ImageView iv_addsign;
    private ImageView iv_addsign2;
    private Spinner sp_check1;
    private Spinner sp_check2;
    private final int DATE_DIALOG = 1;
    private final int DATE_DIALOG2 =2;
    private final int DATE_DIALOG3 = 3;
    private  int dateid;
    private TextView tv_gaozhi;
    private boolean isSign1=false;
    private boolean isSign2=false;
    private String signPath1;
    private String signPath2;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private ArrayList<Map<String,String>> checkers=new ArrayList<Map<String,String>>();
    private DatabaseHelper databaseHelper;
    private String checkAll;
    private ArrayList<String> list;
    private int spIndex1;
    private int spIndex2;
    private RadioGroup radioGroup;
    private RadioButton rb_yes;
    private RadioButton rb_np;
    private int isBack=0;
    private long insertid=-1;
    private String address;
    private String fuzeren;
    private String phone;
    private String tableName;
    private String type;
    private String permits;
    private String resultId;
    private String html="　　我们是监督检查人员，现出示执法证件。我们依法对你（单位）进行日常监督检查，请予配合。\n" +
            "　　依照法律规定，监督检查人员少于两人或者所出示的执法证件与其身份不符的，你（单位）有权拒绝检查；对于监督检查人员有下列情形之一的，" +
            "你（单位）有权申请回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        databaseHelper=new DatabaseHelper(this);
        final Intent intent=getIntent();
        final String corpId=intent.getStringExtra("corpId");
        final String corp_name=intent.getStringExtra("corp_name");
        final String corp_address=intent.getStringExtra("corp_address");
        final String ztlx=intent.getStringExtra("ztlx");
        resultId=intent.getStringExtra("resultId");
        getData(corpId,ztlx);
        instance=this;
        tv_corpname=(TextView)findViewById(R.id.tv_corpname);
        tb_address=(EditText)findViewById(R.id.tb_address);
        ed_date=(TextView)findViewById(R.id.ed_date);
        ed_date2=(TextView)findViewById(R.id.ed_date2);
        ed_date3=(TextView)findViewById(R.id.ed_date3);
        tv_gaozhi=(TextView)findViewById(R.id.tv_gaozhi);
        iv_addsign=(ImageView)findViewById(R.id.iv_addsign);
        iv_addsign2=(ImageView)findViewById(R.id.iv_addsign2);
        sp_check1=(Spinner)findViewById(R.id.sp_check1);
        sp_check2=(Spinner)findViewById(R.id.sp_check2);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        rb_yes=(RadioButton)findViewById(R.id.radio_yes);
        rb_np=(RadioButton)findViewById(R.id.radio_no);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        ed_date.setText(sdf.format(new Date()));
        ed_date2.setText(sdf.format(new Date()));
        ed_date3.setText(sdf.format(new Date()));
        tv_corpname.setText(corp_name);
        tb_address.setText(corp_address);
        tv_gaozhi.setText(html);
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
        ed_date3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateid = DATE_DIALOG3;
                showDialog(dateid);
            }
        });
        iv_addsign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InformActivity.this, SignActivity.class);
                InformActivity.this.startActivityForResult(intent, 0);
            }
        });
        iv_addsign2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InformActivity.this, SignActivity.class);
                InformActivity.this.startActivityForResult(intent, 1);
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        list=new ArrayList<String>();
        list.add("请选择");
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        sp_check1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spIndex1=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_check2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spIndex2=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_yes.getId() == checkedId) {
                    isBack=1;
                }
                if (rb_np.getId() == checkedId) {
                    isBack=0;
                }
            }
        });
        TopBean topBean=new TopBean("执法检查","返回","下一步",true,true);
        getTopView(topBean);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(tb_address.getText()==null||tb_address.getText().toString()==null||"".equals(tb_address.getText().toString().trim())){
                    Toast.makeText(InformActivity.this, "请输入被检查单位地址！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spIndex1==0){
                    Toast.makeText(InformActivity.this, "请选择检查人员1！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spIndex2==0){
                    Toast.makeText(InformActivity.this, "请选择检查人员2！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isSign1==false){
                    Toast.makeText(InformActivity.this, "请被检查单位签名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isSign2==false){
                    Toast.makeText(InformActivity.this, "请检查单位签名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,Object> map=new HashMap<String,Object>();
                map.put("companyId",corpId);
                map.put("checker1",checkers.get(spIndex1-1).get("userId"));
                map.put("checker2",checkers.get(spIndex2 - 1).get("userId"));
                map.put("checkdate",ed_date.getText().toString());
                map.put("ifBack",isBack);
                map.put("companySign",signPath1);
                map.put("companySignDate",ed_date2.getText().toString());
                map.put("checkSign",signPath2);
                map.put("checkSignDate",ed_date.getText().toString());
                map.put("content",html);
                boolean ok=databaseHelper.findMsg(insertid);
                if(ok){
                    databaseHelper.updataMsg(map,insertid);
                }else {
                    insertid=databaseHelper.insertMsg(map);
                }
                if(isBack==1){
                    finish();
                }
                    Intent intent = new Intent();
                    intent.putExtra("corpId", corpId);
                    intent.putExtra("ztlx", ztlx);
                    intent.putExtra("uid",UserDateBean.getInstance().getId());
                    intent.putExtra("address", tb_address.getText().toString());
                    intent.putExtra("insertid", insertid);
                    intent.putExtra("checkAll", checkAll);
                    intent.putExtra("fuzeren", fuzeren);
                    intent.putExtra("phone", phone);
                    intent.putExtra("permits", permits);
                    intent.putExtra("tableName", tableName);
                    intent.putExtra("resultId", resultId);
                    intent.putExtra("checkDate", ed_date.getText().toString());
                    intent.putExtra("type", type);
                    intent.putExtra("zfry1", map.get("checker1").toString());
                    intent.putExtra("zfry2", map.get("checker2").toString());
                    intent.putExtra("corpname", tv_corpname.getText().toString());
                    intent.setClass(InformActivity.this, CheckDetailActivity.class);
                    InformActivity.this.startActivity(intent);
                }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog("提示","您还没有编辑完，是否确定退出？");
            }
        });
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
                        if(databaseHelper.delMsg(insertid)){
                            finish();
                        }
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
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
    protected Dialog onCreateDialog(int id) {
        Toast.makeText(InformActivity.this, id+"", Toast.LENGTH_SHORT).show();
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
            case 3:
                ed_date3.setText(new StringBuffer().append(mYear).append(getString(R.string.year)).append(mMonth + 1).append(getString(R.string.month)).append(mDay).append(getString(R.string.day)));
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
    public void updateView() {
        String s=html;
        tv_gaozhi.setText(Html.fromHtml(s));
        tb_address.setText(address);
        if(checkers!=null&&checkers.size()>0){
            for(int i=0;i<checkers.size();i++){
                list.add(checkers.get(i).get("userName"));
            }
            sp_check1.setAdapter(adapter1);
            sp_check2.setAdapter(adapter2);
        }

    }

    private void getData(final String corpId,final String ztlx2) {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{
                   // jsonQuery.put("uid",UserDateBean.getInstance().getId());
                    jsonQuery.put("uid", UserDateBean.getInstance().getId());
                    jsonQuery.put("corpId", corpId);
                    jsonQuery.put("ztlx2", ztlx2);
                    String data=  jsonQuery.toString();
                    data= Base64Coder.encodeString(data);
                    map.put("data", data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_GETGZY, map);
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
                                String  data=jsonObj.getString("data");
                                data =new String(Base64Coder.decodeString(data));
                                JSONObject  dataArray = new JSONObject(data);
                                JSONArray checkerList=new JSONArray(JSONUtils.getString(dataArray, "jcry", ""));
                                if(checkerList!=null&&checkerList.length()>0){
                                    for(int i=0;i<checkerList.length();i++){
                                        Map<String,String> checkMap=new HashMap<String,String>();
                                        JSONObject  checkObj = checkerList.getJSONObject(i);
                                        checkMap.put("userId", JSONUtils.getString(checkObj, "userId", ""));
                                        checkMap.put("userName", JSONUtils.getString(checkObj, "userName", ""));
                                        checkers.add(checkMap);
                                    }

                                }
                                html= JSONUtils.getString(dataArray, "gzsx", "");
                                address= JSONUtils.getString(dataArray, "address", "");
                                checkAll= JSONUtils.getString(dataArray, "content", "");
                                fuzeren= JSONUtils.getString(dataArray, "fuzeren", "");
                                phone= JSONUtils.getString(dataArray, "fuzerenTel", "");
                                permits= JSONUtils.getString(dataArray, "permits", "");
                                tableName= JSONUtils.getString(dataArray, "tableName", "");
                                type= JSONUtils.getString(dataArray, "type", "");
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
    @Override
    public void onBackPressed() {
        showNormalDialog("提示","您还没有编辑完，是否确定退出？");
    }


}
