package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private  Context context;
    private String photoFileName;
    private String imagePath="";
    private String url="";

    private PopWindow popWindow;
    private boolean  isPOPOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        context=this;
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
        Log.e("===地址：",UserDateBean.getUser().getPhotoimage());
        if(UserDateBean.getUser().getPhotoimage()!=null){
            ArrayList<ImagesBean> imageUrls=new ArrayList<ImagesBean>();
            // userDateBean.getPh
            ImagesBean IB=new ImagesBean();
            IB.setType("netImage");
            IB.setPath(UserDateBean.getUser().getPhotoimage());
            imageUrls.add(IB);
            ImageView imageView=(ImageView)this.findViewById(R.id.images);
            imageView.setBackground(null);
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

   // @Override
   // protected void onStart() {
       /* if(UserDateBean.getUser().getPhotoimage()!=null){
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
        }*/
  //  }

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
                if (mobiletext.length()>0&&isok){
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
        if(url.length()>0){
            UserDateBean.getInstance().setPhotoimage(url);
            ArrayList<ImagesBean> imageUrls=new ArrayList<ImagesBean>();
            ImagesBean IB=new ImagesBean();
            IB.setType("netImage");
            IB.setPath(url);
            imageUrls.add(IB);
            getImageGridViews(imageUrls, R.id.images);
            url="";
        }else{
            name.setText(UserDateBean.getUser().getName());
            office.setText(UserDateBean.getUser().getOffice());
            no.setText(UserDateBean.getUser().getNo());
            phone.setText(UserDateBean.getUser().getPhone());
            mobile.setText(UserDateBean.getUser().getMobile());
            String addres=UserDateBean.getUser().getAddress() ;
            v_address.setText(addres);
            email.setText(UserDateBean.getUser().getEmail());
        }
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
            final int index=0;
            final ImageView imageView=(ImageView)this.findViewById(id);
            ImagesBean image=imageUrls.get(0);
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
                        //imageView.setVisibility(View.GONE);
                        imageView.setBackground(context.getResources().getDrawable(R.drawable.touxiang1));
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });
            }
            if(image.getType().equals("localImage")){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(image.getPath(), options);
                imageView.setImageBitmap(bm);
                imageView.setBackground(null);
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
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ShowPickDialog(imageUrls);
                    return false;
                }
            });

    }
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(this, ImagePagerHeadActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }

    private void ShowPickDialog(final ArrayList<ImagesBean> imageUrls){

        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,100);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                      Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(intent2,200);
                    }
                }).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null){//系统相册
            Uri imageData = data.getData();
            imagePath=getPath(imageData);

        }else if(requestCode==200&&resultCode==RESULT_OK&&data!=null){//系统相机
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = new File("/sdcard/photos/");

            file.mkdirs();
            String str = null;
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
            date = new Date(resultCode);
            str = format.format(date);
            imagePath = "/sdcard/photos/" + str + ".jpg";
            try {
                b = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if(!"".equals(imagePath)&&imagePath.length()>0){
            uploadimage();
        }
    }
    //如果本地有,就不需要再去联网去请求
    private boolean readImage() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getExternalFilesDir("");
        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = getFilesDir();
        }
        File file = new File(filesDir,"icon.png");
        if(file.exists()){
            //存储--->内存
           /* Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            iv.setImageBitmap(bitmap);*/
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * 判断本地是否有该图片,没有则去联网请求
     * */
    @Override
    protected void onResume() {
        super.onResume();
        if(readImage()){
            return;
        }
    }

    private void uploadimage() {
        if(!"".equals(imagePath)&&imagePath.length()>0){
        pd = ProgressDialog.show(this, "", "上传中...");
        new Thread() {
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonQuery = new JSONObject();
                try{
                    jsonQuery.put("uid",UserDateBean.getUser().getId());
                    jsonQuery.put("image",getImageStr(imagePath));
                    map.put("data", Base64Coder.encodeString(jsonQuery.toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                NetUtil net = new NetUtil();
                String res = net.sendPost(ConstUtil.METHOD_UPLOADHEADIMAGE, map);
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
                                url=  Base64Coder.decodeString(jsonObj.optString("data"));
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
    }

    public  String getImageStr(String imgFile) {

        try {
            File file = new File(imgFile);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new String(Base64Coder.encodeLines(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回Base64编码过的字节数组字符串
        return "";
    }
}
