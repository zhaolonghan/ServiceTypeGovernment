package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.PopWindow;
import wancheng.com.servicetypegovernment.view.SlideShowView;

/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {


    private Button btDetail;
    private Button btStartCheck;
    private LinearLayout view_1layout;//企业
    private LinearLayout view_2layout;//检查
    private LinearLayout view_3layout;//问题
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);
        btDetail=(Button)this.findViewById(R.id.bt_detail);
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
        int index= intent.getIntExtra("index", 5);
        oldindexsintent=intent.getIntExtra("oldindexs",5);
        getJumpFoot(this, index, oldindexsintent);
        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","检查指南",true,true);
        getTopView(topBean);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopWindow popWindow = new PopWindow(CheckOrderActivity.this);
                popWindow.showPopupWindow(findViewById(R.id.tv_right));
            }
        });

        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);

        final   ColorStateList btnblue=tvlNotice.getTextColors();
        final  ColorStateList btnblack=tvNew.getTextColors();
        final  Drawable  linered=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable lineblack=relNewsListName.getChildAt(1).getBackground();


        view_1layout= (LinearLayout)findViewById(R.id.view_1);
        view_2layout= (LinearLayout)findViewById(R.id.view_2);
        view_3layout= (LinearLayout)findViewById(R.id.view_3);

        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);
                view_1layout.setVisibility(View.VISIBLE);
                view_2layout.setVisibility(View.GONE);
                view_3layout.setVisibility(View.GONE);


            }
        });


        relLawListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblue);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(linered);
                view_2layout.setVisibility(View.VISIBLE);
                view_1layout.setVisibility(View.GONE);
                view_3layout.setVisibility(View.GONE);

            }
        });
        //新闻
        relNewsListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // footView(2);
                tvNew.setTextColor(btnblue);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(linered);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(lineblack);
                view_3layout.setVisibility(View.VISIBLE);
                view_2layout.setVisibility(View.GONE);
                view_1layout.setVisibility(View.GONE);

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
        this.finish();
    }
}
