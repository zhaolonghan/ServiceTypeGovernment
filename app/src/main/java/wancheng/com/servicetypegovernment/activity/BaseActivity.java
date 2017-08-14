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
 * ����Activity����
 *
 * @author hanzl
 *
 */
@SuppressLint("HandlerLeak")
public  class BaseActivity extends Activity {

    // �ȴ���ʾ��
    protected ProgressDialog pd;

    // ��Ϣ�Ի���
    protected Dialog dialog;

    // ������Ϣ
    protected String errorMsg = "";
    @SuppressLint("ShowToast")
    public Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // ˢ���б�
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    updateListView();
                    break;
                case 2:
                    // �ɹ�
                    pd.dismiss();
                    showDialog(2);
                    break;
                case 3:
                    // ʧ��
                    pd.dismiss();
                    showDialog(3);
                    break;
                case 4:
                    // �쳣
                    exhand();
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    showDialog(4);
                    break;
                case 5:
                    // �����˳��Ի���
                    showDialog(5);
                    break;
                case 6:
                    // �˳�
                    pd.dismiss();
                    finishActivity();
                    break;
                case 10:
                    // sessionʧЧ��ʾ
                    pd.dismiss();
                    showDialog(10);
                    break;
                case 11:
                    // ����δ�����쳣
                    exhand();
                    pd.dismiss();
                    break;
                case 12:
                    // ֻ�رս�����
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    break;
                case 13:
                    // �رս�������Toast������������
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
                    // ˢ��ҳ��
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
                    // �رս�������Toast������������
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
                    // �رս�������Toast������������
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
                    // �رս�������Toast������������
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
        b.setTitle("ϵͳ��ʾ");
        switch (id) {
            case 2:
                b.setMessage("��¼�ɹ���");
                b.setNeutralButton("ȷ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 3:
                b.setMessage(errorMsg);
                b.setNeutralButton("ȷ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 4:
                b.setMessage("�������粻�������������硣");
                b.setNeutralButton("ȷ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;

            case 5:
                b.setMessage("�Ƿ�ȷ���˳���");
                b.setPositiveButton("ȷ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                b.setNegativeButton("ȡ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 6:
                b.setMessage("");
                b.setNeutralButton("ȷ ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 7:
                b.setMessage("");
                b.setNeutralButton("ȷ ��", new DialogInterface.OnClickListener() {
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
     * ˢ���б�
     */
    public void updateListView() {}
    /**
     * ˢ��ҳ��
     */
    public void updateView() {}

    /**
     * ת����ҳ��
     */
    protected void toStartactivity() {
        // Intent i = new Intent(BaseListActivity.this, LoginActivity.class);
        // startActivity(i);
        // finish();
        return;
    }

    /**
     * �ղص�������
     *
     * @param id
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener() {
        return null;
    }

    /**
     * �ղص�������
     *
     * @param id
     * @return
     */
    android.content.DialogInterface.OnClickListener getFavoriteListener1() {
        return null;
    }

    /**
     * �쳣ʱ������
     */
    protected void exhand() {

    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * �˳�
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
     * �ж�s�Ƿ�Ϊ�գ�Ϊ�շ��ؿ��ַ���
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
