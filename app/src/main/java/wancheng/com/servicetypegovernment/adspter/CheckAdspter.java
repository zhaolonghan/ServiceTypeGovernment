package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckDetailActivity;
import wancheng.com.servicetypegovernment.activity.CheckHistoryListActivity;
import wancheng.com.servicetypegovernment.activity.CheckListActivity;
import wancheng.com.servicetypegovernment.activity.CheckOrderActivity;
import wancheng.com.servicetypegovernment.activity.CheckResultDetailActivity;
import wancheng.com.servicetypegovernment.activity.CompanyCheckListActivity;
import wancheng.com.servicetypegovernment.activity.CompanyDetailActivity;
import wancheng.com.servicetypegovernment.activity.InformActivity;
import wancheng.com.servicetypegovernment.activity.QuestionListActivity;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;

/**
 * Created by john on 2017/8/17.
 */
public class CheckAdspter extends BaseAdapter
{
    private AMapLocationClient locationClient = null;
    private int reMovenum=0;
    public List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private String uid;
    private int typeadapter;//0企业列表  1执法检查 3问题处置
    private Drawable  backstyle;
    private int choose_i=-1;
    private int choose_k=-1;
    private String lng="";
    private  String lat="";
    protected ProgressDialog pd;
    public String[]  gprsArray={"调整位置","导航路线"};
    public Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            //方法区
            if(msg.what==13){
                updataButton();
            }
            if(msg.what==18){
                if(choose_i>=0){
                    CheckOrderActivity.instance.getListDataFirst();
                }
                Toast.makeText(context, "已调整位置！" , Toast.LENGTH_SHORT).show();
                choose_i=-1;
            }
            if(msg.what==19){
                Toast.makeText(context, "调整位置失败！" , Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();

        }


    };
    public void updataButton(){
        if(reMovenum>=0){
            data.remove(reMovenum);
            update(data);
        }
    }

    public CheckAdspter(Context context, List<Map<String, Object>> data,int typeadapter,ProgressDialog pd,Handler handler){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.typeadapter=typeadapter;
    }
    public final class subgroup{
        public TextView id;
        //企业列表
        public TextView corp_name;
        public TextView corp_code;
        public TextView corp_person;
        public TextView corp_tel;
        public TextView corp_address;
        public ImageView corp_telepboneOpen;
        public ImageView corp_gps;
        public TextView   distance;
        //执法检查
        public TextView check_date;
        public TextView check_corpnum;
        public TextView check_status;
        public TextView check_numed;
        public TextView check_numing;
        public TextView check_numthrought;
        public TextView check_numunthrought;
        public TextView check_radioing;
        public TextView check_radiothrought;
        //问题处置
        public TextView question_corpname;
        public TextView question_no;
        public TextView question_date;
        public TextView question_result;
        public TextView question_management;
        public TextView question_status;
        public TextView question_limit;
        public Button bt_history;
        public Button bt_check;
        public Button btDetail;
        public Button btStartCheck ;
        public Button  questionbotton;
        public  Button status1;
        public  Button status2;
    }
    public final class corpsubgroup{
        public TextView id;


    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(data!=null&&data.size()>0){

        subgroup zujian;
        if(convertView==null){
            zujian=new subgroup();
            //获得组件，实例化组件
            int [] items={R.layout.item_checkcorp,R.layout.item_checkenforcement,R.layout.item_checkquestion};
            convertView=layoutInflater.inflate(items[typeadapter], null);
            if(typeadapter==0) {
                initCorpView(zujian,convertView);
            }
            if(typeadapter==1) {
                initCheckView(zujian, convertView);
            }
            if(typeadapter==2) {
                initquestionView(zujian, convertView,i);
            }
            convertView.setTag(zujian);
        }else{
            zujian=(subgroup)convertView.getTag();
        }
        if(typeadapter==0) {
            initCorpData(zujian,i);
        }
        if(typeadapter==1){
            initcheckData(zujian, i);
        }
        if(typeadapter==2){
            initquestionData(zujian, i);
        }

        }
        return convertView;
    }
    public void add(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new ArrayList<>();
        }

