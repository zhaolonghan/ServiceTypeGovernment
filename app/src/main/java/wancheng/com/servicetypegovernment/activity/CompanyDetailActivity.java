package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
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
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckAdspter;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

public class CompanyDetailActivity extends BaseActivity {
    private LinearLayout view_1layout;//企业
    private LinearLayout view_2layout;//检查
    private LinearLayout view_3layout;//问题
    private RelativeLayout relNewsListName;//
    private RelativeLayout relNoticeListName;//
    private RelativeLayout relLawListName;//
    private TextView tvNew;
    private TextView tvlNotice;
    private TextView tvlLaw;
    private  String corpId;
    private  Map<String ,Object> mapinfo=new HashMap<String ,Object>();
     LinearLayout view_1layoute;
     LinearLayout view_2layoute;
     LinearLayout view_3layoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        initView();
        onOperationEvent();
        getData();

    }
    @Override
    public void updateView() {
        corpinfo.name.setText(mapinfo.get("name").toString());
        corpinfo.code.setText(mapinfo.get("code").toString());
        corpinfo.legal.setText(mapinfo.get("legal").toString());
        corpinfo.legalTel.setText(mapinfo.get("legalTel").toString());
        corpinfo.fuzeren .setText(mapinfo.get("fuzeren").toString());
        corpinfo.fuzerenTel.setText(mapinfo.get("fuzerenTel").toString());
        corpinfo.status.setText(mapinfo.get("status").toString());
        corpinfo.jjxz.setText(mapinfo.get("jjxz").toString());
        corpinfo.jyfs.setText(mapinfo.get("jyfs").toString());
        corpinfo.vocation_category_id.setText(mapinfo.get("vocation_category_id").toString());
        corpinfo.register_capital.setText(mapinfo.get("register_capital").toString());
        corpinfo.ztlxName3.setText(mapinfo.get("ztlxName3").toString());
        corpinfo. jymj.setText(mapinfo.get("jymj").toString());
        corpinfo.cyrysl.setText(mapinfo.get("cyrysl").toString());
        corpinfo.jydz.setText(mapinfo.get("jydz").toString());
        corpinfo.zssfqq.setText(mapinfo.get("zssfqq").toString());
        corpinfo.ydjy.setText(mapinfo.get("ydjy").toString());
        corpinfo.is_important_service.setText(mapinfo.get("is_important_service").toString());
        corpinfo.is_important_inspect.setText(mapinfo.get("is_important_inspect").toString());
        corpinfo.is_current_districtpay_tax.setText(mapinfo.get("is_current_districtpay_tax").toString());
        corpinfo.is_current_year_audit.setText(mapinfo.get("is_current_year_audit").toString());
        corpinfo.sfywljy.setText(mapinfo.get("sfywljy").toString());
        corpinfo.jyfw.setText(mapinfo.get("jyfw").toString());
        corpinfo.zcdz.setText(mapinfo.get("zcdz").toString());


        permit.permit_corpname.setText(mapinfo.get("permit_corpname").toString());
        permit.permit_name.setText(mapinfo.get("permit_name").toString());
        permit.permit_code.setText(mapinfo.get("permit_code").toString());
        permit.permit_beginDate.setText(mapinfo.get("permit_beginDate").toString());
        permit.permit_endTime.setText(mapinfo.get("permit_endTime").toString());
        permit.permit_sendOrg.setText(mapinfo.get("permit_sendOrg").toString());
        permit.permit_scope.setText(mapinfo.get("permit_scope").toString());



        businessLicense.businessLicense_corpname.setText(mapinfo.get("businessLicense_corpname").toString());
        businessLicense.businessLicense_name.setText(mapinfo.get("businessLicense_name").toString());
        businessLicense.businessLicense_code .setText(mapinfo.get("businessLicense_code").toString());
        businessLicense.businessLicense_sendTime.setText(mapinfo.get("businessLicense_sendTime").toString());
        businessLicense.businessLicense_scope .setText(mapinfo.get("businessLicense_scope").toString());
        businessLicense. businessLicense_sendOrg.setText(mapinfo.get("businessLicense_sendOrg").toString());
        businessLicense. businessLicense_beginDate.setText(mapinfo.get("businessLicense_beginDate").toString());
        businessLicense.businessLicense_endTime.setText(mapinfo.get("businessLicense_endTime").toString());


        if(mapinfo.get("permit_images")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)mapinfo.get("permit_images");
            getImageGridViews(imageUrls,R.id.permit_images);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.permit_images);
            imageView.setVisibility(View.GONE);
        }
        if(mapinfo.get("businessLicense_images")!=null){
            ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)mapinfo.get("businessLicense_images");
            getImageGridViews(imageUrls,R.id.businessLicense_images);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.businessLicense_images);
            imageView.setVisibility(View.GONE);
        }
    }
    private void initView(){
        Intent intent=getIntent();
        corpId=intent.getStringExtra("corpId");
        TopBean topBean=new TopBean("企业详情","返回","",true,false);
        getTopView(topBean);
        tvlNotice =(TextView)this.findViewById(R.id.tv_notice);
        tvlLaw=(TextView)this.findViewById(R.id.tv_loyal);
        tvNew=(TextView)this.findViewById(R.id.tv_news);
        relNoticeListName=(RelativeLayout)this.findViewById(R.id.listname_notice);
        relNewsListName=(RelativeLayout)this.findViewById(R.id.listname_news);
        relLawListName=(RelativeLayout)this.findViewById(R.id.listname_law);
        view_1layoute=(LinearLayout)this.findViewById(R.id.view_1);
        view_2layoute=(LinearLayout)this.findViewById(R.id.view_2);
        view_3layoute=(LinearLayout)this.findViewById(R.id.view_3);

        corpinfo.name=(TextView)this.findViewById(R.id.corp_name);
        corpinfo.code =(TextView)this.findViewById(R.id.code);
        corpinfo.legal =(TextView)this.findViewById(R.id.legal);
        corpinfo.legalTel=(TextView)this.findViewById(R.id.legalTel);
        corpinfo.fuzeren =(TextView)this.findViewById(R.id.fuzeren);
        corpinfo.fuzerenTel=(TextView)this.findViewById(R.id.fuzerenTel);
        corpinfo.status=(TextView)this.findViewById(R.id.status);
        corpinfo.jyfs=(TextView)this.findViewById(R.id.jyfs);
        corpinfo.jjxz=(TextView)this.findViewById(R.id.jyxz);
        corpinfo.vocation_category_id=(TextView)this.findViewById(R.id.vocation_category_id);
        corpinfo.register_capital=(TextView)this.findViewById(R.id.register_capital);
        corpinfo.ztlxName3=(TextView)this.findViewById(R.id.ztlxName3);
        corpinfo. jymj=(TextView)this.findViewById(R.id.jymj);
        corpinfo.cyrysl=(TextView)this.findViewById(R.id.cyrysl);
        corpinfo.jydz=(TextView)this.findViewById(R.id.jydz);
        corpinfo.zssfqq=(TextView)this.findViewById(R.id.zssfqq);
        corpinfo.ydjy=(TextView)this.findViewById(R.id.ydjy);
        corpinfo.is_important_service=(TextView)this.findViewById(R.id.is_important_service);
        corpinfo.is_important_inspect=(TextView)this.findViewById(R.id.is_important_inspect);
        corpinfo.is_current_districtpay_tax=(TextView)this.findViewById(R.id.is_current_districtpay_tax);
        corpinfo.is_current_year_audit=(TextView)this.findViewById(R.id.is_current_year_audit);
        corpinfo.sfywljy=(TextView)this.findViewById(R.id.sfywljy);
        corpinfo.jyfw=(TextView)this.findViewById(R.id.jyfw);
        corpinfo.zcdz=(TextView)this.findViewById(R.id.zcdz);



        permit.permit_corpname=(TextView)this.findViewById(R.id.permit_corpname);
        permit.permit_name=(TextView)this.findViewById(R.id.permit_name) ;
        permit.permit_code =(TextView)this.findViewById(R.id.permit_code);
        permit.permit_beginDate=(TextView)this.findViewById(R.id.permit_beginDate);
        permit.permit_endTime =(TextView)this.findViewById(R.id.permit_endTime);
        permit.permit_sendOrg=(TextView)this.findViewById(R.id.permit_sendOrg);
        permit.permit_scope=(TextView)this.findViewById(R.id.permit_scope);



        businessLicense.businessLicense_corpname=(TextView)this.findViewById(R.id.businessLicense_corpname);
        businessLicense.businessLicense_name =(TextView)this.findViewById(R.id.businessLicense_name);
        businessLicense.businessLicense_code =(TextView)this.findViewById(R.id.businessLicense_code);
        businessLicense.businessLicense_sendTime=(TextView)this.findViewById(R.id.businessLicense_sendTime);
        businessLicense.businessLicense_scope =(TextView)this.findViewById(R.id.businessLicense_scope);
        businessLicense. businessLicense_sendOrg=(TextView)this.findViewById(R.id.businessLicense_sendOrg);
        businessLicense. businessLicense_beginDate=(TextView)this.findViewById(R.id.businessLicense_beginDate);
        businessLicense.businessLicense_endTime=(TextView)this.findViewById(R.id.businessLicense_endTime);


    }
    public void onOperationEvent() {

        final ColorStateList btnblack =tvlNotice.getTextColors();
        final  ColorStateList btnblue =tvlLaw.getTextColors();
        final Drawable  lineblack=relNoticeListName.getChildAt(1).getBackground();
        final   Drawable linered =relLawListName.getChildAt(1).getBackground();


        tvNew.setTextColor(btnblack);
        tvlNotice.setTextColor(btnblue);
        tvlLaw.setTextColor(btnblack);

        relNewsListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.getChildAt(1).setBackground(linered);
        relLawListName.getChildAt(1).setBackground(lineblack);
        relNoticeListName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                tvNew.setTextColor(btnblack);
                tvlNotice.setTextColor(btnblue);
                tvlLaw.setTextColor(btnblack);
                //线

                relNewsListName.getChildAt(1).setBackground(lineblack);
                relNoticeListName.getChildAt(1).setBackground(linered);
                relLawListName.getChildAt(1).setBackground(lineblack);

                view_1layoute.setVisibility(View.VISIBLE);
                view_2layoute.setVisibility(View.GONE);
                view_3layoute.setVisibility(View.GONE);


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
                view_1layoute.setVisibility(View.GONE);
                view_2layoute.setVisibility(View.VISIBLE);
                view_3layoute.setVisibility(View.GONE);


            }
        });
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

                view_1layoute.setVisibility(View.GONE);
                view_2layoute.setVisibility(View.GONE);
                view_3layoute.setVisibility(View.VISIBLE);

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
    public  void getData(){

        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                String url= ConstUtil.METHOD_GETCORP;
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("id",corpId);
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
                                   /* JSONArray dataArray=new JSONArray(data);
                                    if (dataArray!=null&&dataArray.length()>0) {
                                       // setcorpdata(dataArray);
                                    }*/
                                    //基本信息
                                    JSONObject corpobject= JSONUtils.getJSONObject(data,"corp",null);
                                    if(corpobject!=null){
                                        mapinfo.put("name", JSONUtils.getString(corpobject, "name", ""));
                                        mapinfo.put("code", JSONUtils.getString(corpobject, "code", ""));
                                        mapinfo.put("legal", JSONUtils.getString(corpobject, "legal", ""));
                                        mapinfo.put("legalTel", JSONUtils.getString(corpobject, "legalTel", ""));
                                        mapinfo.put("fuzeren", JSONUtils.getString(corpobject, "fuzeren", ""));
                                        mapinfo.put("fuzerenTel", JSONUtils.getString(corpobject, "fuzerenTel", ""));
                                        mapinfo.put("status", JSONUtils.getString(corpobject, "status", ""));
                                        mapinfo.put("jjxz", JSONUtils.getString(corpobject, "jyxz", ""));
                                        mapinfo.put("jyfs", JSONUtils.getString(corpobject, "jyfs", ""));
                                        mapinfo.put("vocation_category_id", JSONUtils.getString(corpobject, "vocation_category_id", ""));//主管
                                        mapinfo.put("register_capital", JSONUtils.getString(corpobject, "register_capital", ""));
                                        mapinfo.put("ztlxName3", JSONUtils.getString(corpobject, "ztlxName3", ""));
                                        mapinfo.put("jymj", JSONUtils.getString(corpobject, "jymj", ""));
                                        mapinfo.put("cyrysl", JSONUtils.getString(corpobject, "cyrysl", ""));
                                        mapinfo.put("jydz", JSONUtils.getString(corpobject, "jydz", ""));
                                        mapinfo.put("zssfqq", JSONUtils.getString(corpobject, "zssfqq", ""));
                                        mapinfo.put("ydjy", JSONUtils.getString(corpobject, "ydjy", ""));
                                        mapinfo.put("is_important_service", JSONUtils.getString(corpobject, "is_important_service", ""));
                                        mapinfo.put("is_important_inspect", JSONUtils.getString(corpobject, "is_important_inspect", ""));
                                        mapinfo.put("is_current_districtpay_tax", JSONUtils.getString(corpobject, "is_current_districtpay_tax", ""));
                                        mapinfo.put("is_current_year_audit", JSONUtils.getString(corpobject, "is_current_year_audit", ""));
                                        mapinfo.put("sfywljy", JSONUtils.getString(corpobject, "sfywljy", ""));
                                        mapinfo.put("jyfw", JSONUtils.getString(corpobject, "jyfw", ""));
                                        mapinfo.put("zcdz", JSONUtils.getString(corpobject, "zcdz", ""));

                                    }
                                    //许可证
                                    JSONObject permitobject= JSONUtils.getJSONObject(data,"permit",null);
                                    mapinfo.put("permit_corpname", mapinfo.get("name").toString());
                                    mapinfo.put("permit_name", JSONUtils.getString(corpobject, "name", ""));
                                    mapinfo.put("permit_code", JSONUtils.getString(corpobject, "code", ""));
                                    mapinfo.put("permit_beginDate", JSONUtils.getString(corpobject, "beginDate", "").length()==0?"": DateFormat.format("yyyy年MM月dd日",new Date(Long.parseLong(JSONUtils.getString(corpobject, "beginDate", "0"))) ));
                                    mapinfo.put("permit_endTime", JSONUtils.getString(corpobject, "endTime", "").length()==0?"": DateFormat.format("yyyy年MM月dd日",new Date(Long.parseLong(JSONUtils.getString(corpobject, "endTime", "0"))) ));
                                    mapinfo.put("permit_sendOrg", JSONUtils.getString(corpobject, "sendOrg", ""));
                                    mapinfo.put("permit_scope", JSONUtils.getString(corpobject, "scope", ""));

                                    ArrayList<ImagesBean> imageUrls;

                                    String two= JSONUtils.getString(data, "permit", "");
                                    JSONObject  twonew =new JSONObject(two);
                                    if(JSONUtils.getString(twonew, "images", "").length()>0){
                                        imageUrls=new ArrayList<ImagesBean>()  ;
                                        ImagesBean bean=new ImagesBean();
                                        bean.setPath(JSONUtils.getString(twonew, "images", ""));
                                        bean.setType("netImage");
                                        imageUrls.add(bean);
                                        mapinfo.put("permit_images",imageUrls);
                                    }



                                    //营业
                                    JSONObject businessLicenseobject= JSONUtils.getJSONObject(data,"businessLicense",null);
                                    mapinfo.put("businessLicense_corpname", mapinfo.get("name").toString());
                                    mapinfo.put("businessLicense_name", JSONUtils.getString(corpobject, "name", ""));
                                    mapinfo.put("businessLicense_code", JSONUtils.getString(corpobject, "code", ""));
                                    mapinfo.put("businessLicense_sendTime", JSONUtils.getString(corpobject, "sendTime", "").length()==0?"": DateFormat.format("yyyy年MM月dd日",new Date(Long.parseLong(JSONUtils.getString(corpobject, "sendTime", "0"))) ));
                                    mapinfo.put("businessLicense_scope", JSONUtils.getString(corpobject, "scope", ""));
                                    mapinfo.put("businessLicense_sendOrg", JSONUtils.getString(corpobject, "sendOrg", ""));
                                    mapinfo.put("businessLicense_beginDate", JSONUtils.getString(corpobject, "beginDate", "").length() == 0 ? "" : DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(JSONUtils.getString(corpobject, "beginDate", "0")))));
                                    mapinfo.put("businessLicense_endTime", JSONUtils.getString(corpobject, "endTime", "").length() == 0 ? "" : DateFormat.format("yyyy年MM月dd日", new Date(Long.parseLong(JSONUtils.getString(corpobject, "endTime", "0")))));
                                     two= JSONUtils.getString(data, "businessLicense", "");
                                    twonew =new JSONObject(two);
                                    if(JSONUtils.getString(twonew, "images", "").length()>0){
                                        imageUrls=new ArrayList<ImagesBean>()  ;
                                        ImagesBean bean=new ImagesBean();
                                        bean.setPath(JSONUtils.getString(twonew, "images", ""));
                                        bean.setType("netImage");
                                        imageUrls.add(bean);
                                        mapinfo.put("businessLicense_images",imageUrls);
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

    private  Corpinfo corpinfo=new Corpinfo();
    public   class Corpinfo{
        public TextView name;
        public TextView code ;
        public TextView legal ;
        public TextView legalTel;
        public TextView fuzeren ;
        public TextView fuzerenTel;
        public TextView status;
        public TextView jjxz;
        public TextView jyfs;
        public TextView vocation_category_id;
        public TextView register_capital;
        public TextView ztlxName3;
        public TextView jymj;
        public TextView cyrysl;
        public TextView jydz;
        public TextView zssfqq;
        public TextView ydjy;
        public TextView is_important_service;
        public TextView is_important_inspect;
        public TextView is_current_districtpay_tax;
        public TextView is_current_year_audit;
        public TextView sfywljy;
        public TextView jyfw;
        public TextView zcdz;
    }



    private  Permit permit=new Permit();
    public   class Permit{
        public TextView permit_corpname;
        public TextView permit_name ;
        public TextView permit_code ;
        public TextView permit_beginDate;
        public TextView permit_endTime ;
        public TextView permit_sendOrg;
        public TextView permit_scope;

    }
    private  BusinessLicense businessLicense=new BusinessLicense();
    public   class BusinessLicense{
        public TextView businessLicense_corpname;
        public TextView businessLicense_name ;
        public TextView businessLicense_code ;
        public TextView businessLicense_sendTime;
        public TextView businessLicense_scope ;
        public TextView businessLicense_sendOrg;
        public TextView businessLicense_beginDate;
        public TextView businessLicense_endTime;

    }
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
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }
}
