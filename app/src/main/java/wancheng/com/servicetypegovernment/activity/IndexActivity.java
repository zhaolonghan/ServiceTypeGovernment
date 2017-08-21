package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import android.util.Log;
import android.view.View;
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
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.SlideShowView;

public class IndexActivity extends BaseActivity {

    private AMapLocationClient locationClient = null;
    private SlideShowView SlideShowView;
    private LinearLayout linFood;//食品企业
    private RelativeLayout relNewsListName;//新闻动态
    private RelativeLayout relNoticeListName;//通知公告
    private RelativeLayout relLawListName;//法律法规
    private TextView tvNew;//新闻动态
    private TextView tvlNotice;//通知公告
    private TextView tvlLaw;//法律法规
    List<Map<String, Object>>  listnews;
    private NewsAdspter madapter = null;
    private View  vHead;
    /*
    * 新闻
    * */
/*    private  RelativeLayout relrela_1;
    private  RelativeLayout relrela_2;
    private  RelativeLayout relrela_3;
    private  RelativeLayout relrela_4;
    private  RelativeLayout relrela_5;*/
    private ListView listView=null;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        vHead= View.inflate(this, R.layout.view_head, null);
        listnews= newlistcontext(1);
        listView=(ListView)findViewById(R.id.newslist);
        listView.addHeaderView(vHead);
        madapter = new NewsAdspter(this, listnews);
        listView.setAdapter(madapter);
        Log.e("test!!!!!!!!!!!!!!!!!!","11111111111111111111111111111111111111");

        linFood=(LinearLayout)this.findViewById(R.id.lin_food);
        linFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_food);
                Toast.makeText(IndexActivity.this, "跳转检查页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });

        tvNew=(TextView)this.findViewById(R.id.tv_news);
        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);

        final   ColorStateList btnblue=tvNew.getTextColors();
        final  ColorStateList btnblack=tvlNotice.getTextColors();
        final  Drawable  linered=relNewsListName.getChildAt(1).getBackground();
        final   Drawable lineblack=relNoticeListName.getChildAt(1).getBackground();


//新闻
        relNewsListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblue);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(linered);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(lineblack);
                Toast.makeText(IndexActivity.this, "新闻列表", Toast.LENGTH_SHORT).show();

                listnews=  newlistcontext(1);
                madapter.update(listnews);
            }
        });
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
                Toast.makeText(IndexActivity.this, "公告列表", Toast.LENGTH_SHORT).show();

                listnews=newlistcontext(2);
                madapter.update(listnews);


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

                listnews= newlistcontext(3);
                madapter.update(listnews);
                Toast.makeText(IndexActivity.this, "法律列表", Toast.LENGTH_SHORT).show();
            }
        });

        String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
                "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
                "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
                "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
                "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
        SlideShowView= (wancheng.com.servicetypegovernment.view.SlideShowView) this.findViewById(R.id.sv_photo);
        SlideShowView.setView(imageUrls);
        //初始化client
        locationClient = new AMapLocationClient(getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {

                if (null != loc) {
                    if (loc.getErrorCode() == 0) {

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
        getTopView(topBean);
    }
    public  List<Map<String, Object>>  newlistcontext(int i){
        List<Map<String, Object>>  list;
      /*  relrela_1=(RelativeLayout)this.findViewById(R.id.rela_1);;
        relrela_2=(RelativeLayout)this.findViewById(R.id.rela_2);;;
        relrela_3=(RelativeLayout)this.findViewById(R.id.rela_3);;
        relrela_4=(RelativeLayout)this.findViewById(R.id.rela_4);;
        relrela_5=(RelativeLayout)this.findViewById(R.id.rela_5);;*/
        //标题0  时间1  内容2String id="1";
        String title="";
        String time="";
        String content="";
        switch (i){
            case 1:
                title="新闻新闻新闻新闻新闻新闻新闻新闻新闻新";
                time="2016-12-20";
                content="新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容";
                break;
            case 2:
                title="公告公告公告公告公告公告公告公告公告公公告";
                time="2017-05-20";
                content="公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容";
                break;
            case 3:
                title="法律法律法律法律法律法律法律法律法律法律";
                time="2017-06-20";
                content="法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规";
                break;
        }


        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<6;j++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("id",j);
            map.put("title",title);
            map.put("time",time);
            map.put("context",content);
            list.add(map);
        }


   /*TextView  viewtittle= (TextView)relrela_1.getChildAt(0);
        TextView  viewtime=(TextView) relrela_1.getChildAt(1);
        TextView  viewcontext= (TextView)relrela_1.getChildAt(2);


    TextView  viewtittle2= (TextView)relrela_2.getChildAt(0);
        TextView  viewtime2=(TextView) relrela_2.getChildAt(1);
    TextView  viewcontext2= (TextView)relrela_2.getChildAt(2);

    TextView  viewtittle3= (TextView)relrela_3.getChildAt(0);
        TextView  viewtime3= (TextView)relrela_3.getChildAt(1);
    TextView  viewcontext3= (TextView)relrela_3.getChildAt(2);

    TextView  viewtittle4= (TextView)relrela_4.getChildAt(0);
        TextView  viewtime4= (TextView)relrela_4.getChildAt(1);
    TextView  viewcontext4= (TextView)relrela_4.getChildAt(2);

    TextView  viewtittle5= (TextView)relrela_5.getChildAt(0);
        TextView  viewtime5=(TextView)relrela_5.getChildAt(1);
    TextView  viewcontext5= (TextView)relrela_5.getChildAt(2);

        viewtittle.setText(title);
        viewtime.setText(time);
        viewcontext.setText(content);

        viewtittle2.setText(title);
        viewtime2.setText(time);
        viewcontext2.setText(content);


        viewtittle3.setText(title);
        viewtime3.setText(time);
        viewcontext3.setText(content);

        viewtittle4.setText(title);
        viewtime4.setText(time);
        viewcontext4.setText(content);

        viewtittle5.setText(title);
        viewtime5.setText(time);
        viewcontext5.setText(content);*/
        // listView=(ListView)findViewById(R.id.newslist);
       /*  listView.setAdapter(new NewsAdspter(this,listnews));*/
        //  madapter.update(listnews);
        return list;

    }
    private AMapLocationClientOption getDefaultOption(){
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

    public void updateListView() {

    }
}
