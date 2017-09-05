package wancheng.com.servicetypegovernment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import wancheng.com.servicetypegovernment.activity.BaseActivity;
import wancheng.com.servicetypegovernment.activity.CoreActivity;
import wancheng.com.servicetypegovernment.activity.IndexActivity;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.util.Sha1;
import wancheng.com.servicetypegovernment.util.JSONUtils;
public class MainActivity extends BaseActivity {

   private Button btnLogin;
    private String username;
    private String passWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin =(Button)findViewById(R.id.btn_login);
        final EditText ed=(EditText)findViewById(R.id.editText1);
        final EditText ed2=(EditText)findViewById(R.id.editText2);

        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {


                username=ed.getText().toString();
                passWord=ed2.getText().toString();
                Log.e("11111111111111111", Sha1.sHA1(MainActivity.this));
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(passWord)) {
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {


                    getData(username,passWord);
//		                if (!"admin".equals(username)||!"123456".equals(passWord)){
//		                    Toast.makeText(LoginActivity.this, "用户名或密码输入错误", Toast.LENGTH_SHORT).show();
//		                } else {
//		    				LoginActivity.this.finish();
//		                }
                }
            }
        });
    }
    @Override
    public void updateView() {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.setClass(MainActivity.this, CoreActivity.class);
        MainActivity.this.startActivity(intent);
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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    /**
     * 获取数据
     */
    private void getData(final String username,final String password) {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", username);
                map.put("password", password);
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_LOGIN , map);
                Log.e("返回值", res);
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
                                JSONObject dataArray = jsonObj.getJSONObject("data");
                                UserDateBean.getInstance().setUsername(JSONUtils.getString(dataArray, "loginName", ""));
                                UserDateBean.getInstance().setId(JSONUtils.getInt(dataArray, "uid", 0));
                                UserDateBean.getInstance().setName(JSONUtils.getString(dataArray, "name", ""));
                                UserDateBean.getInstance().setPhone(JSONUtils.getString(dataArray, "mobile", ""));
                                msg.what=13;
                                msg.obj=msg_code;
                            }else{
                                if(msg_code!=null&&!msg_code.isEmpty())
                                    msg.obj=msg_code;
                                else
                                    msg.obj="请求异常，请稍后重试！";

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            msg.obj="请求异常，请稍后重试！";
                        }
                        handler.sendMessage(msg);
                    }

                }
            };
        }.start();

    }
}
