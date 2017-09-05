package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CompanyDetailActivity extends BaseActivity {
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
        setContentView(R.layout.activity_company_detail);

        TopBean topBean=new TopBean("企业详情","返回","",true,false);
        getTopView(topBean);
        Intent intent=getIntent();

     /*   int index= intent.getIntExtra("index", 5);
        oldindexsintent=intent.getIntExtra("oldindexs",5);
        getJumpFoot(this, index, oldindexsintent);*/

        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);

        final ColorStateList btnblack =tvlNotice.getTextColors();
        final  ColorStateList btnblue =tvlLaw.getTextColors();
        final Drawable  lineblack=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable linered =relLawListName.getChildAt(1).getBackground();


        tvNew.setTextColor(btnblack);
        tvlNotice.setTextColor(btnblue);
        tvlLaw.setTextColor(btnblack);

        relNewsListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.getChildAt(1).setBackground(linered);
        relLawListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);



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


            }
        });
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
}
