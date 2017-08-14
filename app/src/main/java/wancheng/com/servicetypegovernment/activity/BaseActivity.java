package wancheng.com.servicetypegovernment.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;


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
     * @param id
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener() {
        return null;
    }

    /**
     * 收藏单击监听
     *
     * @param id
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
    public String testStringNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

}
