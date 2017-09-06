package wancheng.com.servicetypegovernment.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import wancheng.com.servicetypegovernment.view.RefreshListView;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class NewsFragment  extends BaseFragment implements RefreshListView.OnRefreshListener{
    List<Map<String, Object>> listnews;
    private NewsAdspter madapter = null;
    private Context context;
    private boolean isRef=false;
    private  View view1;
    private RefreshListView listView=null;
  //  private RefreshListView refreshListView;
    private final static int REFRESH_COMPLETE = 0;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    listView.setOnRefreshComplete();
                    madapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    break;

                default:
                    break;
            }
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.activity_newslist,
                container, false);
        lazyLoad();
        return contactsLayout;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isRef) {
            if (madapter.updataView(view1, 150)) {
                listView.deferNotifyDataSetChanged();
                Log.e("走没走？", "走了");
                isRef = false;
            }
        }else{

            Log.e("走没走？", "没走");
        }
    }
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                 //   mDatas.add(0, "new data");
                    mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
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
            listView=(RefreshListView)contactsLayout.findViewById(R.id.newslist);
            listnews=  newlistcontext(3,10);
            madapter = new NewsAdspter(context, listnews);
            listView.setAdapter(madapter);
            listView.setOnRefreshListener(this);
            // madapter.update(listnews);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                  @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      view1=view;
                          isRef=true;
                          Map<String, Object> map = listnews.get(i);
                          Intent intent = new Intent();
                          intent.putExtra("title","新闻详情");
                          intent.setClass(context, ContextDetailActivity.class);
                          context.startActivity(intent);

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