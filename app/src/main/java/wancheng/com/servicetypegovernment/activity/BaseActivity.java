package wancheng.com.servicetypegovernment.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;


/**
 * 界面Activity基类
 *
 * @author hanzl
 *
 */
@SuppressLint("HandlerLeak")
public  class BaseActivity extends Activity {

    // 等待提示框
    protected ProgressDialog pd;

    // 信息对话框
    protected Dialog dialog;

    // 错误信息
    protected String errorMsg = "";
    protected  TextView tv_title;
    protected TextView tv_left;
    protected TextView tv_right;
    // protected LinearLayout lin_foot;/** index 代表位置*/
    protected LinearLayout lin_foot1;
    protected LinearLayout lin_foot2;
    protected LinearLayout lin_foot3;
    protected LinearLayout lin_foot4;
    protected LinearLayout lin_foot5;
    protected int oldindexsintent;
    @SuppressLint("ShowToast")
    public Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 刷新列表
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    updateListView();
                    break;
                case 2:
                    // 成功
                    pd.dismiss();
                    showDialog(2);
                    break;
                case 3:
                    // 失败
                    pd.dismiss();
                    showDialog(3);
                    break;
                case 4:
                    // 异常
                    exhand();
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    showDialog(4);
                    break;
                case 5:
                    // 弹出退出对话框
                    showDialog(5);
                    break;
                case 6:
                    // 退出
                    pd.dismiss();
                    finishActivity();
                    break;
                case 10:
                    // session失效提示
                    pd.dismiss();
                    showDialog(10);
                    break;
                case 11:
                    // 处理未捕获异常
                    exhand();
                    pd.dismiss();
                    break;
                case 12:
                    // 只关闭进度条
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    break;
                case 13:
                    // 关闭进度条，Toast弹出传递文字
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    if(msg.obj!=null){
                        String msgtxt = msg.obj.toString();
                        if(msgtxt!=null&&!msgtxt.isEmpty())
                            Toast.makeText(getApplicationContext(), msgtxt,Toast.LENGTH_SHORT).show();
                    }
                    updateView();
                    break;
                case 14:
                    // 刷新页面
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    updateView();
                    break;
                case 15:
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    String msgtxt_obj = msg.obj.toString();
                    if(msgtxt_obj!=null&&!msgtxt_obj.isEmpty())
                        Toast.makeText(getApplicationContext(), msgtxt_obj,Toast.LENGTH_SHORT).show();
                    break;
                case 16:
                    // 关闭进度条，Toast弹出传递文字
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    if(msg.obj!=null){
                        String msgtxt = msg.obj.toString();
                        if(msgtxt!=null&&!msgtxt.isEmpty())
                            Toast.makeText(getApplicationContext(), msgtxt,Toast.LENGTH_SHORT).show();
                    }
                    finishOwn();
                    break;
                case 17:
                    // 关闭进度条，Toast弹出传递文字
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    if(msg.obj!=null){
                        String msgtxt = msg.obj.toString();
                        if(msgtxt!=null&&!msgtxt.isEmpty())
                            Toast.makeText(getApplicationContext(), msgtxt,Toast.LENGTH_SHORT).show();
                    }
                    finishOwn();
                    break;
                case 18:
                    // 关闭进度条，Toast弹出传递文字
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    if(msg.obj!=null){
                        String msgtxt = msg.obj.toString();
                        if(msgtxt!=null&&!msgtxt.isEmpty())
                            Toast.makeText(getApplicationContext(), msgtxt,Toast.LENGTH_SHORT).show();
                    }
                    updateData();
                    break;
                default:
                    break;
            }

        }


    };
    public void finishOwn(){

    }
    public void updateData() {

    }
    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("系统提示");
        switch (id) {
            case 2:
                b.setMessage("登录成功！");
                b.setNeutralButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 3:
                b.setMessage(errorMsg);
                b.setNeutralButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 4:
                b.setMessage("您的网络不给力，请检查网络。");
                b.setNeutralButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;

            case 5:
                b.setMessage("是否确定退出？");
                b.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                b.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 6:
                b.setMessage("");
                b.setNeutralButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 7:
                b.setMessage("");
                b.setNeutralButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            default:
                break;
        }
        dialog = b.create();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 刷新列表
     */
    public void updateListView() {}
    /**
     * 刷新页面
     */
    public void updateView() {}

    /**
     * 转到首页面
     */
    protected void toStartactivity() {
        // Intent i = new Intent(BaseListActivity.this, LoginActivity.class);
        // startActivity(i);
        // finish();
        return;
    }

    /**
     * 收藏单击监听
     *
     * @param
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener() {
        return null;
    }

    /**
     * 收藏单击监听
     *
     * @param
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener1() {
        return null;
    }

    /**
     * 异常时处理方法
     */
    protected void exhand() {

    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * 退出
     *
     */
    protected void logout() {
        this.finish();
    }

    protected void finishActivity() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);

        startMain.addCategory(Intent.CATEGORY_HOME);

        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(startMain);

        System.exit(0);
    }

    /**
     * 判断s是否为空，为空返回空字符串
     *
     * @param s
     * @return
     */
    protected String testStringNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
    public void getTopView(TopBean topBean){
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_left=(TextView)findViewById(R.id.tv_left);
        tv_right=(TextView)findViewById(R.id.tv_right);
        tv_title.setText(topBean.getTitle());
        if(topBean.isLeft()){
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setText(topBean.getLeft());
            tv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }else{
            tv_left.setVisibility(View.GONE);
        }
        if(topBean.isRight()){
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(topBean.getRight());
        }else{
            tv_right.setVisibility(View.GONE);
        }
    }
    protected void showNormalDialog(String title,String context){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(context);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    /**
     * index代表位置
     * */
    public void getJumpFoot(final Activity activity,int index,int oldindex){
        //   int[] foot={R.id.lin_foot1,R.id.lin_foot2,R.id.lin_foot3,R.id.lin_foot4,R.id.lin_foot5};
        //((TextView)foot.getChildAt(0)).getBackground()
            /* Intent intent =getIntent();
            int oldindexs=intent.getIntExtra("oldindexss",0);*/
        /*if(oldindexs!=index){

        }*/
        Log.e("11111111111111:",oldindexsintent+"");
        footView(index);
            lin_foot1=(LinearLayout)findViewById(R.id.lin_foot1);
            lin_foot2=(LinearLayout)findViewById(R.id.lin_foot2);
            lin_foot3=(LinearLayout)findViewById(R.id.lin_foot3);
            lin_foot4=(LinearLayout)findViewById(R.id.lin_foot4);
            lin_foot5=(LinearLayout)findViewById(R.id.lin_foot5);
        if(oldindexsintent!=0){
            lin_foot1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    intent.putExtra("index", 0);
                    intent.putExtra("oldindexs", 0);
                    intent.setClass(activity, IndexActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

        if(oldindexsintent!=1) {
            lin_foot2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //公告
                    Intent intent = new Intent();
                    intent.putExtra("index", 1);
                    intent.putExtra("oldindexs", 1);
                    intent.setClass(activity, NewsListActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
        if(oldindexsintent!=2) {
            lin_foot3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //法规
                    Intent intent = new Intent();
                    intent.putExtra("index", 2);
                    intent.putExtra("oldindexs", 2);

                    intent.setClass(activity, NewsListActivity.class);
                    activity.startActivity(intent);
                }
            });}
        if(oldindexsintent!=3) {
            lin_foot4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //新闻
                    Intent intent = new Intent();
                    intent.putExtra("index", 3);
                    intent.putExtra("oldindexs", 3);

                    intent.setClass(activity, NewsListActivity.class);
                    activity.startActivity(intent);
                }
            });}
        if(oldindexsintent!=4) {
            lin_foot5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("index", 4);
                    intent.putExtra("oldindexs", 4);

                    intent.setClass(activity, NewsInfoActivity.class);
                    activity.startActivity(intent);
                }
            });}
        }



    /**
     *
     * 显示第几个下部信息，从1开始到5
     * */
    public void footView(int index){

        //未选
        int untextcolor=R.color.white;
        int unbackroundcolor=R.color.btnblue;
        //已选
        int textcolor=getResources().getColor(R.color.btnblue);
        int backroundcolor=getResources().getColor(R.color.white);

        int[] lin_id={R.id.lin_foot1,R.id.lin_foot2,R.id.lin_foot3,R.id.lin_foot4,R.id.lin_foot5};
        LinearLayout foot=(LinearLayout)findViewById(lin_id[index]);
        foot.setBackgroundColor(backroundcolor);//背景变白
        TextView whitetext=(TextView)foot.getChildAt(1);
        whitetext.setTextColor(textcolor);//文字变蓝
        switch (index){
            case 0:
                ((ImageView)foot.getChildAt(0)).setImageResource(R.drawable.sy002);//换图
                break;

            case 1:
                ((ImageView)foot.getChildAt(0)).setImageResource(R.drawable.tz01);
                break;
            case 2:
                ((ImageView)foot.getChildAt(0)).setImageResource(R.drawable.fl01);
                break;
            case 3:
                ((ImageView)foot.getChildAt(0)).setImageResource(R.drawable.xw01);
                break;
            case 4:
                ((ImageView)foot.getChildAt(0)).setImageResource(R.drawable.gr01);
                break;

        }
    }
    /**
     * i=1 公告
     *
     * i=1 法律
     *
     * i=1 新闻
     *
     * num=条数
     *
     * */
    public List<Map<String, Object>> newlistcontext(int i,int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String title="";
        String time="";
        String content="";
        switch (i){
            case 1:
                title="公告公告公告公告公告公告公告公告公告公公告";
                time="2017-05-20";
                content="公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容公告内容";
                break;
            case 2:
                title="法律法律法律法律法律法律法律法律法律法律";
                time="2017-06-20";
                content="法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规法律法规";
                break;
            case 3:
                title="新闻新闻新闻新闻新闻新闻新闻新闻新闻新";
                time="2016-12-20";
                content="新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容";
                break;
        }


        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("id",j);
            map.put("title",title);
            map.put("time",time);
            map.put("context",content);
            list.add(map);
        }

        return list;

    }
    public List<Map<String, Object>> newslistcontext(int num){
        //http://www.tjcac.gov.cn/zlaqzd/
        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("id","1");
        map.put("title","市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议");
        map.put("time","2017-08-23");
        map.put("context","8月17日，市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议，总队长郝恩海，副总队长施航华、王斌、王书生，市建委质量安全处处长王俊河，执法监督处处长石林，市施工企业协会秘书长黑金山同志在主席台就坐。市、区两级监管机构负责同志，有关企业、协会及驻津办代表，共计300余人参加会议，会议由王斌副总队长主持。\n" +
                "\n" +
                "    王书生副总队长传达了8月12日全市安全生产电视电话会议精神；王斌副总队长传达了8月14日天津市社会维稳和信访工作会议精神。施航华副总队长通报了今年以来建设工程质量安全专项检查、建筑材料封样抽测、行政处罚、安全事故、观摩交流、扬尘治理情况。");


        list.add(map);


        map=new HashMap<String, Object>();
        map.put("id","2");
        map.put("title","总队强化全运会建筑施工应急保障组织工作");
        map.put("time","2017-08-23");
        map.put("context","8月17日，市质安监管总队召开2017年第一次建设工程质量安全专项检查通报分析会议，总队长郝恩海，副总队长施航华、王斌、王书生，市建委质量安全处处长王俊河，执法监督处处长石林，市施工企业协会秘书长黑金山同志在主席台就坐。市、区两级监管机构负责同志，有关企业、协会及驻津办代表，共计300余人参加会议，会议由王斌副总队长主持。\n" +
                "\n" +
                "    王书生副总队长传达了8月12日全市安全生产电视电话会议精神；王斌副总队长传达了8月14日天津市社会维稳和信访工作会议精神。施航华副总队长通报了今年以来建设工程质量安全专项检查、建筑材料封样抽测、行政处罚、安全事故、观摩交流、扬尘治理情况。");


        list.add(map);
        return list;
    }
    public List<Map<String, Object>> lawslistcontext(int num){
        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        return list;
    }
    public List<Map<String, Object>> noticelistcontext(int num){
        List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        return list;
    }
    /*@Override
    protected void onStop() {
        //什么时候调用finish方法？
        super.onStop();
    }*/
}
