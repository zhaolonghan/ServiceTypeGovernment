package wancheng.com.servicetypegovernment.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

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
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.service.UserlngTrajectoryService;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public  class IndexFragment  extends BaseFragment {
    private AMapLocationClient locationClient = null;

    private  List<Map<String, Object>>  oaNotifylist=new ArrayList<Map<String, Object>>();
    private  List<Map<String, Object>>  lawslist=new ArrayList<Map<String, Object>>();
    private  List<Map<String, Object>>  newslist=new ArrayList<Map<String, Object>>();
    private  List<Map<String, Object>>  imagelist=new ArrayList<Map<String, Object>>();
    private wancheng.com.servicetypegovernment.view.SlideShowView SlideShowView;
    private LinearLayout linFood;//食品企业
    private LinearLayout linyp;//食品企业
    private LinearLayout linbjp;//食品企业
    private LinearLayout linhzp;//食品企业
    private LinearLayout linylqx;//食品企业
    private LinearLayout lintzsb;//食品企业
    private RelativeLayout relNewsListName;//新闻动态
    private RelativeLayout relNoticeListName;//通知公告
    private RelativeLayout relLawListName;//法律法规
    private TextView tvNew;//新闻动态
    private TextView tvlNotice;//通知公告
    private TextView tvlLaw;//法律法规
    List<Map<String, Object>> listnews;
    private NewsAdspter madapter = null;
    private View vHead;
    private int listtype = 1;//1公告 2法律 3新闻
    private Context context;
    private String[] imageUrls = {"http://www.sxxynews.com/uploadfile/2015/0316/20150316095141166.jpg",
            "http://img0.imgtn.bdimg.com/it/u=645947745,2193220436&fm=214&gp=0.jpg",
            "http://jiangsu.china.com.cn/uploadfile/2016/0316/1458124624807199.png.jpg",
            "http://ww1.sinaimg.cn/orj480/999a7ed9jw1f2pc4wn1fpj20b4089aap.jpg",
            "http://www.5888.tv/Upload_Map/uploads/2014/7/2014-07-16-11-15-33.jpg"};
    private String[] ztlxcorp=new String[6];
    private String uid= UserDateBean.getUser().getId();
    /*
    * 新闻
    * */

    private ListView listView = null;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.activity_index,
                container, false);

        lazyLoad();
        run();
        return contactsLayout;
    }
public void run(){
    Intent i = new Intent(context,UserlngTrajectoryService.class);
    i.putExtra("uid",UserDateBean.getInstance().getId());
    context.startService(i);
}

    @Override
    public void onStart() {
        super.onStart();
        getData();

    }




    @Override
    protected void lazyLoad() {
        context = getActivity();
        vHead=getActivity().getLayoutInflater().inflate(R.layout.view_head, null);
        listView=(ListView)contactsLayout.findViewById(R.id.newslist);
        listView.addHeaderView(vHead);

        linFood=(LinearLayout)contactsLayout.findViewById(R.id.lin_food);
        linyp=(LinearLayout)contactsLayout.findViewById(R.id.lin_yaopin);
        linbjp=(LinearLayout)contactsLayout.findViewById(R.id.lin_baojianpin);
        linhzp=(LinearLayout)contactsLayout.findViewById(R.id.lin_huazhuangpin);
        linylqx=(LinearLayout)contactsLayout.findViewById(R.id.lin_yiliaoqixie);
        lintzsb=(LinearLayout)contactsLayout.findViewById(R.id.lin_tezhongshebei);
        linFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_food);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.putExtra("ztlx", ztlxcorp[0]);
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linyp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_yp);
                Intent intent = new Intent();
                intent.putExtra("ztlx", ztlxcorp[1]);
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linbjp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_bjp);
                Intent intent = new Intent();
                intent.putExtra("ztlx", ztlxcorp[2]);
                intent.putExtra("companyType","保健品企业");
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linhzp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_hzp);
                Intent intent = new Intent();
                intent.putExtra("ztlx", ztlxcorp[3]);
                intent.putExtra("companyType","化妆品企业");
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linylqx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_ylqx);
                Intent intent = new Intent();
                intent.putExtra("ztlx", ztlxcorp[4]);
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        lintzsb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_tzsb);
                Intent intent = new Intent();
                intent.putExtra("ztlx", ztlxcorp[5]);
                intent.putExtra("companyType","特种设备");
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        tvlNotice =(TextView)contactsLayout.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)contactsLayout.findViewById(R.id.tv_loyal);
        tvNew=(TextView)contactsLayout.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)contactsLayout.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)contactsLayout.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)contactsLayout.findViewById(R.id.listname_law);

        final ColorStateList btnblue=tvlNotice.getTextColors();
        final  ColorStateList btnblack=tvNew.getTextColors();
        final Drawable linered=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable lineblack=relNewsListName.getChildAt(1).getBackground();



