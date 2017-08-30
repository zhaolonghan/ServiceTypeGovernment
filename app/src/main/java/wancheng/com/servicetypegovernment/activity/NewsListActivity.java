package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

/**
 * test
 */
public class NewsListActivity extends BaseActivity {
    List<Map<String, Object>>  listnews;
    private NewsAdspter madapter = null;
    /*
    * 新闻
    * */

    private ListView listView=null;;
    private int listtype=1;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        Intent intent=getIntent();
        int index= intent.getIntExtra("index", 0);
        getJumpFoot(this, index);
        String titlename="通知公告";
       if(index==2){
           titlename="法律法规";
       }else if(index==3){
           titlename="新闻动态";
       }
        listtype=index;
        TopBean topBean=new TopBean(titlename,"返回","",true,true);
        getTopView(topBean);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listView=(ListView)findViewById(R.id.newslist);
        listnews=  newlistcontext(index,10);
        madapter = new NewsAdspter(this, listnews);
        listView.setAdapter(madapter);
       // madapter.update(listnews);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Map<String, Object> map = listnews.get(i);

                                                //Log.e("getErrorCode", "");
                                                if (map.get("id") != null) {
                                                    String id = map.get("id").toString();
                                                    Intent intent = new Intent();
                                                    // intent.putExtra("id",((TextView)((RelativeLayout)listView.getChildAt(i)).getChildAt(0)).getText());
                                                    intent.putExtra("ids", id);
                                                    intent.putExtra("index", listtype);
                                                    intent.setClass(NewsListActivity.this, NewsInfoActivity.class);
                                                    NewsListActivity.this.startActivity(intent);
                                                }


                                            }
                                        }
        );
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
