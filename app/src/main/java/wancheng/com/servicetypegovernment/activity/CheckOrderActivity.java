package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.view.PopWindow;
/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {
    boolean isadd=true;
    boolean isonclickadd=true;
    List<Map<String, Object>> bottondatalist;
    private LinearLayout view_1layout;//企业
    private LinearLayout view_2layout;//检查
    private LinearLayout view_3layout;//问题
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    private PopWindow popWindow;
    private boolean  isPOPOpen=false;
    private ImageView ib_search1;
    private EditText question_corpnamesearch;
    private ImageView corpSearch;
    private EditText corpSearchName;
    /**企业列表里**/
    private CheckAdspter madapter = null;
    private ListView corplistView=null;;
    List<Map<String, Object>> listcorp;
    List<Map<String, Object>> oneGetcorp;

    /**执法**/
    private CheckAdspter questionadapter = null;
    private ListView questionlistView=null;;
    List<Map<String, Object>> listquestion;
    List<Map<String, Object>> oneGetquestion;

    /**问题*/
    /**执法**/
    private CheckAdspter enforcementadapter = null;
    private ListView enforcementlistView=null;;
    List<Map<String, Object>> listenforcement;
    List<Map<String, Object>> oneGetenforcement;


    public    ColorStateList btnblue=null;
    public  ColorStateList btnblack=null;
    public  Drawable  linered=null;
    public   Drawable lineblack=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);