//公告
        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                madapter.update(oaNotifylist);
                listnews=oaNotifylist;
                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);
            }
        });

        //法律

        relLawListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                listnews=lawslist;
                madapter.update(lawslist);
                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblue);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(linered);
                listtype=2;
            }
        });
        //新闻
        relNewsListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                listnews=newslist;
                madapter.update(newslist);
                tvNew.setTextColor(btnblue);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblack);
                //线
                relNewsListName.getChildAt(1).setBackground(linered);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(lineblack);
                madapter.update(newslist);
                listtype=3;
            }
        });
        SlideShowView= (wancheng.com.servicetypegovernment.view.SlideShowView) contactsLayout.findViewById(R.id.sv_photo);

        //监听文本
        TopBean topBean=new TopBean("首页","我的辖区","刷新",true,true);
        getTopView(topBean, contactsLayout);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Map<String, Object> map = listnews.get(i - 1);
                                                Intent intent = new Intent();
                                                intent.putExtra("title", "详情");
                                                intent.putExtra("url", map.get("url").toString());
                                                intent.setClass(context, ContextDetailActivity.class);
                                                context.startActivity(intent);
                                            }
                                        }
        );
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationClient = new AMapLocationClient(context);
                locationClient.setLocationOption(getDefaultOption());
                locationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation loc) {
                        if (null != loc) {
                            final double lat = loc.getLatitude();
                            final double lng = loc.getLongitude();
                            //跳转地图
                            Intent intent = new Intent();
                            intent.putExtra("loc_lng",lng);
                            intent.putExtra("loc_lat",lat);
                            intent.putExtra("city_name",loc.getCity());

                            intent.setClass(context, MapActivity.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, " 无法获取当前的位置", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                locationClient.startLocation();

            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
    @Override
    public void updateView(){
        if(listnews==null||listnews.size()==0){
            listnews=oaNotifylist;
        }
        madapter = new NewsAdspter(context, listnews);
        listView.setAdapter(madapter);
        SlideShowView.setView(imageUrls);
    }

    /**
     * 获取数据
     *  类型 1公告 2法律 3新闻
     *
     * */
    private void getData() {

         oaNotifylist=new ArrayList<Map<String, Object>>();
         lawslist=new ArrayList<Map<String, Object>>();
         newslist=new ArrayList<Map<String, Object>>();

        context = getActivity();
        pd = ProgressDialog.show(context, "", "请稍候...");

        new Thread() {
            public void run() {
                    String url= ConstUtil.METHOD_INDEXLIST;
                    Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("uid", uid);
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
                                   // JSONArray dataArray = jsondate.getJSONArray("data");

                                    if(data!=null){
                                        Map<String, Object> contextmap=null;
                                        data =new String(Base64Coder.decode(data));
                                        JSONObject   jsondate = new JSONObject(data);
                                        //新闻
                                        JSONArray newsdataArray = jsondate.getJSONArray("news");
                                        Map<String ,Object> onecontextmap=null;
                                        for(int i=0;i<newsdataArray.length();i++){
                                            onecontextmap= contextMap(newsdataArray.getJSONObject(i));
                                            newslist.add(onecontextmap);
                                        }
                                        //法律
                                        newsdataArray = jsondate.getJSONArray("laws");
                                        for(int i=0;i<newsdataArray.length();i++){
                                            onecontextmap= contextMap(newsdataArray.getJSONObject(i));
                                            lawslist.add(onecontextmap);
                                        }
                                        //通知
                                        newsdataArray = jsondate.getJSONArray("notify");
                                        for(int i=0;i<newsdataArray.length();i++){
                                            onecontextmap= contextMap(newsdataArray.getJSONObject(i));
                                            oaNotifylist.add(onecontextmap);
                                        }
                                        //图片

                                        newsdataArray = jsondate.getJSONArray("images");
                                        for(int i=0;i<newsdataArray.length();i++){
                                            imageUrls[i]= JSONUtils.getString(newsdataArray.getJSONObject(i), "image", "");
                                        }

                                        //企业主题类型 ztlx
                                               /* "drugs": "药品企业",
                                                "health_products": "保健品",
                                                "food": "食品类型",
                                                "cosmetics": "化妆",
                                                "medical ": "医疗",
                                                "special_equipment": "特种"*/
                                        JSONObject ztlx= jsondate.getJSONObject("ztlx");
                                        ztlxcorp[0]= JSONUtils.getString(ztlx, "food", "");
                                        ztlxcorp[1]= JSONUtils.getString(ztlx, "drugs", "");
                                        ztlxcorp[2]= JSONUtils.getString(ztlx, "health_products", "");
                                        ztlxcorp[3]= JSONUtils.getString(ztlx, "cosmetics", "");
                                        ztlxcorp[4]= JSONUtils.getString(ztlx, "medical", "");
                                        ztlxcorp[5]= JSONUtils.getString(ztlx, "special_equipment", "");

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
    String title= JSONUtils.getString(dataobject, "title", "");
    if(title!=null&&title.length()>15){
        title=title.substring(0,15)+"...";
    }
    long timelong= JSONUtils.getLong(dataobject, "ptime", 0);
    String oneurl= JSONUtils.getString(dataobject, "url", "");
    String count= JSONUtils.getString(dataobject, "browse", "0");
    String source= JSONUtils.getString(dataobject, "source", "");
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
    public AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//
        mOption.setGpsFirst(false);//
        mOption.setHttpTimeOut(30000);//
        mOption.setInterval(200000);//
        mOption.setNeedAddress(true);//
        mOption.setOnceLocation(true);//
        mOption.setOnceLocationLatest(false);//
        mOption.setLocationCacheEnable(true);
        return mOption;
    }
}
