package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.PopWindow;
/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {


    private Button btDetail;
    private Button btStartCheck;
    private Button bt_history;
    private Button bt_check;
    private LinearLayout view_1layout;//企业
    private LinearLayout view_2layout;//检查
    private LinearLayout view_3layout;//问题
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    private PopWindow popWindow;
    private boolean  isPOPOpen=false;
    /**企业列表里**/
    private CheckAdspter madapter = null;
    private ListView corplistView=null;;
    List<Map<String, Object>> listcorp;

    /**执法**/
    private CheckAdspter questionadapter = null;
    private ListView questionlistView=null;;
    List<Map<String, Object>> listquestion;

    /**问题*/
    /**执法**/
    private CheckAdspter enforcementadapter = null;
    private ListView enforcementlistView=null;;
    List<Map<String, Object>> listenforcement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);

        //企业列表
        corplist();
        //执法列表
        enforcementlist();
        //问题
        question();



      /*  bt_history=(Button)findViewById(R.id.bt_history);
        bt_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CheckOrderActivity.this, " 跳转历史检查列表页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CheckHistoryListActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        bt_check=(Button)this.findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CheckOrderActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, InformActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
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
        });*/
        Intent intent=getIntent();
        int index= intent.getIntExtra("index", 5);
        oldindexsintent=intent.getIntExtra("oldindexs",5);
        getJumpFoot(this, index, oldindexsintent);
        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","检查指南",true,true);
        getTopView(topBean);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPOPOpen=true;
                popWindow = new PopWindow(CheckOrderActivity.this);
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


        tvNew.setTextColor(btnblack);
        tvlNotice.setTextColor(btnblue);
        tvlLaw.setTextColor(btnblack);
        relNewsListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.getChildAt(1).setBackground(linered);
        relLawListName.getChildAt(1).setBackground(lineblack);
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
    /**
     *
     * num展示企业条数
     * */
    public List<Map<String, Object>> corplistcontext(int type,int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        String id;
        String corp_name;
        String corp_code;
        String corp_person;
        String corp_tel;
        String corp_address;
        list=new ArrayList<Map<String, Object>>();
        //企业
        if(type==0){
            for(int j=0;j<num;j++){
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("id",j);

                map.put("corp_name","天津市大河食品有限公司");
                map.put("corp_code","001002003");
                map.put("corp_person","漓江上");
                map.put("corp_tel","0101234567");
                map.put("corp_address","北京上地77号楼");
                list.add(map);
            }
        }

        String check_date="2018-08-23";
        String check_corpnum="100家";
        String check_status="进行中";
        String check_numed="50家";
        String check_numing="50家";
        String check_numthrought="40家";
        String check_numunthrought="10家";
        String check_radioing="50%";
        String check_radiothrought="40%";

        //执法
        if(type==1){
            for(int j=0;j<num;j++){
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("id",j);
                map.put("check_date",check_date);
                map.put("check_corpnum",check_corpnum);
                map.put("check_status",check_status);
                map.put("check_numed",check_numed);
                map.put("check_numing",check_numing);
                map.put("check_numthrought",check_numthrought);
                map.put("check_numunthrought",check_numunthrought);
                map.put("check_radioing", check_radioing);
                map.put("check_radiothrought", check_radiothrought);
                list.add(map);
            }
        }

        String question_corpname="天津市大河食品有限公司";
        String question_no="20170228000001";
        String question_date="2017-08-28";
        String question_result="基本符合";
        String question_management="书面责令整改";
        String question_status="未整改";
        String question_limit="2017-09-30";


        //问题
        if(type==2){
            for(int j=0;j<num;j++){
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("id",j);
                map.put("question_corpname",question_corpname);
                map.put("question_no",question_no);
                map.put("question_date",question_date);
                map.put("question_result",question_result);
                map.put("question_management",question_management);
                map.put("question_status",question_status);
                map.put("question_limit",question_limit);
                list.add(map);
            }
        }
        return list;

    }
    public void corplist(){
        listcorp= corplistcontext(0,5);
        corplistView=(ListView)findViewById(R.id.corplist);
        madapter = new CheckAdspter(this, listcorp,0);
        corplistView.setAdapter(madapter);
        corplistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            madapter.add(listcorp);

                        }

                        break;

                }

            }


            @Override

            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {

            }

        });
    }

    public void enforcementlist(){
        listenforcement= corplistcontext(1,5);
        enforcementlistView=(ListView)findViewById(R.id.enforcementlist);
        enforcementadapter = new CheckAdspter(this, listenforcement,1);
        enforcementlistView.setAdapter(enforcementadapter);
        enforcementlistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            enforcementadapter.add(listenforcement);

                        }

                        break;

                }

            }


            @Override

            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {

            }

        });
    }


    public void question(){
        listquestion= corplistcontext(2,5);
        questionlistView=(ListView)findViewById(R.id.questionlist);
        questionadapter = new CheckAdspter(this, listquestion,2);
        questionlistView.setAdapter(questionadapter);
        questionlistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            questionadapter.add(listquestion);

                        }

                        break;

                }

            }


            @Override

            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {

            }

        });
    }
}
