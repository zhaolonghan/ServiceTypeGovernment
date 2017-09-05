package wancheng.com.servicetypegovernment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public  class IndexFragment  extends BaseFragment {

    private AMapLocationClient locationClient = null;
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
        return contactsLayout;
    }


    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(200000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        mOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
//    /**
//     * i=1 公告
//     *
//     * i=1 法律
//     *
//     * i=1 新闻
//     *
//     * num=条数
//     *
//     * */
//    public List<Map<String, Object>> newlistcontext(int i,int num){
//        List<Map<String, Object>>  list;
//        List<Map<String, Object>>  addalllist;
//        //标题0  时间1  内容2String id="1";
//        String title="";
//        String time="";
//        String content="";
//        switch (i){
//            case 1:
//                title="国家质量监督检验检疫总局《进出口工业品风险管理办法》";
//                time="2017-06-21";
//                content="《进出口工业品风险管理办法》已经2017年2月21日国家质量监督检验检疫总局局务会议审议通过，现予公布，自2017年4月1日起施行。";
//                break;
//            case 2:
//                title="中华人民共和国食品安全法实施条例";
//                time="2015-08-17 ";
//                content="《中华人民共和国食品安全法实施条例》已经2009年7月8日国务院第73次常务会议通过，现予公布，自公布之日起施行。";
//                break;
//            case 3:
//                title="市市场监管委党委召开领导干部警示教育大";
//                time="2015-06-27";
//                content="\\u3000\\u3000日前，市市场监管委委党委组织召开了全系统领导干部警示教育大会，传达学习全市领导干部警示教育大会精神，组织开展警示教育，引导广大党员干部知晓、敬畏和严守党纪国法，做到心有所畏、言有沙达14%。";
//                break;
//        }
//
//
//        list=new ArrayList<Map<String, Object>>();
//        for(int j=0;j<num;j++){
//            Map<String, Object> map=new HashMap<String, Object>();
//            map.put("id",j);
//            map.put("title",title);
//            map.put("time",time);
//            map.put("context",content);
//            list.add(map);
//        }
//
//        return list;
//
//    }
//    public List<Map<String, Object>> newslistcontext(int num){
//        //http://www.tjcac.gov.cn/zlaqzd/
//        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
//        Map<String, Object> map=new HashMap<String, Object>();
//        map.put("id","1");
//        map.put("title","市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议");
//        map.put("time","2017-08-23");
//        map.put("context","8月17日，市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议，总队长郝恩海，副总队长施航华、王斌、王书生，市建委质量安全处处长王俊河，执法监督处处长石林，市施工企业协会秘书长黑金山同志在主席台就坐。市、区两级监管机构负责同志，有关企业、协会及驻津办代表，共计300余人参加会议，会议由王斌副总队长主持。\n" +
//                "\n" +
//                "    王书生副总队长传达了8月12日全市安全生产电视电话会议精神；王斌副总队长传达了8月14日天津市社会维稳和信访工作会议精神。施航华副总队长通报了今年以来建设工程质量安全专项检查、建筑材料封样抽测、行政处罚、安全事故、观摩交流、扬尘治理情况。");
//
//
//        list.add(map);
//
//
//        map=new HashMap<String, Object>();
//        map.put("id","2");
//        map.put("title","总队强化全运会建筑施工应急保障组织工作");
//        map.put("time","2017-08-23");
//        map.put("context","8月17日，市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议，总队长郝恩海，副总队长施航华、王斌、王书生，市建委质量安全处处长王俊河，执法监督处处长石林，市施工企业协会秘书长黑金山同志在主席台就坐。市、区两级监管机构负责同志，有关企业、协会及驻津办代表，共计300余人参加会议，会议由王斌副总队长主持。\n" +
//                "\n" +
//                "    王书生副总队长传达了8月12日全市安全生产电视电话会议精神；王斌副总队长传达了8月14日天津市社会维稳和信访工作会议精神。施航华副总队长通报了今年以来建设工程质量安全专项检查、建筑材料封样抽测、行政处罚、安全事故、观摩交流、扬尘治理情况。");
//
//
//        list.add(map);
//        return list;
//    }
    public List<Map<String, Object>> lawslistcontext(int num){
        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        return list;
    }
    public List<Map<String, Object>> noticelistcontext(int num){
        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        return list;
    }


    @Override
    protected void lazyLoad() {
        context = getActivity();
        vHead=getActivity().getLayoutInflater().inflate(R.layout.view_head, null);
        listnews= newlistcontext(1,5);
        listView=(ListView)contactsLayout.findViewById(R.id.newslist);
        listView.addHeaderView(vHead);
        madapter = new NewsAdspter(context, listnews);
        listView.setAdapter(madapter);
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
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linyp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_yp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linbjp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_bjp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linhzp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_hzp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        linylqx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_ylqx);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(context, CheckOrderActivity.class);
                context.startActivity(intent);
            }
        });
        lintzsb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)contactsLayout.findViewById(R.id.tv_tzsb);
                Intent intent = new Intent();
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

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);
                listnews=newlistcontext(1,5);
                madapter.update(listnews);
                listtype=1;

            }
        });

        //法律

        relLawListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblue);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(linered);

                listnews= newlistcontext(2,5);
                madapter.update(listnews);
                listtype=2;
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

                listnews=  newlistcontext(3,5);
                madapter.update(listnews);
                listtype=3;
            }
        });
        SlideShowView= (wancheng.com.servicetypegovernment.view.SlideShowView) contactsLayout.findViewById(R.id.sv_photo);
        SlideShowView.setView(imageUrls);
        //初始化client
        locationClient = new AMapLocationClient(context);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {

                if (null != loc) {
                    if (loc.getErrorCode() == 0) {
                        Toast.makeText(context, " 当前定位的地点是："+loc.getAddress(), Toast.LENGTH_SHORT).show();
                        Log.e("ok", loc.getAddress());
                    } else {
                        Log.e("getErrorCode", loc.getErrorCode()+"");
                        Log.e("errorInfo", loc.getErrorInfo());
                    }
                } else {
                    Log.e("no", "定位失败");
                }
            }
        });
        locationClient.startLocation();
        //监听文本
        TopBean topBean=new TopBean("首页","","",false,false);
        getTopView(topBean,contactsLayout);

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
                                                    intent.putExtra("index",  listtype);
                                                    intent.setClass(context, NewsInfoActivity.class);
                                                    context.startActivity(intent);
                                                }


                                            }
                                        }
        );
    }



}
