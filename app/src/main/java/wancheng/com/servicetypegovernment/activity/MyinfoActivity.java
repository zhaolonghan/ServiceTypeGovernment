package wancheng.com.servicetypegovernment.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.view.PopWindow;

/**
 * test
 */
public class MyinfoActivity extends BaseActivity {
    private TextView name;
    private TextView office;
    private TextView no;
    private TextView v_address;
    private EditText phone;
    private EditText mobile;
    private EditText email;



    private PopWindow popWindow;
    private boolean  isPOPOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        onOperationEvent();
    }
    private void initView(){
        TopBean topBean=new TopBean("基本资料","返回","保存",true,true);
        getTopView(topBean);
        name=(TextView)this.findViewById(R.id.name);
        office=(TextView)this.findViewById(R.id.office);
        no=(TextView)this.findViewById(R.id.no);
        v_address=(TextView)this.findViewById(R.id.v_address);

        phone=(EditText)this.findViewById(R.id.phone);
        mobile=(EditText)this.findViewById(R.id.mobile);
        email=(EditText)this.findViewById(R.id.email);

        name.setText(UserDateBean.getUser().getName());
        office.setText(UserDateBean.getUser().getOffice());
        no.setText(UserDateBean.getUser().getNo());
        phone.setText(UserDateBean.getUser().getPhone());
        mobile.setText(UserDateBean.getUser().getMobile());
        email.setText(UserDateBean.getUser().getEmail());
        String addres=UserDateBean.getUser().getAddress() ;
        v_address.setText(addres);


    }

    @Override
    protected void onStart() {
        if(UserDateBean.getUser().getPhotoimage()!=null){
            ArrayList<ImagesBean> imageUrls=new ArrayList<ImagesBean>();
            // userDateBean.getPh
            ImagesBean IB=new ImagesBean();
            IB.setType("netImage");
            IB.setPath(UserDateBean.getUser().getPhotoimage());
            imageUrls.add(IB);
            getImageGridViews(imageUrls, R.id.images);
        }else{
            ImageView imageView=(ImageView)this.findViewById(R.id.images);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
            ImagesBean IB=new ImagesBean();
            IB.setType("defaultImage");
            ArrayList<ImagesBean> imageUrls=new ArrayList<ImagesBean>();
            imageUrls.add(IB);
            getImageGridViews(imageUrls, R.id.images);
            //imageView.setVisibility(View.GONE);
        }
    }

    public void onOperationEvent() {
        //监控筛选按钮
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonetext=phone.getText().toString();
                String mobiletext=mobile.getText().toString();
                String emailtext=email.getText().toString();
                //判断
                String msg="";
                boolean isok=true;
                if(phonetext.length()>0&&isok){
                    isok= isMobileNO(phonetext);
                    if(!isok){
                        msg="请输入正确的手机号码格式!";
                    }

                }
                if(mobiletext.length()>0&&isok){
                    /*isok= isTel(mobiletext);
                    if(!isok){
                        msg="请输入正确的固定电话格式!";
                    }*/

                }
                if(emailtext.length()>0&&isok){
                    isok= isEmail(emailtext);
                    if(!isok){
                        msg="请输入正确的电子邮箱格式!";
                    }

                }
                if(isok){
                    updateinfo(UserDateBean.getUser().getId(), mobiletext, emailtext,phonetext);
                }else{
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    // 验证手机号是否为正确手机号
    public  boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public  boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 固定电话
     * */
    public  boolean isTel(String mobiletext) {
        String str = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mobiletext);
        return m.matches();
    }
    @Override
    public void updateView() {
        name.setText(UserDateBean.getUser().getName());
        office.setText(UserDateBean.getUser().getOffice());
        no.setText(UserDateBean.getUser().getNo());
        phone.setText(UserDateBean.getUser().getPhone());
        mobile.setText(UserDateBean.getUser().getMobile());
        String addres=UserDateBean.getUser().getAddress() ;
        v_address.setText(addres);
        email.setText(UserDateBean.getUser().getEmail());
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
        if(isPOPOpen){
            popWindow.dismiss();
            isPOPOpen=false;
        }

        // this.finish();
    }


    private void updateinfo(final String uid,final String mobile,final String email,final String phone) {
        pd = ProgressDialog.show(this, "", "请稍候...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{

                    jsonQuery.put("uid",uid);
                    jsonQuery.put("mobile", mobile);
                    jsonQuery.put("email", email);
                    jsonQuery.put("phone", phone);
                    String data=  jsonQuery.toString();
                    data= Base64Coder.encodeString(data);
                    map.put("data", data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.posturl(ConstUtil.METHOD_UPDATEUSER, map);
                //Log.e("res", res);
                if (res == null || "".equals(res) || res.contains("Fail to establish http connection!")) {
                    handler.sendEmptyMessage(4);
                } else {
                    Message msg = new Message();
                    msg.what = 15;
                    if (!res.isEmpty()) {
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(res);
                            String msg_code = testStringNull(jsonObj.optString("msg"));
                            String code = testStringNull(jsonObj.optString("code"));
                            if ("0".equals(code)) {
                                UserDateBean.getInstance().setMobile(mobile);
                                UserDateBean.getInstance().setEmail(email);
                                UserDateBean.getInstance().setPhone(phone);
                                msg.what = 13;
                                msg.obj = msg_code;
                            } else {
                                if (msg_code != null && !msg_code.isEmpty())
                                    msg.obj = msg_code;
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
        Intent intent = new Intent(this, ImagePagerHeadActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }








}
