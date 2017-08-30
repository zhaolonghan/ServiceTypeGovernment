package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.PopWindow;
import wancheng.com.servicetypegovernment.view.SlideShowView;

/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {


    private Button btDetail;
    private Button btCheckDetail;
    private Button btStartCheck;
    private Button bt_check;
    private SimpleDateFormat sdf;
    private Date date1;
    private Date date2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);
        date1=new Date();
        Log.e("时间开始是：",new SimpleDateFormat("HH:mm:dd").format(date1));
        btCheckDetail=(Button)this.findViewById(R.id.bt_check_detail);
        btDetail=(Button)this.findViewById(R.id.bt_detail);
        bt_check=(Button)this.findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, InformActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        btCheckDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CheckResultDetailActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        btDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CompanyDetailActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        btStartCheck=(Button)this.findViewById(R.id.bt_start_check);
        btStartCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CompanyCheckListActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        Intent intent=getIntent();
        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","检查指南",true,true);
        getTopView(topBean);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopWindow popWindow = new PopWindow(CheckOrderActivity.this);
                popWindow.showPopupWindow(findViewById(R.id.tv_right));
            }
        });
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
        date2=new Date();
        Log.e("onstop：", "onstop");
        Log.e("时间结束是：", new SimpleDateFormat("HH:mm:dd").format(date2));
        long l=date2.getTime()-date1.getTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        date2=new Date();
        Log.e("ondestroy：", "ondestroy");
        Log.e("时间结束是：", new SimpleDateFormat("HH:mm:dd").format(date2));
        long l=date2.getTime()-date1.getTime();
        Log.e("用时：",l+"毫秒");
    }
}
