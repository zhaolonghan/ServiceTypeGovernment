package wancheng.com.servicetypegovernment.activity;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckHistoryAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class CheckHistoryListActivity extends BaseActivity {

    private RelativeLayout rela_history;
    List<Map<String, Object>> listcontext= new ArrayList<Map<String, Object>>();
    private CheckHistoryAdspter madapter = null;
    private ListView listView=null;
    private  boolean isadd=true;//是否添加
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history_list);
        //  rela_history=(RelativeLayout)findViewById(R.id.rela_history);
        initView();
        getHistoryListData();
        onOperationEvent();
    }

    private void initView(){
        TopBean topBean=new TopBean("历史检查记录","返回","下一步",true,false);
        getTopView(topBean);
        listView=(ListView)findViewById(R.id.check_history);
        Intent intent=getIntent();
        historyQuery =new HistoryQuery(intent.getStringExtra("corpId"),intent.getStringExtra("ztlx"),1,20);
        madapter = new CheckHistoryAdspter(this,listcontext );
        listView.setAdapter(madapter);
    }


    @Override
    public void updateView() {
           if(madapter ==null){
                madapter = new CheckHistoryAdspter(this, listcontext);
                listView.setAdapter(madapter);
            }else{
                madapter.update(listcontext);
                madapter.notifyDataSetChanged();
            }
        isadd=true;
    }
    public void onOperationEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Map<String, Object> map = listcontext.get(i);
                                                if (map.get("id") != null) {
                                                    String id = map.get("id").toString();
                                                    Intent intent = new Intent();
                                                    // intent.putExtra("id",((TextView)((RelativeLayout)listView.getChildAt(i)).getChildAt(0)).getText());
                                                    intent.putExtra("ids", id);

                                                    intent.setClass(CheckHistoryListActivity.this, CheckResultDetailActivity.class);
                                                    CheckHistoryListActivity.this.startActivity(intent);
                                                }


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
                            madapter.add(listcontext);
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

    public  void getHistoryListData(){
        if(listcontext==null){
            listcontext=new ArrayList<Map<String, Object>>();
        }
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_HISTORYLIST;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("pageNo",historyQuery.pageNo);
                    jsonQuery.put("pageSize", historyQuery.pageSize);
                    jsonQuery.put("corpName", historyQuery.corpId);
                    jsonQuery.put("ztlx", historyQuery.ztlx);
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
                                    if (dataArray!=null&&dataArray.length()>0) {// 1符合  2基本符合  3不符合
                                        setdata(dataArray);
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
    private void initdata() {

        listcontext= listcontext(18);
        madapter = new CheckHistoryAdspter(this, listcontext);
        listView.setAdapter(madapter);


    }
    //解析执法检查
    public void setdata( JSONArray   dataArray) throws JSONException{
        if(listcontext==null){
            listcontext=new ArrayList<Map<String, Object>>();
        }
        if(dataArray!=null){
            Map<String, Object> contextmap=null;
            for(int i=0;i<dataArray.length();i++){
                JSONObject dataobject = dataArray.getJSONObject(i);
                if(dataobject!=null){
                    contextmap=new HashMap<String, Object>();
                    if(JSONUtils.getString(dataobject, "time", "")!=null&&JSONUtils.getString(dataobject, "time", "").length()>0) {
                        contextmap.put("id",JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("history_time", DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "time", "0")))));
                        contextmap.put("result",JSONUtils.getString(dataobject, "result", ""));
                        contextmap.put("type",JSONUtils.getString(dataobject, "type", ""));
                        contextmap.put("result",JSONUtils.getString(dataobject, "result", ""));

                        listcontext.add(contextmap);
                    }
                }

            }
        }
    }
    public List<Map<String, Object>> listcontext(int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String history_time="2017-08-24";
        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("id",j);
            map.put("history_time",history_time);
            list.add(map);
        }

        return list;

    }
    public HistoryQuery historyQuery;
    protected  class HistoryQuery{
        public  String corpId="";
        public  String ztlx="";
        public  int pageNo=1;
        public  int pageSize=20;
        public HistoryQuery(){

        }
        public HistoryQuery(String corpId,String ztlx,int pageNo ,int pageSize){
            this.corpId=corpId;
            this.ztlx=ztlx;
            this.pageNo=pageNo;
            this.pageSize=pageSize;
        }

    }
}
