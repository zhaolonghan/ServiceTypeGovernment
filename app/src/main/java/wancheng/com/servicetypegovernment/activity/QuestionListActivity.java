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
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
public class QuestionListActivity extends BaseActivity {
    boolean isadd=true;
    boolean isonclickadd=true;

    private PopWindow popWindow;
    private boolean  isPOPOpen=false;


    /**执法**/
    private CheckAdspter questionadapter = null;
    private ListView questionlistView=null;;
    List<Map<String, Object>> listquestion;
    List<Map<String, Object>> oneGetquestion;

    public    ColorStateList btnblue=null;
    public  ColorStateList btnblack=null;
    public  Drawable  linered=null;
    public   Drawable lineblack=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionlist);
;        initView();
        onOperationEvent();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getquestionListData();

    }
    private void initView(){
        Intent intent=getIntent();
        questionquery=new QuestionQuery(intent.getStringExtra("corpName"),intent.getStringExtra("ztlx"),1,10);
        TopBean topBean=new TopBean("问题处置","返回","",true,false);
        getTopView(topBean);
        listquestion= new ArrayList<Map<String,Object>>();//corplistcontext(2,5);
        questionlistView=(ListView)findViewById(R.id.questionlist);
        questionadapter = new CheckAdspter(this, listquestion,2,pd,handler);
        questionlistView.setAdapter(questionadapter);

    }

    @Override
    public void updateView() {


        if(oneGetquestion!=null&&oneGetquestion.size()>0){
            listquestion.addAll(oneGetquestion);
        }
        questionadapter.update(listquestion);
        questionadapter.notifyDataSetChanged();
        isadd=true;

    }
    public void onOperationEvent() {


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
                                String  data= Base64Coder.decodeString(jsonObj.getString("data"));
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
                    if(JSONUtils.getString(dataobject, "time", "")!=null&& JSONUtils.getString(dataobject, "time", "").length() > 0) {
                        contextmap.put("id", JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("question_date", JSONUtils.getString(dataobject, "time", "0"));
                        contextmap.put("question_corpname", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("question_no", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("question_result", JSONUtils.getString(dataobject, "result", ""));
                        contextmap.put("question_management", JSONUtils.getString(dataobject, "zhuzhi", ""));
                        contextmap.put("question_status", JSONUtils.getString(dataobject, "status", ""));
                        contextmap.put("question_limit", JSONUtils.getString(dataobject, "deadline", "0").length() > 0 ? DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "deadline", "0")))):"");
                        contextmap.put("uid", UserDateBean.getUser().getId());
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
    /**执法检查的查询条件*/
    public CheckQuery checkQuery;
    protected  class CheckQuery{
        public  String corpName="";
        public  String ztlx="";
        public  int pageNo=1;
        public  int pageSize=10;
        public  String uid= UserDateBean.getUser().getId();

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
        public  String uid= UserDateBean.getUser().getId();

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
