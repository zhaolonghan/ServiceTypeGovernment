package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import wancheng.com.servicetypegovernment.adspter.CheckQuestionAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckResultAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class CheckResultDetailActivity extends BaseActivity {
    private ScrollView view_1layout;
    private LinearLayout view_2layout;
    private ScrollView view_3layout;
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    List<Map<String, Object>> listnews;
    private CheckResultAdspter madapter = null;
    private ListView listView=null;
    private  String resultid="";
   // METHOD_INSPECTRESULTDETAIL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result_detail);
        initView();
        onOperationEvent();
        getData();


    }
    @Override
    public void updateView() {
            //公告
            //详情
            madapter.update(listnews);
            madapter.notifyDataSetChanged();
            //结果
    }

    public  void getData(){

        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_INSPECTRESULTDETAIL;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                   // jsonQuery.put("resultId", resultid);
                    jsonQuery.put("resultId", "02d1a533ee034f7585e3701d04772d0b");
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
                                    if (dataArray!=null&&dataArray.length()>0) {//
                                        //告知页
                                        //setdata(dataArray);
                                        //检查详情
                                        //检查结果
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
    private void initView(){
        Intent intent=getIntent();
        resultid=intent.getStringExtra("ids");
        TopBean topBean=new TopBean("检查详情","返回","下一步",true,false);
        getTopView(topBean);
        listView=(ListView)findViewById(R.id.checkquestionlist);
        listnews= new ArrayList<Map<String, Object>>();
        madapter = new CheckResultAdspter(this, listnews,0);
        listView.setAdapter(madapter);

        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);



    }
    public void onOperationEvent() {
        final ColorStateList btnblue=tvlNotice.getTextColors();
        final  ColorStateList btnblack=tvNew.getTextColors();
        final Drawable linered=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable lineblack=relNewsListName.getChildAt(1).getBackground();


        view_1layout= (ScrollView)findViewById(R.id.view_1);
        view_2layout= (LinearLayout)findViewById(R.id.view_2);
        view_3layout= (ScrollView)findViewById(R.id.view_3);
        view_1layout.setVisibility(View.VISIBLE);
        view_2layout.setVisibility(View.GONE);
        view_3layout.setVisibility(View.GONE);
        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

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
                view_3layout.setVisibility(View.VISIBLE);
                view_2layout.setVisibility(View.GONE);
                view_1layout.setVisibility(View.GONE);

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

    public List<Map<String, Object>> checkresultlistcontext(int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String title="";
        String time="";
        String content="";



        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            //tv_result3=1  合理    ；2是；3否
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("detail_title", "1、生产环境条件");
                List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
                Map<String, Object> infomap=new HashMap<String, Object>();
                infomap.put("detail_info","1.1 厂区基本无扬尘、基本无积水，基本厂区、基本车间卫生整洁。基本厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
                infomap.put("tv_result3",1);
                infomap.put("detail_remark","整体不错，继续努力");
                infomap.put("detail_image","");
                infolist.add(infomap);

            infomap=new HashMap<String, Object>();
            infomap.put("detail_info","1.2 厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
            infomap.put("tv_result3",2);
            infomap.put("detail_remark","全部合格");
            infomap.put("detail_image","");
            infolist.add(infomap);


           infomap=new HashMap<String, Object>();
            infomap.put("detail_info","1.3 厂区有扬尘、有积水，厂区、车间卫生整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。");
            infomap.put("tv_result3",3);
            infomap.put("detail_remark","很不合格");
            infomap.put("detail_image","");
            infolist.add(infomap);
            map.put("infolist",infolist);
            list.add(map);
        }

        return list;

    }
    //解析执法检查
    public void setdata( JSONArray   dataArray) throws JSONException{
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
                        listnews.add(contextmap);
                    }
                }

            }
        }}
}
