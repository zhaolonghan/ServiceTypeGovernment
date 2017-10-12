package wancheng.com.servicetypegovernment.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CompanyCheckListActivity extends BaseActivity {


    private Button bt_check;
    private Button bt_history;
    private CheckAdspter madapter = null;
    private ListView corplistView=null;;
    private  List<Map<String, Object>> listcorp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_check_list);
        /*bt_check=(Button)findViewById(R.id.bt_check);
        bt_history=(Button)findViewById(R.id.bt_history);
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CompanyCheckListActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CompanyCheckListActivity.this, InformActivity.class);
                CompanyCheckListActivity.this.startActivity(intent);
            }
        });
        bt_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CompanyCheckListActivity.this, " 跳转历史检查列表页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CompanyCheckListActivity.this, CheckHistoryListActivity.class);
                CompanyCheckListActivity.this.startActivity(intent);
            }
        });*/

        TopBean topBean=new TopBean("执法检查","返回","",true,false);
        getTopView(topBean);

    }
    @Override
    protected void onResume() {
        super.onResume();
        corplist();

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
    public void corplist(){
        listcorp= corplistcontext(0,5);
        corplistView=(ListView)findViewById(R.id.corplist);
        madapter = new CheckAdspter(this, listcorp,0, pd,  handler);
        corplistView.setAdapter(madapter);
        corplistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                           // madapter.add(listcorp);

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
}
