package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckQuestionAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckResultAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CheckResultDetailActivity extends BaseActivity {
    private ScrollView view_1layout;
    private LinearLayout view_2layout;
    private ScrollView view_3layout;
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    List<Map<String, Object>> listnews;
    private CheckResultAdspter madapter = null;
    private ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result_detail);
//        bt_check=(Button)findViewById(R.id.bt_check);
//        bt_check.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Toast.makeText(CheckDetailActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(CheckDetailActivity.this, InformActivity.class);
//                CheckDetailActivity.this.startActivity(intent);
//            }
//        });

        TopBean topBean=new TopBean("检查详情","返回","下一步",true,false);
        getTopView(topBean);
        indata();
        listnews= checkresultlistcontext(5);
        listView=(ListView)findViewById(R.id.checkquestionlist);
        madapter = new CheckResultAdspter(this, listnews,0);
        listView.setAdapter(madapter);

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
    public void indata(){
        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);

        final ColorStateList btnblue=tvlNotice.getTextColors();
        final  ColorStateList btnblack=tvNew.getTextColors();
        final Drawable linered=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable lineblack=relNewsListName.getChildAt(1).getBackground();


        view_1layout= (ScrollView)findViewById(R.id.view_1);
        view_2layout= (LinearLayout)findViewById(R.id.view_2);
        view_3layout= (ScrollView)findViewById(R.id.view_3);
        view_1layout.setVisibility(View.VISIBLE);
        view_2layout.setVisibility(View.GONE);
        view_3layout.setVisibility(View.GONE);
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
    public List<Map<String, Object>> checkresultlistcontext(int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String title="";
        String time="";
        String content="";



        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            //tv_result3=1  合理    ；2是；3否
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("detail_title", "1、生产环境条件");
                List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
                Map<String, Object> infomap=new HashMap<String, Object>();
                infomap.put("detail_info","1.1 厂区基本无扬尘、基本无积水，基本厂区、基本车间卫生整洁。基本厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
                infomap.put("tv_result3",1);
                infomap.put("detail_remark","整体不错，继续努力");
                infomap.put("detail_image","");
                infolist.add(infomap);

            infomap=new HashMap<String, Object>();
            infomap.put("detail_info","1.2 厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
            infomap.put("tv_result3",2);
            infomap.put("detail_remark","全部合格");
            infomap.put("detail_image","");
            infolist.add(infomap);


           infomap=new HashMap<String, Object>();
            infomap.put("detail_info","1.3 厂区有扬尘、有积水，厂区、车间卫生整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。");
            infomap.put("tv_result3",3);
            infomap.put("detail_remark","很不合格");
            infomap.put("detail_image","");
            infolist.add(infomap);
            map.put("infolist",infolist);


            list.add(map);
        }

        return list;

    }
}
