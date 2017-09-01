package wancheng.com.servicetypegovernment.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class NewsFragment  extends BaseFragment {
    List<Map<String, Object>> listnews;
    private NewsAdspter madapter = null;
    private Context context;


    private ListView listView=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.activity_newslist,
                container, false);
        lazyLoad();
        return contactsLayout;
    }
    @Override
    protected void lazyLoad() {
        {
            context = getActivity();
           /* if(index==2){
                titlename="法律法规";
                RelativeLayout layout= (RelativeLayout) findViewById(R.id.search);
                layout.setVisibility(View.VISIBLE);
            }else if(index==3){
                titlename="新闻动态";
            }*/
            TopBean topBean=new TopBean("新闻动态","","",false,false);

            getTopView(topBean,contactsLayout);
            listView=(ListView)contactsLayout.findViewById(R.id.newslist);
            listnews=  newlistcontext(3,10);
            madapter = new NewsAdspter(context, listnews);
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
                                                        intent.setClass(context, NewsInfoActivity.class);
                                                        context.startActivity(intent);

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

                                madapter.add(listnews);

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

}