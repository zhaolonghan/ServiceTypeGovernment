package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckHistoryAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckQuestionAdspter;
import wancheng.com.servicetypegovernment.adspter.CheckResultAdspter;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
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
    private     Map<String, Object> gzyMap=new Hashtable<String, Object>();
    private   LayoutInflater layoutInflater;

   // METHOD_INSPECTRESULTDETAIL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_check_result_detail);
        initView();
        onOperationEvent();
        getData();


    }
    @Override
    public void updateView() {
            //公告
        gzy.corpName.setText(gzyMap.get("corpName").toString());
        gzy.address.setText(gzyMap.get("address").toString());
        gzy.gzyZhifaBy1.setText(gzyMap.get("gzyZhifaBy1").toString());
        gzy.gzyZhifaBy2.setText(gzyMap.get("gzyZhifaBy2").toString());
        gzy.gzyInspectTime.setText(gzyMap.get("gzyInspectTime").toString());
        gzy.gzyGzsx.setText(gzyMap.get("gzyGzsx").toString());
        gzy.gzySfhb.setText(gzyMap.get("gzySfhb").toString());
        /// gzy.gzyBjcdwqz=(TextView)this.findViewById(R.id.gzyBjcdwqz);
        gzy.gzyBjcdwqzTime.setText(gzyMap.get("gzyBjcdwqzTime").toString());
        //  gzy.gzyJcdwqz=(TextView)this.findViewById(R.id.tv_gaozhi);
        gzy.gzyJcdwqzTime.setText(gzyMap.get("gzyJcdwqzTime").toString());
        //gzyMap.put("gzyBjcdwqz",imageUrls);
        if(gzyMap.get("gzyBjcdwqz")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)gzyMap.get("gzyBjcdwqz");
            getImageGridViews(imageUrls,R.id.iv_addsign);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.iv_addsign);
            imageView.setVisibility(View.GONE);
        }
        if(gzyMap.get("gzyJcdwqz")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)gzyMap.get("gzyJcdwqz");
            getImageGridViews(imageUrls,R.id.iv_addsign2);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.iv_addsign2);
            imageView.setVisibility(View.GONE);
        }
        //结果


        resultinfo.result_corpName.setText(gzyMap.get("result_corpName").toString());
        resultinfo.result_fuzeren.setText(gzyMap.get("result_fuzeren").toString());
        resultinfo.result_tel.setText(gzyMap.get("result_tel").toString());
        resultinfo.result_permitCode.setText(gzyMap.get("result_permitCode").toString());
        resultinfo.result_inspectTimes.setText(gzyMap.get("result_inspectTimes").toString());
        resultinfo.result_inspectContent.setText(gzyMap.get("result_inspectContent").toString());
        resultinfo.result_inspectResult.setText(gzyMap.get("result_inspectResult").toString());
        resultinfo.result_result.setText(gzyMap.get("result_result").toString());
        resultinfo.result_remarks.setText(gzyMap.get("result_remarks").toString());
        resultinfo.result_zfryqzTime.setText(gzyMap.get("result_zfryqzTime").toString());

        resultinfo.result_inspectUnitOpinions.setText(gzyMap.get("result_inspectUnitOpinions").toString());
        resultinfo.result_frhfzrqzTime.setText(gzyMap.get("result_frhfzrqzTime").toString());;
        if(gzyMap.get("result_zfryqz")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)gzyMap.get("result_zfryqz");
            getImageGridViews(imageUrls,R.id.result_zfryqz);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.iv_addsign);
            imageView.setVisibility(View.GONE);
        }
        if(gzyMap.get("result_frhfzrqz")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)gzyMap.get("result_frhfzrqz");
            getImageGridViews(imageUrls,R.id.result_frhfzrqz);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.result_frhfzrqz);
            imageView.setVisibility(View.GONE);
        }
        //详情

            madapter.update(listnews);
            madapter.notifyDataSetChanged();
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
                                    JSONObject dataobject=new JSONObject(data);
                                    if (dataobject!=null) {
                                        //告知页
                                        getGzy(dataobject);
                                        //setdata(dataArray);
                                        //检查详情
                                        //检查结果
                                        getResult(dataobject);
                                       JSONArray jsonArray=  dataobject.getJSONArray("inspectDetail");
                                        if(jsonArray!=null&&jsonArray.length()>0){
                                            inspectDetail(jsonArray);
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
    private void initView(){
        Intent intent=getIntent();
        resultid=intent.getStringExtra("ids");
        TopBean topBean=new TopBean("检查详情","返回","下一步",true,false);
        getTopView(topBean);
        listView=(ListView)findViewById(R.id.checkquestionlist);
       listnews= new ArrayList<Map<String, Object>>();
        //listnews= checkresultlistcontext(5);
        madapter = new CheckResultAdspter(this, listnews,0);
        listView.setAdapter(madapter);

        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);


        gzy.corpName=(TextView)this.findViewById(R.id.corpName);
        gzy.address=(TextView)this.findViewById(R.id.address);
        gzy.gzyZhifaBy1=(TextView)this.findViewById(R.id.gzyZhifaBy1);
        gzy.gzyZhifaBy2=(TextView)this.findViewById(R.id.gzyZhifaBy2);
        gzy.gzyInspectTime=(TextView)this.findViewById(R.id.gzyInspectTime);
        gzy.gzyGzsx=(TextView)this.findViewById(R.id.tv_gaozhi);
        gzy.gzySfhb=(TextView)this.findViewById(R.id.gzySfhb);
       /// gzy.gzyBjcdwqz=(TextView)this.findViewById(R.id.gzyBjcdwqz);
        gzy.gzyBjcdwqzTime=(TextView)this.findViewById(R.id.gzyBjcdwqzTime);
      //  gzy.gzyJcdwqz=(TextView)this.findViewById(R.id.tv_gaozhi);
        gzy.gzyJcdwqzTime=(TextView)this.findViewById(R.id.gzyJcdwqzTime);


        resultinfo.result_corpName=(TextView)this.findViewById(R.id.result_corpName);
        resultinfo.result_fuzeren=(TextView)this.findViewById(R.id.result_fuzeren);
        resultinfo.result_tel=(TextView)this.findViewById(R.id.result_tel);
        resultinfo.result_permitCode=(TextView)this.findViewById(R.id.result_permitCode);
        resultinfo.result_inspectTimes=(TextView)this.findViewById(R.id.result_inspectTimes);
        resultinfo.result_inspectContent=(TextView)this.findViewById(R.id.result_inspectContent);
        resultinfo.result_inspectResult=(TextView)this.findViewById(R.id.result_inspectResult);
        resultinfo.result_result=(TextView)this.findViewById(R.id.result_result);
        resultinfo.result_remarks=(TextView)this.findViewById(R.id.result_remarks);
        resultinfo.result_inspectUnitOpinions=(TextView)this.findViewById(R.id.result_inspectUnitOpinions);
        resultinfo.result_zfryqzTime=(TextView)this.findViewById(R.id.result_zfryqzTime);
        resultinfo.result_frhfzrqzTime=(TextView)this.findViewById(R.id.result_frhfzrqzTime);


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
            //tv_resul 2合理    ；1是；0否
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("detail_title", "1、生产环境条件");
                List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
                Map<String, Object> infomap=new HashMap<String, Object>();
                infomap.put("detail_info","合格1.1 厂区基本无扬尘、基本无积水，基本厂区、基本车间卫生整洁。基本厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
                infomap.put("tv_result3",2);
                infomap.put("detail_remark","整体不错，继续努力");
                infomap.put("detail_image","");
                infolist.add(infomap);

            infomap=new HashMap<String, Object>();
            infomap.put("detail_info","是1.2 厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。厂区无扬尘、无积水，厂区、车间卫生整洁。");
            infomap.put("tv_result3",1);
            infomap.put("detail_remark","全部合格");
            infomap.put("detail_image","");
            infolist.add(infomap);


           infomap=new HashMap<String, Object>();
            infomap.put("detail_info","否1.3 厂区有扬尘、有积水，厂区、车间卫生整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。厂区有扬尘、有积水，厂区、车间卫生不整洁。");
            infomap.put("tv_result3",0);
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
    private void getImageGridViews(final ArrayList<ImagesBean> imageUrls,int id){
        for(int k=0;k<imageUrls.size();k++){
            final int index=k;
            final ImageView imageView=(ImageView)this.findViewById(id);
            ImagesBean image=imageUrls.get(k);
            if (image.getType().equals("netImage")){
                DisplayImageOptions options = new DisplayImageOptions.Builder()//
                        .cacheInMemory(true)//
                        .cacheOnDisk(true)//
                        .bitmapConfig(Bitmap.Config.RGB_565)//
                        .build();
                ImageLoader.getInstance().displayImage(image.getPath(), imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        imageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
                    newList.addAll(imageUrls);
                    if (imageUrls.get(imageUrls.size() - 1).getType().equals("defaultImage")) {
                        newList.remove(newList.size() - 1);
                    }
                    imageBrower(index, newList);


                }
            });
            imageView.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }
    private void getGzy(JSONObject dataobject) {
        try {
            gzyMap.put("corpName",dataobject.getString("corpName"));//被检查单位
            gzyMap.put("address",dataobject.getString("address"));//地址
            gzyMap.put("gzyZhifaBy1",dataobject.getString("gzyZhifaBy1"));//检查人员1
            gzyMap.put("gzyZhifaBy2",dataobject.getString("gzyZhifaBy2"));
            gzyMap.put("gzyInspectTime",DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(dataobject.getString("gzyInspectTime")))));//检查时间
            gzyMap.put("gzyGzsx", Html.fromHtml(dataobject.getString("gzyGzsx")));//告知事项
            gzyMap.put("gzySfhb","否");//
            if ("1".equals(dataobject.getString("gzySfhb"))){
                gzyMap.put("gzySfhb","是");//规避.
            }
            gzyMap.put("gzyBjcdwqzTime",DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(dataobject.getString("gzyBjcdwqzTime")))));
            gzyMap.put("gzyJcdwqzTime", DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(dataobject.getString("gzyJcdwqzTime")))));
            ArrayList<ImagesBean> imageUrls;
            if(null!=dataobject.getString("gzyJcdwqz")&&!"".equals(dataobject.getString("gzyJcdwqz"))){
                imageUrls=new ArrayList<ImagesBean>()  ;
                ImagesBean bean=new ImagesBean();
                bean.setPath(dataobject.getString("gzyJcdwqz"));
                bean.setType("netImage");
                imageUrls.add(bean);
                gzyMap.put("gzyJcdwqz",imageUrls);
            }
            if(null!=dataobject.getString("gzyBjcdwqz")&&!"".equals(dataobject.getString("gzyBjcdwqz"))){
                imageUrls=new ArrayList<ImagesBean>()  ;
                ImagesBean bean=new ImagesBean();
                bean.setPath(dataobject.getString("gzyBjcdwqz"));
                // bean.setPath("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
                bean.setType("netImage");
                imageUrls.add(bean);
                gzyMap.put("gzyBjcdwqz",imageUrls);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

        //结果页面
    private void getResult(JSONObject dataobject) {
        try {
            gzyMap.put("result_corpName",dataobject.get("corpName")!=null?dataobject.getString("corpName"):"");//被检查单位
            gzyMap.put("result_address",dataobject.get("address")!=null?dataobject.getString("address"):"");//地址
            gzyMap.put("result_fuzeren", dataobject.get("fuzeren") != null ? dataobject.getString("fuzeren") : "");//联系人
           // gzyMap.put("result_tel","假的，早点更新新的");//电话
            gzyMap.put("result_tel",!dataobject.isNull("fuzerenTel")?dataobject.getString("fuzerenTel"):"");//电话
            gzyMap.put("result_permitCode",dataobject.get("permitCode")!=null?dataobject.getString("permitCode"):"");//许可证
            gzyMap.put("result_inspectTimes",dataobject.get("inspectTimes")!=null?dataobject.getString("inspectTimes"):"");//检查次数
            gzyMap.put("result_inspectContent", dataobject.get("inspectContent")!=null?Html.fromHtml(dataobject.getString("inspectContent")):"");//检查内容
            gzyMap.put("result_inspectResult","通过");//0是 1否 2符合
            if("2".equals(dataobject.getString("inspectResult"))){
                gzyMap.put("result_inspectResult","整改");//0是 1否 2符合
            }else if("3".equals(dataobject.getString("result"))){
                gzyMap.put("result_inspectResult","停止");//0是 1否 2符合
            }

            gzyMap.put("result_result","符合");
            if("2".equals(dataobject.getString("result"))){
                gzyMap.put("result_result","基本符合");
            }else if("3".equals(dataobject.getString("result"))){
                gzyMap.put("result_result","不符合");
            }
            gzyMap.put("result_remarks", "及时更改数据，当前假数据");
            //gzyMap.put("result_remarks",!dataobject.isNull("fuzerenTel")?dataobject.getString("remarks"):"");//说明

            gzyMap.put("result_zfryqzTime",DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(dataobject.getString("zfryqzTime")))).toString());//检查时间
            gzyMap.put("result_frhfzrqzTime",DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(dataobject.getString("frhfzrqzTime")))).toString());

           // gzyMap.put("result_inspectUnitOpinions",dataobject.getString("inspectUnitOpinions"));//检查次数
            gzyMap.put("result_inspectUnitOpinions","假的，不是真数据");
            ArrayList<ImagesBean> imageUrls;
            if(null!=dataobject.getString("zfryqz")&&!"".equals(dataobject.getString("zfryqz"))){
                imageUrls=new ArrayList<ImagesBean>()  ;
                ImagesBean bean=new ImagesBean();
                bean.setPath(dataobject.getString("zfryqz"));
                bean.setType("netImage");
                imageUrls.add(bean);
                gzyMap.put("result_zfryqz",imageUrls);
            }
            if(null!=dataobject.getString("frhfzrqz")&&!"".equals(dataobject.getString("frhfzrqz"))){
                imageUrls=new ArrayList<ImagesBean>()  ;
                ImagesBean bean=new ImagesBean();
                bean.setPath(dataobject.getString("frhfzrqz"));
                // bean.setPath("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
                bean.setType("netImage");
                imageUrls.add(bean);
                gzyMap.put("result_frhfzrqz",imageUrls);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //检查详情数据
    private void inspectDetail(JSONArray jsonArray) {
        try {
            for(int i=0;i<jsonArray.length();i++){
                Map<String, Object> map=new HashMap<String, Object>();
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                map.put("detail_title", jsonObject.getString("no")+"、"+jsonObject.getString("name"));
                map.put("detail_remarks", jsonObject.getString("remarks"));
                JSONArray array=jsonObject.getJSONArray("detail");
                if(array!=null&&array.length()>0){
                    List<Map<String, Object>>  infolist=new ArrayList<Map<String, Object>>();
                    for(int j=0;j<array.length();j++){
                        //第二层
                        JSONObject twojson= array.getJSONObject(j);
                        if(twojson!=null){
                            Map<String, Object> infomap=new HashMap<String, Object>();
                            infomap.put("detail_info", (i + 1) + "." + (j + 1) + "、" + twojson.getString("content"));
                            infomap.put("tv_result3",twojson.getString("result"));
                            infomap.put("isPoint",twojson.getString("isPoint"));
                            infomap.put("remarks", twojson.getString("remarks"));


                            JSONArray threearray= twojson.getJSONArray("image");

                            ArrayList<ImagesBean> imageUrls=new ArrayList<ImagesBean>();
                            for(int k=0;k<threearray.length();k++){
                                String path="http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg";
                                ImagesBean imagesBean=new ImagesBean();
                                imagesBean.setType("netImage");
                                imagesBean.setPath(threearray.getString(k));
                                imagesBean.setPath(path);
                                imageUrls.add(imagesBean);
                            }
                            infomap.put("detail_image", imageUrls);
                            infolist.add(infomap);
                        }
                    }
                    map.put("infolist", infolist);
                }

                listnews.add(map);




            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private  Gzy gzy=new Gzy();//告知
    private  class Gzy{
        TextView corpName;
        TextView address;
        TextView gzyZhifaBy1;
        TextView gzyZhifaBy2;
        TextView gzyInspectTime;
        TextView gzyGzsx;
        TextView gzySfhb;
        TextView gzyBjcdwqz;
        TextView gzyBjcdwqzTime;
        TextView gzyJcdwqz;
        TextView gzyJcdwqzTime;

    }
    private  Resultinfo resultinfo=new Resultinfo();//检查结果
    private  class Resultinfo{
        TextView result_corpName;
        TextView result_fuzeren;
        TextView result_tel;
        TextView result_permitCode;
        TextView result_inspectTimes;
        TextView result_inspectContent;
        TextView result_inspectResult;
        TextView result_result;
        TextView result_remarks;
        TextView result_zfryqzTime;
        TextView result_inspectUnitOpinions;
        TextView result_frhfzrqzTime;
    }
}
