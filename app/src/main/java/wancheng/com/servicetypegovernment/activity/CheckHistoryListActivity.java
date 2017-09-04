package wancheng.com.servicetypegovernment.activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckHistoryAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CheckHistoryListActivity extends BaseActivity {

    private RelativeLayout rela_history;
    List<Map<String, Object>> listcontext;
    private CheckHistoryAdspter madapter = null;
    private ListView listView=null;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history_list);
        //  rela_history=(RelativeLayout)findViewById(R.id.rela_history);

        initdata();

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
    private void initdata() {
/*        rela_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CheckHistoryListActivity.this, " 跳转检查详情页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CheckHistoryListActivity.this, CheckResultDetailActivity.class);
                CheckHistoryListActivity.this.startActivity(intent);
            }
        });*/

        TopBean topBean=new TopBean("历史检查记录","返回","下一步",true,false);
        getTopView(topBean);
        listcontext= listcontext(18);
        listView=(ListView)findViewById(R.id.check_history);
        madapter = new CheckHistoryAdspter(this, listcontext);
        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Map<String, Object> map = listcontext.get(i);

                                                //Log.e("getErrorCode", "");
                                                if (map.get("id") != null) {
                                                    String id = map.get("id").toString();
                                                    Intent intent = new Intent();
                                                    // intent.putExtra("id",((TextView)((RelativeLayout)listView.getChildAt(i)).getChildAt(0)).getText());
                                                    intent.putExtra("ids", id);

                                                    intent.setClass(CheckHistoryListActivity.this, CheckResultDetailActivity.class);
                                                    CheckHistoryListActivity.this.startActivity(intent);
                                                }


                                            }
                                        }
        );
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {

                            madapter.add(listcontext);

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
    public List<Map<String, Object>> listcontext(int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String history_time="2017-08-24";
        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("id",j);
            map.put("history_time",history_time);
            list.add(map);
        }

        return list;

    }
}