;        initView();
        getListDataFirst();
        onOperationEvent();
    }
    private void initView(){
        Intent intent=getIntent();
        //企业列表
        corpquery=new CorpQuery("",intent.getStringExtra("ztlx"),1,10);
        checkQuery=new CheckQuery("",intent.getStringExtra("ztlx"),1,10);
        questionquery=new QuestionQuery("",intent.getStringExtra("ztlx"),1,10);

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
        view_2layout = (LinearLayout) findViewById(R.id.view_2);
        view_3layout = (LinearLayout) findViewById(R.id.view_3);

        tvNew.setTextColor(btnblack);
        tvlNotice.setTextColor(btnblue);
        tvlLaw.setTextColor(btnblack);
        relNewsListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.getChildAt(1).setBackground(linered);
        relLawListName.getChildAt(1).setBackground(lineblack);
        view_1layout.setVisibility(View.VISIBLE);
        view_2layout.setVisibility(View.GONE);
        view_3layout.setVisibility(View.GONE);
        corplistView=(ListView)findViewById(R.id.corplist);
        corpSearch=(ImageView)findViewById(R.id.ib_search);
        corpSearchName=(EditText)findViewById(R.id.corpSearchName);
        ib_search1=(ImageView)findViewById(R.id.ib_search1);
        question_corpnamesearch=(EditText)findViewById(R.id.question_corpnamesearch);

        //执法
        enforcementlistView=(ListView)findViewById(R.id.enforcementlist);

        listquestion= new ArrayList<Map<String,Object>>();//corplistcontext(2,5);
        questionlistView=(ListView)findViewById(R.id.questionlist);
        questionadapter = new CheckAdspter(this, listquestion,2);
        questionlistView.setAdapter(questionadapter);

        madapter = new CheckAdspter(this, listquestion,0);
        corplistView.setAdapter(madapter);
        enforcementadapter = new CheckAdspter(this, listquestion,1);
        enforcementlistView.setAdapter(enforcementadapter);
    }

    @Override
    public void updateView() {
        if(oneGetcorp!=null&&oneGetcorp.size()>0){
            listcorp.addAll(oneGetcorp);
        }
        madapter.update(listcorp);
        madapter.notifyDataSetChanged();

       if(oneGetenforcement!=null&&oneGetenforcement.size()>0){
            listenforcement.addAll(oneGetenforcement);
        }
       enforcementadapter.update(listenforcement);
       enforcementadapter.notifyDataSetChanged();

        if(oneGetquestion!=null&&oneGetquestion.size()>0){
            listquestion.addAll(oneGetquestion);
        }
        questionadapter.update(listquestion);
        questionadapter.notifyDataSetChanged();
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

        ib_search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isadd=true;
                String corpname=question_corpnamesearch.getText().toString();
                questionquery.corpName=corpname;
                questionquery.pageNo=1;
                listquestion=new ArrayList<Map<String, Object>>();
                getquestionListData();
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
                view_2layout.setVisibility(View.GONE);
                view_3layout.setVisibility(View.GONE);


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
                view_2layout.setVisibility(View.VISIBLE);
                view_1layout.setVisibility(View.GONE);
                view_3layout.setVisibility(View.GONE);

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
                view_3layout.setVisibility(View.VISIBLE);
                view_2layout.setVisibility(View.GONE);
                view_1layout.setVisibility(View.GONE);

            }
        });

        corplistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部


                        if (view.getLastVisiblePosition() == (view.getCount() - 1)&&isadd) {
                            isadd=false;
                            // null, null listcorp.size() /corpquery.pageSize + 2, 10

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
        //执法
        enforcementlistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1) && isadd) {
                            isadd = false;
                            checkQuery.pageNo = checkQuery.pageNo + 1;
                            getenforcementListData();

                        }

                        break;

                }

            }


            @Override

            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {

            }

        });
        questionlistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {

                    // 当不滚动时

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部

                        if (view.getLastVisiblePosition() == (view.getCount() - 1) && isadd) {
                            isadd = false;
                            questionquery.pageNo = questionquery.pageNo + 1;
                            getquestionListData();

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
            oneGetenforcement=new ArrayList<Map<String, Object>>();
            oneGetquestion=new ArrayList<Map<String, Object>>();
            bottondatalist=new ArrayList<Map<String, Object>>();

            pd = ProgressDialog.show(this, "", "请稍候...");
            new Thread() {
                public void run() {
                    String url= ConstUtil.METHOD_GETCORPLIST_FIRST;
                    //   .put("ide", "Eclispe").put("name", "java");m
                    Map<String, Object> map = new HashMap<String, Object>();
                    try{
                        JSONObject jsonQuery = new JSONObject();
                        jsonQuery.put("ztlx", corpquery.ztlx);
                        jsonQuery.put("uid", corpquery.uid);
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
                                    String  data=Base64Coder.decodeString(jsonObj.getString("data"));
                                    if(data!=null&&data!="") {
                                        JSONObject jsondata = new JSONObject(data);
                                        JSONArray   dataArray = jsondata.getJSONArray("corp");
                                        if (dataArray!=null&&dataArray.length()>0){
                                            setcorpdata(dataArray);
                                        }
                                        dataArray = jsondata.getJSONArray("special");
                                        if (dataArray!=null&&dataArray.length()>0) {
                                            setCheckdata(dataArray);
                                        }
                                        dataArray = jsondata.getJSONArray("result");
                                        if (dataArray!=null&&dataArray.length()>0) {
                                            setQuestiondata(dataArray);
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
            }.start();}


    }
    public  void getCorpListData(){
        if(listcorp==null){
            listcorp=new ArrayList<Map<String, Object>>();
        }
        oneGetcorp=new ArrayList<Map<String, Object>>();
        pd = ProgressDialog.show(this, "", "请稍候...");
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
                        String  data=Base64Coder.decodeString(jsonObj.getString("data"));
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
        }.start();}

    public  void getenforcementListData(){
        if(listenforcement==null){
            listenforcement=new ArrayList<Map<String, Object>>();
        }
        oneGetenforcement=new ArrayList<Map<String, Object>>();
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_SPECIALLIST;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("pageNo",checkQuery.pageNo);
                    jsonQuery.put("pageSize", checkQuery.pageSize);
                    jsonQuery.put("corpName", checkQuery.corpName);
                    jsonQuery.put("ztlx", checkQuery.ztlx);
                    jsonQuery.put("uid", checkQuery.uid);
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
                                String  data=Base64Coder.decodeString(jsonObj.getString("data"));
                                if(data!=null&&data!=""){
                                    JSONArray dataArray=new JSONArray(data);
                                    setcorpdata(dataArray);
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
        }.start();}
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
                    if(JSONUtils.getString(dataobject, "id", "")!=null&&JSONUtils.getString(dataobject, "id", "").length()>0){
                        contextmap=new HashMap<String, Object>();
                        corpTypeList=new ArrayList<Map<String,Object>>();
                        contextmap.put("id",JSONUtils.getString(dataobject, "id", ""));
                        contextmap.put("ztlx", corpquery.ztlx);
                        contextmap.put("corp_name",JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("corp_code",JSONUtils.getString(dataobject, "code",""));
                        contextmap.put("corp_person",JSONUtils.getString(dataobject, "fuzeren",""));
                        contextmap.put("corp_tel",JSONUtils.getString(dataobject, "fuzerenTel",""));
                        contextmap.put("corp_address", JSONUtils.getString(dataobject, "jydz",""));
                        dataTypeArray=new JSONArray(JSONUtils.getString(dataobject, "inspectTable",""));
                        if(dataTypeArray!=null&&dataTypeArray.length()>0){
                            for(int j=0;j<dataTypeArray.length();j++){
                                JSONObject typeobject = dataTypeArray.getJSONObject(j);
                                type=new HashMap<String, Object>();
                                type.put("tableName",JSONUtils.getString(typeobject, "tableName", ""));
                                type.put("ztlx2",JSONUtils.getString(typeobject, "ztlx2",""));
                                corpTypeList.add(type);
                            }
                        }
                        contextmap.put("corpTypeList", corpTypeList);
                        oneGetcorp.add(contextmap);
                    }

                }

            }
        }
    }
