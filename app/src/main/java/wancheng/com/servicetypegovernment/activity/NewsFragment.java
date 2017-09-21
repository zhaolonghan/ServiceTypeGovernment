package wancheng.com.servicetypegovernment.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
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
    private boolean isadd=true;
    private int pageNo=1;
    private int pageSize=12;
    private String keyword;
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
        getData();
        return contactsLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (isRef) {
            if (madapter.updataView(view1, 150)) {
                listView.deferNotifyDataSetChanged();
                Log.e("走没走？", "走了");
                isRef = false;
            }
        }else{

            Log.e("走没走？", "没走");
        }*/
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

        context = getActivity();
        TopBean topBean=new TopBean("新闻动态","","",false,false);
        getTopView(topBean,contactsLayout);
        listView=(RefreshListView)contactsLayout.findViewById(R.id.newslist);
        listnews=  new ArrayList<Map<String, Object>>();
        madapter = new NewsAdspter(context, listnews);
        listView.setAdapter(madapter);
        onOperationEvent();

    }

    @Override
    public void updateView() {
        madapter.update(listnews);
        madapter.notifyDataSetChanged();
        isadd=true;
    }
    public void onOperationEvent() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1)&&isadd) {
                            isadd=false;
                            pageNo=pageNo+1;
                            getData();

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
    private void getData() {
        //listnews=new ArrayList<Map<String, Object>>();

        context = getActivity();
        pd = ProgressDialog.show(context, "", "请稍候...");

        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_NEWSLIST;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("pageNo",pageNo);
                    jsonQuery.put("pageSize", pageSize);
                    jsonQuery.put("keyword", keyword);
                    map.put("data", Base64Coder.encodeString(jsonQuery.toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.posturl(url , map);
                if(res==null||"".equals(res)||res.contains("Fail to establish http connection!")){
                    handler.sendEmptyMessage(4);
                }else{
                    Message msg=new Message();
                    msg.what=14;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = testStringNull(jsonObj.optString("msg"));
                            String code = testStringNull(jsonObj.optString("code"));
                            if("0".equals(code)){
                                String  data=jsonObj.getString("data");
                                if(data!=null){
                                    Map<String, Object> contextmap=null;
                                    data =new String(Base64Coder.decode(data));
                                    //新闻
                                    JSONArray newsdataArray = new JSONArray(data);
                                    Map<String ,Object> onecontextmap=null;
                                    for(int i=0;i<newsdataArray.length();i++){
                                        onecontextmap= contextMap(newsdataArray.getJSONObject(i));
                                        listnews.add(onecontextmap);
                                    }
                                }
                                msg.what=14;
                            }else{
                                if(msg_code!=null&&!msg_code.isEmpty())
                                    msg.obj=msg_code;
                                else
                                    msg.obj="请求异常，请稍后重试！";

                            }
                        } catch (JSONException e) {

                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.getStackTraceString(e);
                            msg.obj="请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }
                }


            }

            ;
        }.start();

    }
    public  Map<String ,Object>  contextMap( JSONObject dataobject){
        Map<String, Object> contextmap=new HashMap<String, Object>();
        String title=JSONUtils.getString(dataobject, "title", "");
        if(title!=null&&title.length()>15){
            title=title.substring(0,15)+"...";
        }
        long timelong=JSONUtils.getLong(dataobject, "ptime", 0);
        String oneurl=JSONUtils.getString(dataobject, "url", "");
        String count=JSONUtils.getString(dataobject, "browse", "0");
        String source=JSONUtils.getString(dataobject, "source", "");
        Date timedate=  new Date(timelong);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
        String year=format.format(timedate);
        format=new SimpleDateFormat("dd");
        String day=format.format(timedate);
        contextmap=new HashMap<String, Object>();
        contextmap.put("title",title);
        contextmap.put("url",oneurl);
        contextmap.put("count",count);
        contextmap.put("source",source);
        contextmap.put("day",day);
        contextmap.put("year",year);
        return contextmap;
    }
}