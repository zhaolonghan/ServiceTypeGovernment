package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
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
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.view.PopWindow;

/**
 * test
 */
public class CheckListActivity extends BaseActivity {
    boolean isadd=true;
    boolean isonclickadd=true;
    List<Map<String, Object>> bottondatalist;
    private LinearLayout view_1layout;//企业


    private PopWindow popWindow;
    private boolean  isPOPOpen=false;

    private ImageView corpSearch;
    private EditText corpSearchName;
    /**企业列表里**/
    private CheckAdspter madapter = null;
    private ListView corplistView=null;;
    List<Map<String, Object>> listcorp;
    List<Map<String, Object>> oneGetcorp;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
;        initView();
        getCorpListData();
        onOperationEvent();
    }
    private void initView(){
        Intent intent=getIntent();
        //企业列表
        corpquery=new CorpQuery("",intent.getStringExtra("ztlx"),1,10,intent.getStringExtra("specialId"));


        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","",true, false);
        getTopView(topBean);



        view_1layout = (LinearLayout) findViewById(R.id.view_1);

        view_1layout.setVisibility(View.VISIBLE);

        corplistView=(ListView)findViewById(R.id.corplist);
        corpSearch=(ImageView)findViewById(R.id.ib_search);
        corpSearchName=(EditText)findViewById(R.id.corpSearchName);
        listcorp=new ArrayList<Map<String, Object>>();
        madapter = new CheckAdspter(this, listcorp,0 ,pd,  handler);
        corplistView.setAdapter(madapter);

    }

    @Override
    public void updateView() {
        if(oneGetcorp!=null&&oneGetcorp.size()>0){
            listcorp.addAll(oneGetcorp);
        }
        madapter.update(listcorp);
        madapter.notifyDataSetChanged();


        //questionadapter.notifyDataSetChanged();
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
                        popWindow = new PopWindow(CheckListActivity.this,bottondatalist);
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
                isadd = true;

                String corpname = corpSearchName.getText().toString();
                corpquery.corpName = corpname;
                corpquery.pageNo = 1;
                listcorp = new ArrayList<Map<String, Object>>();
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








    public  void getCorpListData(){
        if(listcorp==null){
            listcorp=new ArrayList<Map<String, Object>>();
        }
        oneGetcorp=new ArrayList<Map<String, Object>>();
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_SPECIALCORPLIST;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("pageNo",corpquery.pageNo);
                    jsonQuery.put("pageSize", corpquery.pageSize);
                    jsonQuery.put("corpName", corpquery.corpName);
                    jsonQuery.put("ztlx", corpquery.ztlx);
                    jsonQuery.put("uid", corpquery.uid);
                    jsonQuery.put("specialId", corpquery.specialId);
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
                    if(JSONUtils.getString(dataobject, "id", "")!=null&& JSONUtils.getString(dataobject, "id", "").length()>0){
                        contextmap=new HashMap<String, Object>();
                        corpTypeList=new ArrayList<Map<String,Object>>();
                        contextmap.put("id", JSONUtils.getString(dataobject, "id", ""));
                        contextmap.put("ztlx", JSONUtils.getString(dataobject, "ztlx2", ""));
                        contextmap.put("ztlx2", JSONUtils.getString(dataobject, "ztlx2", ""));
                        contextmap.put("corp_name", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("corp_code", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("corp_person", JSONUtils.getString(dataobject, "fuzeren", ""));
                        contextmap.put("corp_tel", JSONUtils.getString(dataobject, "fuzerenTel", ""));
                        contextmap.put("corp_address", JSONUtils.getString(dataobject, "jydz", ""));
                        contextmap.put("resultId", JSONUtils.getString(dataobject, "resultId", ""));
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
        public  String uid= UserDateBean.getUser().getId();
        public String specialId="";
        public CorpQuery(){

        }
        public CorpQuery(String corpName,String ztlx,int pageNo ,int pageSize,String specialId){
            this.corpName=corpName;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
            this.specialId=specialId;
        }

    }



}
