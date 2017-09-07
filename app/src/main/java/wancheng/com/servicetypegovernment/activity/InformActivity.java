package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class InformActivity extends BaseActivity {

    public static InformActivity instance = null;
    private TextView ed_date;
    private TextView ed_date2;
    private TextView ed_date3;
    private ImageView iv_addsign;
    private ImageView iv_addsign2;
    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG = 1;
    private final int DATE_DIALOG2 =2;
    private final int DATE_DIALOG3 = 3;
    private  int dateid;
    private TextView tv_gaozhi;
    private String html="　　我们是监督检查人员，现出示执法证件。我们依法对你（单位）进行日常监督检查，请予配合。\n" +
            "　　依照法律规定，监督检查人员少于两人或者所出示的执法证件与其身份不符的，你（单位）有权拒绝检查；对于监督检查人员有下列情形之一的，" +
            "你（单位）有权申请回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        instance=this;
        ed_date=(TextView)findViewById(R.id.ed_date);
        ed_date2=(TextView)findViewById(R.id.ed_date2);
        ed_date3=(TextView)findViewById(R.id.ed_date3);
        tv_gaozhi=(TextView)findViewById(R.id.tv_gaozhi);
        iv_addsign=(ImageView)findViewById(R.id.iv_addsign);
        iv_addsign2=(ImageView)findViewById(R.id.iv_addsign2);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        ed_date.setText(sdf.format(new Date()));
        ed_date2.setText(sdf.format(new Date()));
        ed_date3.setText(sdf.format(new Date()));
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
                dateid=DATE_DIALOG2;
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
        iv_addsign.setOnClickListener(new View.OnClickListener() {

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

        TopBean topBean=new TopBean("执法检查","返回","下一步",true,true);
        getTopView(topBean);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(InformActivity.this, CheckDetailActivity.class);
                InformActivity.this.startActivity(intent);
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InformActivity.this, "测试对话框", Toast.LENGTH_SHORT).show();
                showNormalDialog("提示","您还没有编辑完，是否确定退出？");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==100)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(data.getStringExtra("path"), options);
            iv_addsign. setBackgroundResource(0);
            iv_addsign.setImageBitmap(bm);
        }else if(resultCode==101)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(data.getStringExtra("path"), options);
            iv_addsign2.setImageBitmap(bm);
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

}
