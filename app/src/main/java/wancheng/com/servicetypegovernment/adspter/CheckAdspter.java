package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckHistoryListActivity;
import wancheng.com.servicetypegovernment.activity.CheckListActivity;
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
    private int reMovenum=0;
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private String uid;
    private int typeadapter;//0企业列表  1执法检查 3问题处置
    private Drawable  backstyle;
    protected ProgressDialog pd;
    public Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            //方法区
            if(msg.what==13){
                updataButton();
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
//        this.pd=pd;
//        this.handler=handler;
    }
    public final class subgroup{
        public TextView id;
        //企业列表
        public TextView corp_name;
        public TextView corp_code;
        public TextView corp_person;
        public TextView corp_tel;
        public TextView corp_address;
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
        //zujian.id=(TextView)convertView.findViewById(R.id.newsid);
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


    }
    public void initCheckView(subgroup zujian, View convertView) {
        //zujian.id=(TextView)convertView.findViewById(R.id.newsid);
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
                final String corpId =data.get(i).get("id").toString();
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
//                if(dataTypeArray==null||dataTypeArray.size()==0){
//
//                    zujian.bt_check.setBackgroundColor(context.getResources().getColor(R.color.darkgrey));
//                }
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

                zujian.btStartCheck.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        if(dataTypeArray!=null&&dataTypeArray.size()>0){
                            if(dataTypeArray.size()==1){
                                Toast.makeText(context, " ", Toast.LENGTH_SHORT).show();
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
}
