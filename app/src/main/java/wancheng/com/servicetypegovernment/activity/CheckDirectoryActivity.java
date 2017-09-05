package wancheng.com.servicetypegovernment.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckDirecttoryAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckResultAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CheckDirectoryActivity extends BaseActivity {
    List<Map<String, Object>> listcontext;
    private CheckDirecttoryAdspter madapter = null;
    private ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_directory);

        TopBean topBean=new TopBean("食品生产日常监督检查操作手册","返回","",true,false);
        getTopView(topBean);

        listcontext= checkDirlistcontext(5);
        listView=(ListView)findViewById(R.id.check_list);
        madapter = new CheckDirecttoryAdspter(this, listcontext,0);
        listView.setAdapter(madapter);
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
    public List<Map<String, Object>> checkDirlistcontext(int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";

        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            //tv_result3=1  合理    ；2是；3否
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("directtory_tittle", "1、生产环境条件");
            List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
            Map<String, Object> infomap=new HashMap<String, Object>();
            infomap.put("directtory_info_tittle", "1.1 厂区无扬尘、无积水，厂区、车间卫生整洁。");
                      infomap.put("directtory_info_laws","《食品安全法》第三十三条、GB 14881-2013《食品生产通用卫生规范》3.2");
            infomap.put("directtory_info_way","检查厂区、车间环境，是否符合卫生规范");
            infomap.put("directtory_info_explain", "1.厂区内的道路一般应铺设混凝土、沥青、或者其他硬质材料；空地应采取必要措施，如铺设水泥、地砖或铺设草坪等方式，保持环境清洁，正常天气下不得有扬尘和积水等现象；\n" +
                    "                                          2.生产车间地面应当无积水、无蛛网积灰、无破损等；需要经常冲洗的地面，应当有一定坡度，其最低处应设在排水沟或者地漏的位置；\n" +
                    "                                          3.查看车间的墙面及地面有无污垢、霉变、积水，不得有食品原辅料、半成品、成品等散落；");
            infolist.add(infomap);

            infomap=new HashMap<String, Object>();
            infomap.put("directtory_info_tittle","1.2 厂区无扬尘、无积水，厂区、车间卫生整洁。");
            infomap.put("directtory_info_laws","《食品安全法》第三十三条、GB 14881-2013《食品生产通用卫生规范》3.2");
            infomap.put("directtory_info_way","检查厂区、车间环境，是否符合卫生规范");
            infomap.put("directtory_info_explain", "1.厂区内的道路一般应铺设混凝土、沥青、或者其他硬质材料；空地应采取必要措施，如铺设水泥、地砖或铺设草坪等方式，保持环境清洁，正常天气下不得有扬尘和积水等现象；\n" +
                    "                                          2.生产车间地面应当无积水、无蛛网积灰、无破损等；需要经常冲洗的地面，应当有一定坡度，其最低处应设在排水沟或者地漏的位置；\n" +
                    "                                          3.查看车间的墙面及地面有无污垢、霉变、积水，不得有食品原辅料、半成品、成品等散落；");
            infolist.add(infomap);



            infomap=new HashMap<String, Object>();
            infomap.put("directtory_info_tittle","1.3 厂区无扬尘、无积水，厂区、车间卫生整洁。");
            infomap.put("directtory_info_laws","《食品安全法》第三十三条、GB 14881-2013《食品生产通用卫生规范》3.2");
            infomap.put("directtory_info_way","检查厂区、车间环境，是否符合卫生规范");
            infomap.put("directtory_info_explain", "1.厂区内的道路一般应铺设混凝土、沥青、或者其他硬质材料；空地应采取必要措施，如铺设水泥、地砖或铺设草坪等方式，保持环境清洁，正常天气下不得有扬尘和积水等现象；\n" +
                    "                                          2.生产车间地面应当无积水、无蛛网积灰、无破损等；需要经常冲洗的地面，应当有一定坡度，其最低处应设在排水沟或者地漏的位置；\n" +
                    "                                          3.查看车间的墙面及地面有无污垢、霉变、积水，不得有食品原辅料、半成品、成品等散落；");
            infolist.add(infomap);

            map.put("infolist",infolist);


            list.add(map);
        }

        return list;

    }
}
