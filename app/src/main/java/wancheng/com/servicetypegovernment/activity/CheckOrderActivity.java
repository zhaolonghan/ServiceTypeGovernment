package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.GeoUtils;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.view.PopWindow;

/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {
    private AMapLocationClient locationClient = null;
    boolean isadd=true;
    boolean isonclickadd=true;
    List<Map<String, Object>> bottondatalist;
    private LinearLayout view_1layout;//企业
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    private PopWindow popWindow;
    private boolean  isPOPOpen=false;

    private ImageView corpSearch;
    private EditText corpSearchName;
    /**企业列表里**/
    private CheckAdspter madapter = null;
    private ListView corplistView=null;;
    public List<Map<String, Object>> listcorp;
    List<Map<String, Object>> oneGetcorp;



    public    ColorStateList btnblue=null;
    public  ColorStateList btnblack=null;
    public  Drawable  linered=null;
    public   Drawable lineblack=null;
    public  String  companyType="";


    public double latloc=0;
    public  double lngloc=0;
    public static CheckOrderActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);
        instance=this;
;        initView();
        onOperationEvent();
        getListDataFirst();
    }

    @Override
    public void onResume() {
        super.onResume();
        int choose_i=-1;
        if(listcorp!=null&&listcorp.size()>0){
            for(Map<String,Object> map:listcorp){
                if(map!=null&&map.get("choose_i")!=null){
                    choose_i=Integer.parseInt(map.get("choose_i").toString());
                    Log.e("g更改数据",choose_i+"");
                        map.remove("choose_i");
                         getCorpOne(choose_i);
                       break;
                }
                //
            }
        }
        //刷新单挑数据
        if(choose_i>=0){

        }
    }
    public void chooseOneCorp(List<Map<String, Object>> newlistcorp){
        listcorp=newlistcorp;
        onResume();
    }
    public void chooseOneCorpForm(List<Map<String, Object>> newlistcorp){
        listcorp=newlistcorp;
    }
    private void initView(){
        Intent intent=getIntent();
        //企业列表
        corpquery=new CorpQuery("",intent.getStringExtra("ztlx"),1,10);
        companyType=intent.getStringExtra("companyType");
        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","",true,false);
        getTopView(topBean);

        tvlNotice = (TextView) this.findViewById(R.id.tv_notice);
        tvlLaw = (TextView) this.findViewById(R.id.tv_loyal);
        tvNew = (TextView) this.findViewById(R.id.tv_news);
        relNoticeListName = (RelativeLayout) this.findViewById(R.id.listname_notice);
        relNewsListName = (RelativeLayout) this.findViewById(R.id.listname_news);
        relLawListName = (RelativeLayout) this.findViewById(R.id.listname_law);

        btnblue=tvlNotice.getTextColors();
        btnblack=tvNew.getTextColors();
        linered=relNoticeListName.getChildAt(1).getBackground();
        lineblack=relNewsListName.getChildAt(1).getBackground();
        view_1layout = (LinearLayout) findViewById(R.id.view_1);


        tvNew.setTextColor(btnblack);
        tvlNotice.setTextColor(btnblue);
        tvlLaw.setTextColor(btnblack);
        relNewsListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.getChildAt(1).setBackground(linered);
        relLawListName.getChildAt(1).setBackground(lineblack);
        view_1layout.setVisibility(View.VISIBLE);

        corplistView=(ListView)findViewById(R.id.corplist);
        corpSearch=(ImageView)findViewById(R.id.ib_search);
        corpSearchName=(EditText)findViewById(R.id.corpSearchName);
        listcorp= new ArrayList<Map<String,Object>>();//corplistcontext(2,5);
        madapter = new CheckAdspter(this, listcorp,0 ,pd,  handler);
        corplistView.setAdapter(madapter);

    }

    @Override
    public void updateView() {
        if(oneGetcorp != null &&oneGetcorp.size()>0){
            listcorp.addAll(oneGetcorp);
        }
        madapter.update(listcorp);
        madapter.notifyDataSetChanged();
        isadd=true;
        if (isonclickadd&&bottondatalist!=null&&bottondatalist.size()>0){
            Intent intent=getIntent();
            TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","检查指南",true,true);
            getTopView(topBean);
            //指南
                isonclickadd=false;
                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPOPOpen = true;
                        popWindow = new PopWindow(CheckOrderActivity.this,bottondatalist);
                        popWindow.showPopupWindow(findViewById(R.id.tv_right));
                    }
                });
        }

    }
    public void onOperationEvent() {
        //监控筛选按钮
        corpSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isadd=true;
                String corpname=corpSearchName.getText().toString();
                corpquery.corpName=corpname;
                corpquery.pageNo=1;
                listcorp=new ArrayList<Map<String, Object>>();
                getCorpListData();
            }
        });


        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                isadd=true;
                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);
                view_1layout.setVisibility(View.VISIBLE);
                corpquery.pageNo=1;
                corpquery.sortOrder=1;
                listcorp=new ArrayList<Map<String, Object>>();
                getCorpListData();
            }
        });
        relLawListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                isadd=true;

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblue);
                //线
                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(linered);
                view_1layout.setVisibility(View.VISIBLE);
                corpquery.pageNo=1;
                corpquery.sortOrder=2;
                listcorp=new ArrayList<Map<String, Object>>();
                getCorpListData();

            }
        });
        relNewsListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                isadd=true;
                // footView(2);
                tvNew.setTextColor(btnblue);
                tvlNotice.setTextColor(btnblack);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(linered);
                relNoticeListName.getChildAt(1).setBackground(lineblack);
                relLawListName.getChildAt(1).setBackground(lineblack);
                view_1layout.setVisibility(View.VISIBLE);
                corpquery.pageNo=1;
                corpquery.sortOrder=3;
                listcorp=new ArrayList<Map<String, Object>>();
                getCorpListData();
            }
        });
        corplistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1) && isadd) {
                            isadd = false;
                            corpquery.pageNo = corpquery.pageNo + 1;
                            getCorpListData();
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
    @Override
    protected void onStop() {
        super.onStop();
        if(isPOPOpen){
            popWindow.dismiss();
            isPOPOpen=false;
        }
       // this.finish();
    }







    /**
     * 初次加载数据
     * */
    public  void getListDataFirst(){
        {
            oneGetcorp=new ArrayList<Map<String, Object>>();
            bottondatalist=new ArrayList<Map<String, Object>>();
            listcorp=new ArrayList<Map<String, Object>>();
            locationClient = new AMapLocationClient(this);
            locationClient.setLocationOption(getDefaultOption());
            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation loc) {
                    if (null != loc) {
                        lngloc =loc.getLongitude();
                        latloc=loc.getLatitude();
                        pd = ProgressDialog.show(CheckOrderActivity.this, "", "请稍候...");
                        new Thread() {
                            public void run() {
                                String url= ConstUtil.METHOD_GETCORPLIST_FIRST;
                                Map<String, Object> map = new HashMap<String, Object>();
                                try{
                                    JSONObject jsonQuery = new JSONObject();
                                    jsonQuery.put("ztlx", corpquery.ztlx);
                                    jsonQuery.put("uid", corpquery.uid);
                                    //Log.e("位置2"getlatAndlng[0]);
                                    jsonQuery.put("lng", lngloc);
                                    jsonQuery.put("lat", latloc);
                                    jsonQuery.put("sortOrder", corpquery.sortOrder);
                                    UserDateBean.getInstance().setLat(latloc);
                                    UserDateBean.getInstance().setLng(lngloc);
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
                                    msg.what=15;
                                    if (!res.isEmpty()) {
                                        JSONObject jsonObj;
                                        try {
                                            jsonObj = new JSONObject(res);
                                            String msg_code = testStringNull(jsonObj.optString("msg"));
                                            String code = testStringNull(jsonObj.optString("code"));
                                            if("0".equals(code)){
                                                String  data= Base64Coder.decodeString(jsonObj.getString("data"));
                                                if(data!=null&&data!="") {
                                                    JSONObject jsondata = new JSONObject(data);
                                                    JSONArray   dataArray = jsondata.getJSONArray("corp");
                                                    if (dataArray!=null&&dataArray.length()>0){
                                                        setcorpdata(dataArray);
                                                    }
                                                    dataArray = jsondata.getJSONArray("ztlx");
                                                    if (dataArray!=null&&dataArray.length()>0) {
                                                        {
                                                            Map<String, Object> contextmap=null;
                                                            for(int i=0;i<dataArray.length();i++){
                                                                JSONObject dataobject = dataArray.getJSONObject(i);
                                                                if(dataobject!=null){
                                                                    contextmap=new HashMap<String, Object>();
                                                                    contextmap.put("name", JSONUtils.getString(dataobject, "name", ""));
                                                                    contextmap.put("ztlx", JSONUtils.getString(dataobject, "ztlx", ""));
                                                                    bottondatalist.add(contextmap);
                                                                }

                                                            }
                                                        }
                                                    }

                                                }else{
                                                    msg.obj="已经到底了";
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
                    } else {
                        Toast.makeText(CheckOrderActivity.this, " 无法获取当前的位置" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            locationClient.startLocation();


        }
    }
    public  void getCorpListData(){

        oneGetcorp=new ArrayList<Map<String, Object>>();
        locationClient = new AMapLocationClient(this);
        locationClient.setLocationOption(getDefaultOption());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {
                if (null != loc) {
                    lngloc =loc.getLongitude();
                    latloc=loc.getLatitude();
                    double lat= UserDateBean.getUser().getLat();
                    double lng= UserDateBean.getUser().getLng();
                    double distance=UserDateBean.getUser().getDistance();
                    double nowdistance= GeoUtils.getDistance(lng, lat, lngloc,latloc);
                    if(listcorp==null||nowdistance>distance){
                        listcorp=new ArrayList<Map<String, Object>>();
                        corpquery.pageNo=1;
                        corpquery.pageSize=10;
                    }
                    pd = ProgressDialog.show(CheckOrderActivity.this, "", "请稍候...");
                    new Thread() {
                        public void run() {
                            String url= ConstUtil.METHOD_GETCORPLIST;
                            Map<String, Object> map = new HashMap<String, Object>();
                            try{
                                JSONObject jsonQuery = new JSONObject();
                                jsonQuery.put("pageNo",corpquery.pageNo);
                                jsonQuery.put("pageSize", corpquery.pageSize);
                                jsonQuery.put("corpName", corpquery.corpName);
                                jsonQuery.put("ztlx", corpquery.ztlx);
                                jsonQuery.put("uid", corpquery.uid);
                                jsonQuery.put("lng", lngloc);
                                jsonQuery.put("lat",  latloc);
                                jsonQuery.put("sortOrder", corpquery.sortOrder);
                                UserDateBean.getInstance().setLat(latloc);
                                UserDateBean.getInstance().setLng(lngloc);
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
                                msg.what=15;
                                if (!res.isEmpty()) {
                                    JSONObject jsonObj;
                                    try {
                                        jsonObj = new JSONObject(res);
                                        String msg_code = testStringNull(jsonObj.optString("msg"));
                                        String code = testStringNull(jsonObj.optString("code"));
                                        if("0".equals(code)){
                                            String  data= Base64Coder.decodeString(jsonObj.getString("data"));
                                            if(data!=null&&data!=""){
                                                JSONArray dataArray=new JSONArray(data);
                                                if (dataArray!=null&&dataArray.length()>0) {
                                                    setcorpdata(dataArray);
                                                }
                                            }else{
                                                msg.obj="已经到底了";
                                            }

                                            msg.what=14;
                                        }else{
                                            if(msg_code!=null&&!msg_code.isEmpty()){
                                                msg.obj=msg_code;
                                                msg.what=14;}
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
                } else {
                    Toast.makeText(CheckOrderActivity.this, " 无法获取当前的位置" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationClient.startLocation();

        }


//解析企业数据
    public void setcorpdata( JSONArray   dataArray) throws JSONException{
        if(listcorp==null){
            listcorp=new ArrayList<Map<String, Object>>();
        }
        if(dataArray!=null){
            Map<String, Object> contextmap=null;
            ArrayList<Map<String,Object>> corpTypeList;
            JSONArray dataTypeArray=null;
            Map<String, Object> type=null;
            for(int i=0;i<dataArray.length();i++){
                JSONObject dataobject = dataArray.getJSONObject(i);
                if(dataobject!=null){
                    String title= JSONUtils.getString(dataobject, "title", "");
                    if(title!=null&&title.length()>15){
                        title=title.substring(15)+"...";
                    }
                    if(JSONUtils.getString(dataobject, "id", "")!=null&& JSONUtils.getString(dataobject, "id", "").length()>0){
                        contextmap=new HashMap<String, Object>();
                        corpTypeList=new ArrayList<Map<String,Object>>();
                        contextmap.put("id", JSONUtils.getString(dataobject, "id", ""));
                        contextmap.put("ztlx", corpquery.ztlx);
                        contextmap.put("corp_name", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("corp_code", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("corp_person", JSONUtils.getString(dataobject, "fuzeren", ""));
                        contextmap.put("corp_tel", JSONUtils.getString(dataobject, "fuzerenTel", ""));
                        contextmap.put("corp_address", JSONUtils.getString(dataobject, "jydz", ""));
                        contextmap.put("resultId", JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("tzsbId", JSONUtils.getString(dataobject, "tzsbId", ""));
                        //距离
                        contextmap.put("lng", JSONUtils.getString(dataobject, "lng", ""));
                        contextmap.put("lat", JSONUtils.getString(dataobject, "lat", ""));
                        contextmap.put("distance", JSONUtils.getString(dataobject, "distance", ""));
                        //未检查数
                        contextmap.put("notInspectNum", JSONUtils.getString(dataobject, "notInspectNum", ""));

                        contextmap.put("inspectNum1", JSONUtils.getString(dataobject, "inspectNum1", "0"));
                        contextmap.put("inspectNum2", JSONUtils.getString(dataobject, "inspectNum2", "0"));
                        contextmap.put("inspectNum3", JSONUtils.getString(dataobject, "inspectNum3", "0"));

                        contextmap.put("sortOrder",corpquery.sortOrder);
                        if(JSONUtils.getString(dataobject, "inspectTable", "").length()>0){
                        dataTypeArray=new JSONArray(JSONUtils.getString(dataobject, "inspectTable", ""));
                        if(dataTypeArray!=null&&dataTypeArray.length()>0){
                            for(int j=0;j<dataTypeArray.length();j++){
                                JSONObject typeobject = dataTypeArray.getJSONObject(j);
                                type=new HashMap<String, Object>();
                                type.put("tableName", JSONUtils.getString(typeobject, "tableName", ""));
                                type.put("ztlx2", JSONUtils.getString(typeobject, "ztlx2", ""));
                                corpTypeList.add(type);
                            }
                        }
                    }

                        contextmap.put("corpTypeList", corpTypeList);
                        oneGetcorp.add(contextmap);
                    }

                }

            }
        }
    }





    /**企业的查询条件*/
    public CorpQuery corpquery;
    protected  class CorpQuery{
        public  String corpName="";
        public  String ztlx="";
        public  int pageNo=1;
        public  int pageSize=10;
        public  String lng="";
        public  String lat="";
        public  int  sortOrder=1;
        public  String uid= UserDateBean.getUser().getId();

        public CorpQuery(){

        }
        public CorpQuery(String corpName,String ztlx,int pageNo ,int pageSize){
            this.corpName=corpName;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
        }

    }


    public  void getCorpOne(final int choose_i){
        oneGetcorp=new ArrayList<Map<String, Object>>();
         pd = ProgressDialog.show(this, "", "请稍候...");
            new Thread() {
                public void run() {
                    String url= ConstUtil.METHOD_GETCORPLIST;
                    Map<String, Object> map = new HashMap<String, Object>();
                    try{
                        JSONObject jsonQuery = new JSONObject();
                        jsonQuery.put("ztlx", corpquery.ztlx);
                        jsonQuery.put("uid", corpquery.uid);
                        jsonQuery.put("lng", UserDateBean.getUser().getLng());
                        jsonQuery.put("lat", UserDateBean.getUser().getLat());
                        jsonQuery.put("corpId", listcorp.get(choose_i).get("id").toString());
                        //特种设备id
                        if (listcorp.get(choose_i).get("tzsbId")!=null){
                            jsonQuery.put("tzsbId", listcorp.get(choose_i).get("tzsbId").toString());
                        }

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
                        msg.what=15;
                        if (!res.isEmpty()) {
                            JSONObject jsonObj;
                            try {
                                jsonObj = new JSONObject(res);
                                String msg_code = testStringNull(jsonObj.optString("msg"));
                                String code = testStringNull(jsonObj.optString("code"));
                                if("0".equals(code)){
                                    String  data= Base64Coder.decodeString(jsonObj.getString("data"));
                                    if(data!=null&&data!=""){
                                        JSONArray dataArray=new JSONArray(data);
                                        if (dataArray!=null&&dataArray.length()>0) {
                                            Map<String, Object>  oneMap=getcorpOnedata(dataArray);
                                            //刷新页面
                                            if(oneMap!=null){
                                                listcorp.get(choose_i).putAll(oneMap);
                                            }else{
                                                listcorp.remove(choose_i);
                                            }

                                        }
                                    }else{
                                        msg.obj="";
                                    }

                                    msg.what=14;
                                }else{
                                    if(msg_code!=null&&!msg_code.isEmpty()){
                                        msg.obj=msg_code;
                                        msg.what=14;}
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
            }.start(); }

    public Map<String, Object> getcorpOnedata( JSONArray   dataArray) throws JSONException{
        if(dataArray!=null){
            Map<String, Object> contextmap=null;
            ArrayList<Map<String,Object>> corpTypeList;
            JSONArray dataTypeArray=null;
            Map<String, Object> type=null;
            for(int i=0;i<dataArray.length();i++){
                JSONObject dataobject = dataArray.getJSONObject(i);
                if(dataobject!=null){
                    String title= JSONUtils.getString(dataobject, "title", "");
                    if(title!=null&&title.length()>15){
                        title=title.substring(15)+"...";
                    }
                    if(JSONUtils.getString(dataobject, "id", "")!=null&& JSONUtils.getString(dataobject, "id", "").length()>0){
                        contextmap=new HashMap<String, Object>();
                        corpTypeList=new ArrayList<Map<String,Object>>();
                        contextmap.put("id", JSONUtils.getString(dataobject, "id", ""));
                        contextmap.put("ztlx", corpquery.ztlx);
                        contextmap.put("corp_name", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("corp_code", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("corp_person", JSONUtils.getString(dataobject, "fuzeren", ""));
                        contextmap.put("corp_tel", JSONUtils.getString(dataobject, "fuzerenTel", ""));
                        contextmap.put("corp_address", JSONUtils.getString(dataobject, "jydz", ""));
                        contextmap.put("resultId", JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("tzsbId", JSONUtils.getString(dataobject, "tzsbId", ""));
                        //距离
                        contextmap.put("lng", JSONUtils.getString(dataobject, "lng", ""));
                        contextmap.put("lat", JSONUtils.getString(dataobject, "lat", ""));
                        contextmap.put("distance", JSONUtils.getString(dataobject, "distance", ""));
                        //未检查数
                        contextmap.put("notInspectNum", JSONUtils.getString(dataobject, "notInspectNum", ""));
                        contextmap.put("inspectNum1", JSONUtils.getString(dataobject, "inspectNum1", "0"));
                        contextmap.put("inspectNum2", JSONUtils.getString(dataobject, "inspectNum2", "0"));
                        contextmap.put("inspectNum3", JSONUtils.getString(dataobject, "inspectNum3", "0"));
                        if(JSONUtils.getString(dataobject, "inspectTable", "").length()>0){
                            dataTypeArray=new JSONArray(JSONUtils.getString(dataobject, "inspectTable", ""));
                            if(dataTypeArray!=null&&dataTypeArray.length()>0){
                                for(int j=0;j<dataTypeArray.length();j++){
                                    JSONObject typeobject = dataTypeArray.getJSONObject(j);
                                    type=new HashMap<String, Object>();
                                    type.put("tableName", JSONUtils.getString(typeobject, "tableName", ""));
                                    type.put("ztlx2", JSONUtils.getString(typeobject, "ztlx2", ""));
                                    corpTypeList.add(type);
                                }
                            }
                        }

                        contextmap.put("corpTypeList", corpTypeList);
                        return contextmap;
                    }

                }

            }
        }
        return null;
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

    @Override
    public void updataData() {
        super.updataData();
        onResume();
    }
}
