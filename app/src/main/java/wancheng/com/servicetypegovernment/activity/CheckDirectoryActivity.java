package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckDirecttoryAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckResultAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class CheckDirectoryActivity extends BaseActivity {
    List<Map<String, Object>> listcontext;
    private CheckDirecttoryAdspter madapter = null;
    private ListView listView=null;
    private  String ztlx="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_directory);
        initView();
        getListData();
    }
    private void initView(){
        Intent intent=getIntent();
        ztlx=intent.getStringExtra("ztlx");
        TopBean topBean=new TopBean(intent.getStringExtra("name"),"返回","",true,false);
        getTopView(topBean);
        listView=(ListView)findViewById(R.id.check_list);
    }
    @Override
    public void updateView() {
        madapter = new CheckDirecttoryAdspter(this, listcontext,0);
        listView.setAdapter(madapter);
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

    public  void getListData(){

            listcontext=new ArrayList<Map<String, Object>>();
            pd = ProgressDialog.show(this, "", "请稍候...");
            new Thread() {
                public void run() {
                    String url= ConstUtil.METHOD_GIODELIST;
                    Map<String, Object> map = new HashMap<String, Object>();
                    try{
                        JSONObject jsonQuery = new JSONObject();
                        jsonQuery.put("ztlx", ztlx);
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
                                        JSONArray jsondata = new JSONArray(data);
                                        if(jsondata!=null&&jsondata.length()>0){
                                            for(int i=0;i<jsondata.length();i++){
                                                JSONObject onedate=jsondata.getJSONObject(i);
                                                if(onedate!=null){
                                                    //第一层
                                                    Map<String, Object> onemap=new HashMap<String, Object>();
                                                    //标题与备注
                                                    onemap.put("directtory_tittle", (i+1)+"、"+JSONUtils.getString(onedate, "name", ""));
                                                    onemap.put("directtory_remark", JSONUtils.getString(onedate, "remark", ""));
                                                    //第二层数据
                                                    List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
                                                    JSONArray infojsonarray = onedate.getJSONArray("content");
                                                    if(infojsonarray!=null&&infojsonarray.length()>0){
                                                        for(int j=0;j<infojsonarray.length();j++){
                                                            Map<String, Object> infomap=new HashMap<String, Object>();
                                                            JSONObject twodate=infojsonarray.getJSONObject(j);
                                                            infomap.put("directtory_info_tittle", (i+1)+"."+(j+1)+"、"+ JSONUtils.getString(twodate, "content", ""));
                                                            infomap.put("directtory_info_laws",JSONUtils.getString(twodate, "base", ""));
                                                            infomap.put("directtory_info_way",JSONUtils.getString(twodate, "mode", ""));
                                                            infomap.put("directtory_info_explain", JSONUtils.getString(twodate, "guide", ""));
                                                            infomap.put("isPoint", JSONUtils.getString(twodate, "isPoint", ""));
                                                            infolist.add(infomap);
                                                            onemap.put("infolist", infolist);
                                                        }
                                                    }

                                                    listcontext.add(onemap);
                                                }


                                            }
                                        }


                                    }else{
                                        msg.obj="请求异常，请稍后重试！";
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


}