//解析执法检查
public void setCheckdata( JSONArray   dataArray) throws JSONException{
    if(listenforcement==null){
        listenforcement=new ArrayList<Map<String, Object>>();
    }
    if(dataArray!=null){
        Map<String, Object> contextmap=null;
        for(int i=0;i<dataArray.length();i++){
            JSONObject dataobject = dataArray.getJSONObject(i);
            if(dataobject!=null){
                contextmap=new HashMap<String, Object>();
                if(JSONUtils.getString(dataobject, "startTime", "")!=null&&JSONUtils.getString(dataobject, "startTime", "").length()>0) {
                    contextmap.put("id",JSONUtils.getString(dataobject, "id", ""));
                    contextmap.put("check_date", DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "startTime", "0")))));
                    contextmap.put("check_corpnum", JSONUtils.getString(dataobject, "corpCount", ""));
                    contextmap.put("check_status", JSONUtils.getString(dataobject, "status", ""));
                    contextmap.put("check_numed", JSONUtils.getString(dataobject, "checked", ""));
                    contextmap.put("check_numing", JSONUtils.getString(dataobject, "notChecked", ""));
                    contextmap.put("check_numthrought", JSONUtils.getString(dataobject, "hege", ""));
                    contextmap.put("check_numunthrought", JSONUtils.getString(dataobject, "buhege", ""));
                    contextmap.put("check_radioing", JSONUtils.getString(dataobject, "jindu", ""));
                    contextmap.put("check_radiothrought", JSONUtils.getString(dataobject, "hegelv", ""));
                    oneGetenforcement.add(contextmap);
                }
            }

        }
    }
}
    //解析问题处置
    public  void getquestionListData(){
        if(listquestion==null){
            listquestion=new ArrayList<Map<String, Object>>();
        }
        oneGetquestion=new ArrayList<Map<String, Object>>();
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_QUESTIONLIST;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("pageNo",questionquery.pageNo);
                    jsonQuery.put("pageSize", questionquery.pageSize);
                    jsonQuery.put("corpName", questionquery.corpName);
                    jsonQuery.put("ztlx", questionquery.ztlx);
                    jsonQuery.put("uid", questionquery.uid);
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
                                String  data=Base64Coder.decodeString(jsonObj.getString("data"));
                                if(data!=null&&data!=""){
                                    JSONArray dataArray=new JSONArray(data);
                                    setQuestiondata(dataArray);
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
        }.start();}

    public void setQuestiondata( JSONArray   dataArray) throws JSONException{
        if(listquestion==null){
            listquestion=new ArrayList<Map<String, Object>>();
        }
        if(dataArray!=null){
            Map<String, Object> contextmap=null;
            for(int i=0;i<dataArray.length();i++){
                JSONObject dataobject = dataArray.getJSONObject(i);
                if(dataobject!=null){
                    contextmap=new HashMap<String, Object>();
                    if(JSONUtils.getString(dataobject, "time", "")!=null&&JSONUtils.getString(dataobject, "time", "").length() > 0) {
                        contextmap.put("id",JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("question_date", DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "time", "0")))));
                        contextmap.put("question_corpname", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("question_no", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("question_result", JSONUtils.getString(dataobject, "result", ""));
                        contextmap.put("question_management", JSONUtils.getString(dataobject, "zhuzhi", ""));
                        contextmap.put("question_status", JSONUtils.getString(dataobject, "status", ""));
                        contextmap.put("question_limit", JSONUtils.getString(dataobject, "deadline", "0").length() > 0 ? DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "deadline", "0")))):"");
                        oneGetquestion.add(contextmap);
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
        public  String uid=UserDateBean.getUser().getId();

        public CorpQuery(){

        }
        public CorpQuery(String corpName,String ztlx,int pageNo ,int pageSize){
            this.corpName=corpName;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
        }

    }
    /**执法检查的查询条件*/
    public CheckQuery checkQuery;
    protected  class CheckQuery{
        public  String corpName="";
        public  String ztlx="";
        public  int pageNo=1;
        public  int pageSize=10;
        public  String uid=UserDateBean.getUser().getId();

        public CheckQuery(){

        }
        public CheckQuery(String corpName,String ztlx,int pageNo ,int pageSize){
            this.corpName=corpName;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
        }

    }
    /**问题处置的查询条件*/
    public QuestionQuery questionquery;
    protected  class QuestionQuery{
        public  String corpName="";
        public  String ztlx="";
        public  int pageNo=1;
        public  int pageSize=10;
        public  String uid=UserDateBean.getUser().getId();

        public QuestionQuery(){

        }
        public QuestionQuery(String corpName,String ztlx,int pageNo ,int pageSize){
            this.corpName=corpName;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
        }

    }


}
