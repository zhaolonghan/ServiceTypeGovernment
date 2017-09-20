package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class IndexActivity extends BaseActivity {

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
    List<Map<String, Object>>  listnews;
    private NewsAdspter madapter = null;
    private View  vHead;
    private int listtype=1;//1公告 2法律 3新闻
    private String[] imageUrls = {"http://www.sxxynews.com/uploadfile/2015/0316/20150316095141166.jpg",
            "http://img0.imgtn.bdimg.com/it/u=645947745,2193220436&fm=214&gp=0.jpg",
            "http://jiangsu.china.com.cn/uploadfile/2016/0316/1458124624807199.png.jpg",
            "http://ww1.sinaimg.cn/orj480/999a7ed9jw1f2pc4wn1fpj20b4089aap.jpg",
            "http://www.5888.tv/Upload_Map/uploads/2014/7/2014-07-16-11-15-33.jpg"};
    /*
    * 新闻
    * */

    private ListView listView=null;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        vHead= View.inflate(this, R.layout.view_head, null);
        listnews= newlistcontext(1,5);
        listView=(ListView)findViewById(R.id.newslist);
        listView.addHeaderView(vHead);
        madapter = new NewsAdspter(this, listnews);
        listView.setAdapter(madapter);

        Intent intent =getIntent();
        int index= intent.getIntExtra("index", 0);
        oldindexsintent=intent.getIntExtra("oldindexs",0);
/*        String id= intent.getStringExtra("ids");
        Toast.makeText(this, "id:" + id, Toast.LENGTH_LONG).show();*/
        getJumpFoot(this, index,oldindexsintent);
        linFood=(LinearLayout)this.findViewById(R.id.lin_food);
        linyp=(LinearLayout)this.findViewById(R.id.lin_yaopin);
        linbjp=(LinearLayout)this.findViewById(R.id.lin_baojianpin);
        linhzp=(LinearLayout)this.findViewById(R.id.lin_huazhuangpin);
        linylqx=(LinearLayout)this.findViewById(R.id.lin_yiliaoqixie);
        lintzsb=(LinearLayout)this.findViewById(R.id.lin_tezhongshebei);
        linFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_food);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        linyp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_yp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        linbjp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_bjp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        linhzp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_hzp);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        linylqx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_ylqx);
                Intent intent = new Intent();
                intent.putExtra("companyType",tv_food.getText().toString());
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        lintzsb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final TextView tv_food=(TextView)findViewById(R.id.tv_tzsb);
                Intent intent = new Intent();
                intent.putExtra("companyType","特种设备");
                intent.setClass(IndexActivity.this, CheckOrderActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        });
        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);

        final   ColorStateList btnblue=tvlNotice.getTextColors();
        final  ColorStateList btnblack=tvNew.getTextColors();
        final  Drawable  linered=relNoticeListName.getChildAt(1).getBackground();
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
                        Toast.makeText(IndexActivity.this, " 当前定位的地点是："+loc.getAddress(), Toast.LENGTH_SHORT).show();
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
                                                    intent.setClass(IndexActivity.this, NewsInfoActivity.class);
                                                    IndexActivity.this.startActivity(intent);
                                                }


                                            }
                                        }
        );
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
    private static boolean mBackKeyPressed = false;//记录是否有首次按键


    @Override

    public void onBackPressed() {

        if (!mBackKeyPressed) {

            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

            mBackKeyPressed = true;

            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录

                @Override

                public void run() {

                    mBackKeyPressed = false;

                }
            }, 2000);

        }

        else{//退出程序

            this.finish();

            System.exit(0);

        }

    }
}