        data.addAll(datas);
        //删除的话用remove
        notifyDataSetChanged();
    }
    public void update(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new ArrayList<>();
        }

        data=datas;
        //删除的话用remove
        notifyDataSetChanged();
    }
    public void initCorpView(subgroup zujian, View convertView) {
        zujian.corp_name = (TextView) convertView.findViewById(R.id.corp_name);
        zujian.corp_code = (TextView) convertView.findViewById(R.id.corp_code);
        zujian.corp_person = (TextView) convertView.findViewById(R.id.corp_person);
        zujian.corp_tel = (TextView) convertView.findViewById(R.id.corp_tel);
        zujian.corp_address = (TextView) convertView.findViewById(R.id.corp_address);
        zujian.bt_history = (Button) convertView.findViewById(R.id.bt_history);
        zujian.bt_check = (Button) convertView.findViewById(R.id.bt_check);
        zujian.btDetail = (Button) convertView.findViewById(R.id.bt_detail);
        zujian.btStartCheck = (Button) convertView.findViewById(R.id.bt_check);
        zujian.questionbotton = (Button) convertView.findViewById(R.id.questionbotton);
        zujian.corp_telepboneOpen=(ImageView)convertView.findViewById(R.id.telephoneOpen);
        zujian.corp_gps=(ImageView)convertView.findViewById(R.id.gps);
        zujian.distance = (TextView) convertView.findViewById(R.id.distance);




    }
    public void initCheckView(subgroup zujian, View convertView) {
        zujian.check_date = (TextView) convertView.findViewById(R.id.check_date);
        zujian.check_corpnum = (TextView) convertView.findViewById(R.id.check_corpnum);
        zujian.check_status = (TextView) convertView.findViewById(R.id.check_status);
        zujian.check_numed = (TextView) convertView.findViewById(R.id.check_numed);
        zujian.check_numing = (TextView) convertView.findViewById(R.id.check_numing);
        zujian.check_numthrought = (TextView) convertView.findViewById(R.id.check_numthrought);
        zujian.check_numunthrought = (TextView) convertView.findViewById(R.id.check_numunthrought);
        zujian.check_radioing = (TextView) convertView.findViewById(R.id.check_radioing);
        zujian.check_radiothrought = (TextView) convertView.findViewById(R.id.check_radiothrought);
        zujian.btStartCheck = (Button) convertView.findViewById(R.id.bt_start_check);



    }
    public void initquestionView(subgroup zujian, View convertView ,final int j) {
        //zujian.id=(TextView)convertView.findViewById(R.id.newsid);
        zujian.question_corpname = (TextView) convertView.findViewById(R.id.question_corpname);
        zujian.question_no = (TextView) convertView.findViewById(R.id.question_no);
        zujian.question_date = (TextView) convertView.findViewById(R.id.question_date);
        zujian.question_result = (TextView) convertView.findViewById(R.id.question_result);
        zujian.question_management = (TextView) convertView.findViewById(R.id.question_management);
        zujian.question_status = (TextView) convertView.findViewById(R.id.question_status);
        zujian.question_limit = (TextView) convertView.findViewById(R.id.question_limit);
        zujian.status1 = (Button) convertView.findViewById(R.id.status1);
        zujian.status2 = (Button) convertView.findViewById(R.id.status2);
        zujian.btStartCheck = (Button) convertView.findViewById(R.id.bt_check_detail);

        if(data.get(j).get("question_status")!=null){
            id=  data.get(j).get("id").toString();
            uid=  data.get(j).get("uid").toString();
            if("未整改".equals(data.get(j).get("question_status").toString())){
                final Button btn=zujian.status1;
                btn.setBackground(context.getResources().getDrawable(R.drawable.check_btn_style));
                zujian.status1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        reMovenum=j;
                        showNormalDialog("提示", "您将处理当前信息，是否确认？");


                    }
                });

            }
            if("未处罚".equals(data.get(j).get("question_status").toString())){
                 zujian.status2.setBackground(context.getResources().getDrawable(R.drawable.check_btn_style));
                final Button button2=zujian.status2;
                zujian.status2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        reMovenum=j;
                        getData(uid, id);
                    }
                });
            }
        }


    }
    public void initCorpData(subgroup zujian,int i) {
        // corpsubgroup.id=(TextView)convertView.findViewById(R.id.newsid);
        if(typeadapter==0){
            if(data!=null&&data.size()> 0 &&data.get(i)!=null&&data.get(i).get("id")!=null){

                final String corp_name=data.get(i).get("corp_name").toString();
                final String corp_address=data.get(i).get("corp_address").toString();
                zujian.corp_name.setText(corp_name);
                zujian.corp_code.setText(data.get(i).get("corp_code").toString());
                zujian.corp_person.setText(data.get(i).get("corp_person").toString());
                zujian.corp_tel.setText(data.get(i).get("corp_tel").toString());
                zujian.corp_address.setText(corp_address);
                zujian.distance.setText(data.get(i).get("distance").toString());
                final String corpId =data.get(i).get("id").toString();
                final String phone =data.get(i).get("corp_tel").toString().trim().replaceAll(" ", "");
                //data.get(i).put("zujian",zujian);
                data.get(i).put("tv_distance",zujian.distance);

                String ztlx2 ="";
                if(data.get(i).get("ztlx")!=null){
                    ztlx2=data.get(i).get("ztlx").toString();
                }
                final String ztlx = ztlx2;
                final ArrayList<Map<String,Object>> dataTypeArray=(ArrayList<Map<String,Object>>)data.get(i).get("corpTypeList");
                String str="";
                if(dataTypeArray!=null&&dataTypeArray.size()>1){
                    for(Map<String,Object>map:dataTypeArray){
                         str+=","+map.get("tableName").toString();
                    }
                }

                //电话
                if(phone.length()>0){

                    zujian.corp_telepboneOpen.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                    zujian.corp_telepboneOpen.setVisibility(View.VISIBLE);

                }else{
                    zujian.corp_telepboneOpen.setVisibility(View.GONE);

                }
                //定位
                final String corpIdgps=data.get(i).get("id").toString();
                choose_k=i;
                choose_i=i;
                zujian.corp_gps.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        //初始化client
                        locationClient = new AMapLocationClient(context);
                        //设置定位参数
                        locationClient.setLocationOption(getDefaultOption());
                        // 设置定位监听
                        locationClient.setLocationListener(new AMapLocationListener() {
                            @Override
                            public void onLocationChanged(AMapLocation loc) {

                                if (null != loc) {
                                    if (loc.getErrorCode() == 0) {
                                            final String lng=loc.getLongitude()+"";
                                            final String lat=loc.getLatitude()+"";
                                        if(data.get(choose_k).get("lng")==null||data.get(choose_k).get("lng").toString().length()==0) {
                                            gpsupdate(corpIdgps,loc.getLongitude()+"",loc.getLatitude()+"");

                                        } else{
                                            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("选择")
                                                    .setSingleChoiceItems(gprsArray, -1, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if(which==0){
                                                                showNormalDialogGprs(corpIdgps,lng,lat);
                                                            }else{
                                                        /* Toast.makeText(context, strArray[which], Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent();
                                                        intent.putExtra("corpId",corpId);
                                                        intent.putExtra("specialId",specialId);
                                                        intent.putExtra("corp_name",corp_name);
                                                        intent.putExtra("corp_address",corp_address);
                                                        intent.putExtra("resultId",resultId);
                                                        intent.putExtra("tzsbId",tzsbId);
                                                        intent.putExtra("ztlx",dataTypeArray.get(which).get("ztlx2").toString());
                                                        intent.setClass(context, InformActivity.class);
                                                        context.startActivity(intent);*/
                                                                Intent    intent=null;
                                                                if (isAvilible(context, "com.autonavi.minimap")) {
                                                                    try{
                                                                        intent = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://navi?lat="+data.get(choose_k).get("lat")+ "&lon="+ data.get(choose_k).get("lng")+ "&dev=0"));
                                                                        context.startActivity(intent);
                                                                    } catch (Exception e)
                                                                    {e.printStackTrace(); }
                                                                }else{
                                                                    Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
                                                                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                                                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                    context.startActivity(intent);
                                                                }
                                                            }
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        }

                                    } else {
                                        Toast.makeText(context, " 定位失败" , Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, " 定位失败" , Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        locationClient.startLocation();
                    }
                });

                final String str1=str;
                zujian.bt_history.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Toast.makeText(context, " 跳转历史检查列表页面", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent();
                        intent.putExtra("corpId",corpId);
                        intent.putExtra("ztlx",ztlx);
                        intent.setClass(context, CheckHistoryListActivity.class);
                        context.startActivity(intent);
                    }
                });

                zujian.bt_check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.putExtra("corpId",corpId);
                        intent.putExtra("ztlx",ztlx);
                        intent.setClass(context, InformActivity.class);
                        context.startActivity(intent);
                    }
                });
                zujian.btDetail.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.putExtra("corpId",corpId);
                        intent.putExtra("ztlx",ztlx);
                        intent.setClass(context, CompanyDetailActivity.class);
                        context.startActivity(intent);
                    }
                });
                zujian.questionbotton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.putExtra("corpName",corp_name);
                        intent.putExtra("ztlx",ztlx);
                        intent.setClass(context, QuestionListActivity.class);
                        context.startActivity(intent);
                    }
                });
                final String specialId=data.get(i).get("specialId")!=null?data.get(i).get("specialId").toString():"";
                final String resultId=data.get(i).get("resultId")!=null?data.get(i).get("resultId").toString():"";
                final String tzsbId=data.get(i).get("tzsbId")!=null?data.get(i).get("tzsbId").toString():"";
                int notInspectNum=Integer.parseInt(data.get(i).get("notInspectNum")!=null?data.get(i).get("notInspectNum").toString():"0");
                if(notInspectNum>0){
                    zujian.btStartCheck.setText("开始检查("+notInspectNum+")");
                }else{
                    zujian.btStartCheck.setText("开始检查");
                }
                zujian.btStartCheck.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        data.get(choose_i).put("choose_i",choose_i) ;
                        //判断文字
                        if(dataTypeArray!=null&&dataTypeArray.size()>0){
                            if(dataTypeArray.size()==1){
                                Intent intent = new Intent();
                                intent.putExtra("corpId",corpId);
                                intent.putExtra("specialId",specialId);
                                intent.putExtra("corp_name",corp_name);
                                intent.putExtra("corp_address",corp_address);
                                intent.putExtra("ztlx",dataTypeArray.get(0).get("ztlx2").toString());
                                intent.putExtra("resultId",resultId);
                                intent.putExtra("tzsbId",tzsbId);
                                intent.setClass(context, InformActivity.class);
                                context.startActivity(intent);
                            }else{
                                    final String[]  strArray=str1.substring(1).split(",");
                                    AlertDialog dialog = new AlertDialog.Builder(context).setTitle("选择检查表")
                                            .setSingleChoiceItems(strArray, -1, new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(context, strArray[which], Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent();
                                                    intent.putExtra("corpId",corpId);
                                                    intent.putExtra("specialId",specialId);
                                                    intent.putExtra("corp_name",corp_name);
                                                    intent.putExtra("corp_address",corp_address);
                                                    intent.putExtra("resultId",resultId);
                                                    intent.putExtra("tzsbId",tzsbId);
                                                    intent.putExtra("ztlx",dataTypeArray.get(which).get("ztlx2").toString());
                                                    intent.setClass(context, InformActivity.class);
                                                    context.startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();
                            }
                        }else{
                            Toast.makeText(context, "该企业没有主体类型，暂时无法检查！", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }else{
                zujian.corp_name.setText(null);
                zujian.corp_code.setText(null);
                zujian.corp_person.setText(null);
                zujian.corp_tel.setText(null);
                zujian.corp_address.setText(null);
            }




        }


    }



    public void  initcheckData (subgroup zujian,int i) {
        if(data!=null&&data.size()> 0 &&data.get(i)!=null){

            zujian.check_date.setText(data.get(i).get("check_date").toString());
            zujian.check_corpnum.setText(data.get(i).get("check_corpnum").toString());
            zujian.check_status.setText(data.get(i).get("check_status").toString());
            zujian.check_numed.setText(data.get(i).get("check_numed").toString());
            zujian.check_numing.setText(data.get(i).get("check_numing").toString());
            zujian.check_numthrought.setText(data.get(i).get("check_numthrought").toString());
            zujian.check_numunthrought.setText(data.get(i).get("check_numunthrought").toString());
            zujian.check_radioing.setText(data.get(i).get("check_radioing").toString());
            zujian.check_radiothrought.setText(data.get(i).get("check_radiothrought").toString());
           final String companyType=data.get(i).get("companyType").toString();
            final String specialId=data.get(i).get("specialId").toString();
            zujian.btStartCheck.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("companyType", companyType);
                    intent.putExtra("specialId", specialId);
                    intent.setClass(context, CheckListActivity.class);
                    context.startActivity(intent);
                }
            });
        }else {
            zujian.check_date.setText(null);
            zujian.check_corpnum.setText(null);
            zujian.check_status.setText(null);
            zujian.check_numed.setText(null);
            zujian.check_numing.setText(null);
            zujian.check_numthrought.setText(null);
            zujian.check_numunthrought.setText(null);
            zujian.check_radioing.setText(null);
            zujian.check_radiothrought.setText(null);
        }

    }
    public AMapLocationClientOption getDefaultOption(){
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

    public void initquestionData(subgroup zujian,int i) {
        final  int j=i;
        if(data!=null&&data.size()> 0 &&data.get(i)!=null) {

           id=  data.get(i).get("id").toString();
           uid=  data.get(i).get("uid").toString();
            zujian.question_corpname.setText(data.get(i).get("question_corpname").toString());
            zujian.question_no.setText(data.get(i).get("question_no").toString());
            zujian.question_date.setText(data.get(i).get("question_date").toString());
            zujian.question_result.setText(data.get(i).get("question_result").toString());
            zujian.question_management.setText(data.get(i).get("question_management").toString());
            zujian.question_status.setText(data.get(i).get("question_status").toString());
            zujian.question_limit.setText(data.get(i).get("question_limit").toString());
            zujian.btStartCheck.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("ids", id);
                    intent.setClass(context, CheckResultDetailActivity.class);
                    context.startActivity(intent);
                }
            });



        }else{
            zujian.question_corpname.setText(null);
            zujian.question_no.setText(null);
            zujian.question_date.setText(null);
            zujian.question_result.setText(null);
            zujian.question_management.setText(null);
            zujian.question_status.setText(null);
            zujian.question_limit.setText(null);
        }


    }
    private void getData(final String uid,final String resultId) {
        pd = ProgressDialog.show(context, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{

                    jsonQuery.put("uid",uid);
                    jsonQuery.put("resultId", resultId);
                    String data=  jsonQuery.toString();
                    data= Base64Coder.encodeString(data);
                    map.put("data", data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_UPDATERESULT, map);
                //Log.e("res",res);
                if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                    handler.sendEmptyMessage(4);
                } else {
                    Message msg = new Message();
                    msg.what = 15;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = JSONUtils.getString(jsonObj,"msg","") ;
                            String code = JSONUtils.getString(jsonObj, "code", "1") ;
                            if ("0".equals(code)) {
                                //String  data=jsonObj.getString("data");
                               // data =new String(Base64Coder.decodeString(data));

                                msg.what = 13;
                                msg.obj = "操作成功";
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = "操作异常";
                                else
                                    msg.obj = "请求异常，请稍后重试！";

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            msg.obj = "请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }

                }
            }

            ;
        }.start();

    }
    protected void showNormalDialog(String title,String context2){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(context2);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getData(uid, id);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
    public void gpsupdate(final String corpId,final String lng,final  String lat ){
        pd = ProgressDialog.show(context, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                try{
                    JSONObject jsonQuery = new JSONObject();
                    jsonQuery.put("corpId", corpId);
                    jsonQuery.put("lng", lng);
                    jsonQuery.put("lat", lat);
                    map.put("data", Base64Coder.encodeString(jsonQuery.toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.sendPost(ConstUtil.METHOD_CORPGPRS, map);
                if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                    handler.sendEmptyMessage(4);
                } else {
                    Message msg = new Message();
                    msg.what = 15;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = jsonObj.optString("msg");
                            String code = jsonObj.optString("code");
                            if ("0".equals(code)) {
                                msg.what = 18;
                                msg.obj = msg_code;
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = msg_code;
                                else
                                    msg.obj = "请求异常，请稍后重试！";
                                msg.what = 19;

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            msg.obj = "请求异常，请稍后重试！";
                            msg.what = 19;
                        }
                        handler.sendMessage(msg);
                    }

                }
            }

            ;
        }.start();

    }
    protected void showNormalDialogGprs(final String corpIdgps,final  String lng,final String lat){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("当前企业已定位，是否当前修改企业定位信息？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gpsupdate(corpIdgps,lng,lat);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
    /* 检查手机上是否安装了指定的软件
    * @param context
    * @param packageName：应用包名
    * @return
            */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
