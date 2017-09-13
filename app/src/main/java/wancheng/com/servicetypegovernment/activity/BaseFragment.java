package wancheng.com.servicetypegovernment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;


public abstract class BaseFragment extends Fragment {

    protected  TextView tv_title;
    protected TextView tv_left;
    protected TextView tv_right;
    protected View contactsLayout ;
    public void getTopView(TopBean topBean,View view){
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_left=(TextView)view.findViewById(R.id.tv_left);
        tv_right=(TextView)view.findViewById(R.id.tv_right);
        tv_title.setText(topBean.getTitle());
        if(topBean.isLeft()){
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setText(topBean.getLeft());
            tv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  finish();
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
    protected abstract void lazyLoad();
        /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */

    // 等待提示框
    protected ProgressDialog pd;

    // 信息对话框
    protected Dialog dialog;

    // 错误信息
    protected String errorMsg = "";
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
                    getActivity().showDialog(2);
                    break;
                case 3:
                    // 失败
                    pd.dismiss();
                    getActivity().showDialog(3);
                    break;
                case 4:
                    // 异常
                    exhand();
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    getActivity().showDialog(4);
                    break;
                case 5:
                    // 弹出退出对话框
                    getActivity().showDialog(5);
                    break;
                case 6:
                    // 退出
                    pd.dismiss();
                    finishActivity();
                    break;
                case 10:
                    // session失效提示
                    pd.dismiss();
                    getActivity().showDialog(10);
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
                    String msgtxt = msg.obj.toString();
                    if(msgtxt!=null&&!msgtxt.isEmpty())
                        Toast.makeText(getActivity().getApplicationContext(), msgtxt, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), msgtxt_obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };


    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_fragment);
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
     *
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener() {
        return null;
    }

    /**
     * 收藏单击监听
     *
     *
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
        getActivity().finish();
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
    public String testStringNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
    private  boolean mBackKeyPressed = false;//记录是否有首次按键

    public  boolean clickBack(int keycode,KeyEvent event,Context context){
        if(keycode==KeyEvent.KEYCODE_BACK){
            if (!mBackKeyPressed) {

                Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();

                mBackKeyPressed = true;

                new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录

                    @Override

                    public void run() {

                        mBackKeyPressed = false;

                    }
                }, 2000);

            }

            else{//退出程序

                getActivity().finish();

                System.exit(0);

            }
        }
        return true;
    }
    public List<Map<String, Object>> newlistcontext(int i,int num){
        List<Map<String, Object>>  list;
        List<Map<String, Object>>  addalllist;
        //标题0  时间1  内容2String id="1";
        String day="";
        String year="";
        String title="";
        String count="";
        String source="";
        switch (i){
            case 1:
                title="国家质量监督检验检疫总局《进出口工业品风险管理办法》";
                day="01";
                year="2017/09";
                count="5";
                source="南方日报";
                break;
            case 2:
                title="中华人民共和国食品安全法实施条例";
                day="01";
                year="2017/09";
                count="10";
                source="南方日报";
                break;
            case 3:
                title="市市场监管委党委召开领导干部警示教育大";
                day="01";
                year="2017/09";
                count="15";
                source="南方日报";
                break;
        }


        list=new ArrayList<Map<String, Object>>();
        for(int j=0;j<num;j++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("day",day);
            map.put("title",title);
            map.put("year",year);
            map.put("count",count);
            map.put("source",source);
            list.add(map);
        }

        return list;

    }
}