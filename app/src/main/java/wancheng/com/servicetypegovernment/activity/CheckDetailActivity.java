package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImageUpload;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.sqlLite.DatabaseHelper;
import wancheng.com.servicetypegovernment.util.JSONUtils;

public class CheckDetailActivity extends BaseActivity {
    private ArrayList<ImagesBean> imageUrls;
    private ArrayList<ImagesBean> imageUrlsNew;
    private LinearLayout lin_image;
    private  String photoFileName;
    private  List<Map<String, Object>>  dataList;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private boolean isDel;
    private DatabaseHelper databaseHelper;
    private String corpId;
    private String ztlx;
    private String checkAll;
    private String uid;
    private long msgId;
    private int imageIndexP=-1;//父索引;
    private int imageIndexC=-1;//子索引
    private String address;
    private String corpname;
    private String fuzeren;
    private String phone;
    private String permits;
    private String tableName;
    private String type;
    private String zfry1;
    private String zfry2;
    private String checkDate;
    private List<ImageUpload> imageUploads=new ArrayList<ImageUpload>();
    private List<Map<String,Object>> sendList;
    public static CheckDetailActivity instance = null;
    private String resultId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        databaseHelper=new DatabaseHelper(this);
        instance=this;
        Intent intent=getIntent();
        corpId=intent.getStringExtra("corpId");
        ztlx=intent.getStringExtra("ztlx");
        checkAll=intent.getStringExtra("checkAll");
        uid=intent.getStringExtra("uid");
        address=intent.getStringExtra("address");
        corpname=intent.getStringExtra("corpname");
        fuzeren=intent.getStringExtra("fuzeren");
        phone=intent.getStringExtra("phone");
        permits=intent.getStringExtra("permits");
        msgId=intent.getLongExtra("insertid", -1);
        tableName=intent.getStringExtra("tableName");
        checkDate=intent.getStringExtra("checkDate");
        type=intent.getStringExtra("type");
        zfry1=intent.getStringExtra("zfry1");
        zfry2=intent.getStringExtra("zfry2");
        resultId=intent.getStringExtra("resultId");
        Log.e("msgId",msgId+"");
        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        TopBean topBean=new TopBean("检查要点","上一步","下一步",true,true);
        getTopView(topBean);
        imageUrls=new ArrayList<ImagesBean>();
        dataList=new ArrayList<Map<String, Object>>();
        try {
            Map<String,Object> map=null;
            Map<String,Object> mapChild=null;
            List<Map<String, Object>>  dataChildList;
            JSONArray checkerList=new JSONArray(checkAll);
            if(checkerList!=null&&checkerList.length()>0){
                for(int i=0;i<checkerList.length();i++){
                    map=new HashMap<String,Object>();
                    dataChildList=new ArrayList<Map<String,Object>>();
                    JSONObject object=checkerList.getJSONObject(i);
                    String no= JSONUtils.getString(object, "no", "");
                    String name= JSONUtils.getString(object, "name", "");
                    String remarks= JSONUtils.getString(object, "remarks", "");
                    JSONArray checkerChildList=new JSONArray(JSONUtils.getString(object, "content", ""));
                    if(checkerChildList!=null&&checkerChildList.length()>0){
                        for(int j=0;j<checkerChildList.length();j++){
                            mapChild=new HashMap<String,Object>();
                            JSONObject objectChild=checkerChildList.getJSONObject(j);
                            String content_sort= JSONUtils.getString(objectChild, "content_sort", "");
                            String itemId= JSONUtils.getString(objectChild, "itemId", "");
                            String isPoint= JSONUtils.getString(objectChild, "isPoint", "");
                            String itemContentId= JSONUtils.getString(objectChild, "itemContentId", "");
                            String content= JSONUtils.getString(objectChild, "content", "");
                            String mode= JSONUtils.getString(objectChild, "mode", "");
                            String guide= JSONUtils.getString(objectChild, "guide", "");
                            String base= JSONUtils.getString(objectChild, "base", "");
                            mapChild.put("content_sort",content_sort);
                            mapChild.put("itemId",itemId);
                            mapChild.put("isPoint",isPoint);
                            mapChild.put("itemContentId",itemContentId);
                            mapChild.put("content",content);
                            mapChild.put("mode",mode);
                            mapChild.put("guide",guide);
                            mapChild.put("base",base);
                            dataChildList.add(mapChild);
                        }

                    }
                    map.put("no",no);
                    map.put("name",name);
                    map.put("remarks",remarks);
                    map.put("dataChildList",dataChildList);
                    Log.e("name",name);
                    dataList.add(map);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        imageUrls.add(new ImagesBean("defaultImage", ""));
        linearLayout=(LinearLayout)findViewById(R.id.lin_data_list);
        init();
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog("提示","返回上一步将清空所有检查项，是否确定？");
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
//                CheckDetailActivity.this.startActivityForResult(intent, 0);
                //
//                boolean isOK = databaseHelper.deleteById(4);
//                if(isOK){
//                    List<Map<String, String>> list = databaseHelper.find(4);
//                    if (list != null && list.size() > 0) {
//                        Intent intent = new Intent(CheckDetailActivity.this, SubmitImageService.class);
//                        intent.putExtra("datalist", (Serializable) (list));
//                        startService(intent);
//                        InformActivity.instance.finish();
//                        finish();
//
//                    }
//                }
                sendList=new ArrayList<Map<String,Object>>();
               if(getListViewData()){
                   Intent intent = new Intent();
                   intent.putExtra("corpId",corpId);
                   intent.putExtra("ztlx",ztlx);
                   intent.putExtra("uid","2");
                   intent.putExtra("address",address);
                   intent.putExtra("insertid",msgId);
                   intent.putExtra("corpname",corpname);
                   intent.putExtra("fuzeren",fuzeren);
                   intent.putExtra("phone",phone);
                   intent.putExtra("permits",permits);
                   intent.putExtra("resultId",resultId);
                   intent.putExtra("data", getJsonStr());
                   intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
                   CheckDetailActivity.this.startActivity(intent);
               }

            }
        });

    }
    private String getJsonStr(){
        try{
//            JSONObject one=new JSONObject();
//            one.put("corpId",corpId);
//            one.put("zfry1",zfry1);
//            one.put("zfry2",zfry2);
//            one.put("date",checkDate);
//            one.put("tableName",tableName);
//            one.put("type",type);
//            //one.put("corpId","11");
//            JSONArray  two=new JSONArray();
//            for(int i=0;i <sendList.size();i++){
//                Map<String,Object> map=sendList.get(i);
//                JSONObject three=new JSONObject();
//                three.put("content_sort",map.get("content_sort").toString());
//                three.put("ispoint",map.get("isImp").toString());
//                three.put("result", map.get("checkResult").toString());
//                two.put(i,three);
//            }
//            one.put("jcnr",two);
//            Log.e("data size111 ", one.toString().length() + "");
            String str="{";
            str+="\"corpId\":\""+corpId+"\"";
            str+=",\"zfry1\":\""+zfry1+"\"";
            str+=",\"zfry2\":\""+zfry2+"\"";
            str+=",\"date\":\""+checkDate+"\"";
           str+=",\"tableName\":\""+tableName+"\"";
            str+=",\"type\":\""+type+"\"";
            str+=",\"jcnr\":[";
            for(int i=0;i <sendList.size();i++){
                Map<String,Object> map=sendList.get(i);
                if(i==0){
                    str+="{";
                }else{
                    str+=",{";
                }
                str+="\"content_sort\":\""+map.get("content_sort").toString()+"\"";
                str+=",\"ispoint\":\""+map.get("isImp").toString()+"\"";
                str+=",\"result\":\""+map.get("checkResult").toString()+"\"}";
            }
            str+="]}";
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private boolean getListViewData(){
        String str="";
        int result;
        Map<String,Object> map=null;
        if(linearLayout!=null&&linearLayout.getChildCount()>0){
            for(int i=0;i<linearLayout.getChildCount();i++){
                LinearLayout linearLayoutChlid=(LinearLayout)linearLayout.getChildAt(i);
                LinearLayout linearLayoutChlid1=(LinearLayout)linearLayoutChlid.findViewById(R.id.check_question);
                Log.e("outChlid1()",linearLayoutChlid1.getChildCount()+"");
                for(int j=0;j<linearLayoutChlid1.getChildCount();j++){
                    LinearLayout l1=(LinearLayout)linearLayoutChlid1.getChildAt(j);
                    EditText ed_checknote=(EditText)l1.findViewById(R.id.ed_checknote);
                    RadioGroup radioGroup=(RadioGroup)l1.findViewById(R.id.rg_yse_no);
                    RadioButton rb_yes=(RadioButton)l1.findViewById(R.id.rb_yes);
                    RadioButton rb_np=(RadioButton)l1.findViewById(R.id.rb_no);
                    RadioButton rb_rational=(RadioButton)l1.findViewById(R.id.rb_rational);
                    if (radioGroup.getCheckedRadioButtonId()==rb_yes.getId()){
                        result=1;
                    } else  if(radioGroup.getCheckedRadioButtonId()==rb_np.getId()){
                        result=0;
                    }else  if(radioGroup.getCheckedRadioButtonId()==rb_rational.getId()){
                        result=2;
                    }else{
                        result=3;
                        str+=","+(i+1)+"."+(j+1);
                    }
                    map=new HashMap<String,Object>();
                    map.put("pid",ztlx);
                    String cid=((List<Map<String, Object>>) (dataList.get(i).get("dataChildList"))).get(j).get("itemContentId").toString();
                    String isPoint=((List<Map<String, Object>>) (dataList.get(i).get("dataChildList"))).get(j).get("isPoint").toString();
                    map.put("cid",cid );
                    map.put("msgId",msgId);
                    map.put("isImp",isPoint);
                    map.put("checkResult",result);
                    map.put("checkNote", ed_checknote.getText().toString());
                    map.put("content_sort",((List<Map<String, Object>>) (dataList.get(i).get("dataChildList"))).get(j).get("content_sort").toString());
                    sendList.add(map);
                    long id=databaseHelper.findCheck(map.get("pid").toString(), map.get("cid").toString(),msgId);
                    if(id!=-1){
                       databaseHelper.updataCheck(map);
                        Log.e("走修改", i + "," + j);
                    }else{
                        id=databaseHelper.insertCheck(map);
                        Log.e("走插入",i+","+j);
                    }
                    databaseHelper.deleteImageByCheckId(id);
                    if(imageUploads!=null&&imageUploads.size()>0){
                        for(int k=0;k<imageUploads.size();k++){
                            ImageUpload imageUpload=imageUploads.get(k);
                            if(imageUpload.getIndexP()==i&&imageUpload.getIndexC()==j){
                                databaseHelper.insertImages(id,imageUpload.getPath(),msgId);
                            }
                        }
                    }
                }
            }
            if(!",".equals(str)){
                str+="没有选择，是否确认为不检查的项目？";
                showNormalDialog1(str.substring(1));
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 998:// 拍照带回
                final LinearLayout f_lin_image=lin_image;
                final ArrayList<ImagesBean> f_imageUrlNew=imageUrlsNew;
                String path=Environment.getExternalStorageDirectory() + "/Wancheng/Photos/" + photoFileName + ".jpg";
                int size = f_imageUrlNew.size();
                if (fileIsExists(path)) {
                    f_imageUrlNew.remove(size - 1);
                    f_imageUrlNew.add(new ImagesBean("localImage", path));
                    imageUploads.add(new ImageUpload(imageIndexP,imageIndexC,path));
                    if (size <5) {
                        f_imageUrlNew.add(new ImagesBean("defaultImage", ""));
                    }
                    f_lin_image.removeAllViews();
                    getImageGridViews(f_imageUrlNew, f_lin_image, imageIndexP, imageIndexC);
                    Log.e("imageIndex拍照P", imageIndexP + "");
                    Log.e("imageIndex拍照C", imageIndexC+"");
                    Log.e("imageUploads size", imageUploads.size()+"");
                }
                break;
        }
        switch (resultCode) {
            case 999: // 选择带回W
                if(data!=null){
                    final LinearLayout f_lin_image=lin_image;
                    final ArrayList<ImagesBean> f_imageUrlNew=imageUrlsNew;
                    ArrayList<String> list = data.getBundleExtra("bundle").getStringArrayList("listurl");
                    f_imageUrlNew.remove(f_imageUrlNew.size() - 1);
                    for(int i=0;i<f_imageUrlNew.size();i++){
                        String type=f_imageUrlNew.get(i).getType();
                        if("localImage".equals(type)){
                            f_imageUrlNew.remove(i);
                            --i;
                        }
                    }
                    if(imageUploads!=null&&imageUploads.size()>0){
                        for(int i=0;i<imageUploads.size();i++){
                           if(imageUploads.get(i).getIndexP()==imageIndexP&&imageUploads.get(i).getIndexC()==imageIndexC){
                               imageUploads.remove(i);
                               --i;
                           }
                        }
                    }
                    for (String s : list) {
                        f_imageUrlNew.add(new ImagesBean("localImage", s));
                        imageUploads.add(new ImageUpload(imageIndexP,imageIndexC,s));
                    }
                    if(f_imageUrlNew.size()<5){
                        f_imageUrlNew.add(new ImagesBean("defaultImage", ""));
                    }
                    f_lin_image.removeAllViews();
                    getImageGridViews(f_imageUrlNew, f_lin_image, imageIndexP, imageIndexC);
                    Log.e("imageIndex选择P", imageIndexP + "");
                    Log.e("imageIndex选择C", imageIndexC + "");
                    Log.e("imageUploads size", imageUploads.size()+"");
                }


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);



    }
    private void init(){
        if(dataList!=null&&dataList.size()>0){
            LinearLayout item ;
            Map<String,Object> map;
            Map<String,Object> mapChild;
            for(int i=0;i<dataList.size();i++){
                item = (LinearLayout) layoutInflater.inflate(R.layout.item_check_detail_one, null);
                map=dataList.get(i);
                final TextView tv_checktitle=(TextView)item.findViewById(R.id.tv_checktitle);
                final TextView tv_checkremark=(TextView)item.findViewById(R.id.tv_checkremark);
                tv_checktitle.setText(map.get("no").toString()+"、"+map.get("name").toString());
                final String remark=map.get("remarks").toString();
                if(remark!=null&&!"".equals(remark)){
                    tv_checkremark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNormalDialog(remark);
                        }
                    });
                }else{
                    tv_checkremark.setVisibility(View.GONE);
                }
                tv_checkremark.setText(map.get("no").toString()+"、"+map.get("name").toString());
                LinearLayout itemChild;
                LinearLayout linChlid=(LinearLayout)item.findViewById(R.id.check_question);
                List<Map<String,Object>> childList=(List<Map<String,Object>>)map.get("dataChildList");
                for(int j=0;j<childList.size();j++){
                    mapChild=childList.get(j);
                    itemChild = (LinearLayout) layoutInflater.inflate(R.layout.item_check_detail_two, null);
                    final LinearLayout iv_images=(LinearLayout)itemChild.findViewById(R.id.lin_images);
                    final ArrayList<ImagesBean> imagesBeans=new ArrayList<ImagesBean>();
                    final TextView zhinan=(TextView)itemChild.findViewById(R.id.tv_zhinan);
                    final RadioGroup radioGroup=(RadioGroup)itemChild.findViewById(R.id.rg_yse_no);
                    final RadioButton rb_yes=(RadioButton)itemChild.findViewById(R.id.rb_yes);
                    final RadioButton rb_np=(RadioButton)itemChild.findViewById(R.id.rb_no);
                    final RadioButton rb_rational=(RadioButton)itemChild.findViewById(R.id.rb_rational);
                    final LinearLayout lin_no=(LinearLayout)itemChild.findViewById(R.id.lin_no);
                    final TextView tv_question_title=(TextView)itemChild.findViewById(R.id.tv_question_title);
                    final TextView tv_isPoint=(TextView)itemChild.findViewById(R.id.tv_isPoint);
                    tv_question_title.setText(mapChild.get("content_sort").toString() + " " + mapChild.get("content").toString());
                    final String mode=mapChild.get("mode").toString();
                    final String guide=mapChild.get("guide").toString();
                    final String base=mapChild.get("base").toString();
                    if("1".equals(mapChild.get("isPoint").toString())){
                        tv_isPoint.setVisibility(View.VISIBLE);
                    }else{
                        tv_isPoint.setVisibility(View.GONE);
                    }
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (rb_yes.getId() == checkedId) {
                                lin_no.setVisibility(View.GONE);
                            }
                            if (rb_np.getId() == checkedId) {
                                lin_no.setVisibility(View.VISIBLE);
                            }
                            if (rb_rational.getId() == checkedId) {
                                lin_no.setVisibility(View.GONE);
                            }
                        }
                    });
                    zhinan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    zhinan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNormalDialog(mode,guide,base);
                        }
                    });
                    imagesBeans.addAll(imageUrls);
                    getImageGridViews(imagesBeans,iv_images,i,j);
                    linChlid.addView(itemChild);
                }
                linearLayout.addView(item);
            }


        }
    }


    protected boolean showNormalDialogIsDel(String title,String context,final int position,final ArrayList<ImagesBean> imageUrls,
                                            final LinearLayout linearLayout,final int  imageindexp,final int  imageindexc){
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
                        delete(position, imageUrls, linearLayout,imageindexp,imageindexc);
                        isDel = true;

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
        return isDel;
    }
    private void delete(int position, final ArrayList<ImagesBean> imageUrls, final LinearLayout linearLayout,final int imageindexp,final int  imageindexc) {//删除选中项方法
        ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
        boolean isDefult = false;
        String path=imageUrls.get(position).getPath();
        imageUrls.remove(position);
        linearLayout.removeAllViews();
        newList.addAll(imageUrls);
        imageUrls.clear();
        imageUrls.addAll(newList);
        for(ImagesBean image:imageUrls){
            if(image.getType().equals("defaultImage")){
                isDefult=true;
            }
        }
        if(!isDefult){
            imageUrls.add(new ImagesBean("defaultImage",""));
        }
        if(imageUploads!=null&&imageUploads.size()>0){
            for(int i=0;i<imageUploads.size();i++){
                if(imageUploads.get(i).getIndexP()==imageIndexP&&imageUploads.get(i).getIndexC()==imageIndexC&&imageUploads.get(i).getPath().equals(path)){
                    imageUploads.remove(i);
                    --i;
                }
            }
        }
        getImageGridViews(imageUrls, linearLayout, imageindexp, imageindexc);
        Log.e("imageIndex删除P", imageIndexP + "");
        Log.e("imageIndex删除C", imageIndexC + "");
        Log.e("imageUploads size", imageUploads.size() + "");
    }
    private void getImageGridViews(final ArrayList<ImagesBean> imageUrls,final LinearLayout linearLayout,final int imageindexp,final int imageindexc){
        for(int k=0;k<imageUrls.size();k++){
            final int index=k;
            LinearLayout lin= (LinearLayout) layoutInflater.inflate(R.layout.item_gridview, null);
            final ImageView imageView = (ImageView) lin.findViewById(R.id.iv_image);
            ImagesBean image=imageUrls.get(k);
            if (image.getType().equals("netImage")){
                DisplayImageOptions options = new DisplayImageOptions.Builder()//
                        .cacheInMemory(true)//
                        .cacheOnDisk(true)//
                        .bitmapConfig(Bitmap.Config.RGB_565)//
                        .build();
                ImageLoader.getInstance().displayImage(image.getPath(), imageView, options);
            }
            if(image.getType().equals("defaultImage")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.add_img));
            }
            if(image.getType().equals("localImage")){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(image.getPath(), options);
                imageView.setImageBitmap(bm);
            }
            lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageIndexP=imageindexp;
                    imageIndexC=imageindexc;
                    final ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
                    if (!(imageUrls.get(index).getType().equals("defaultImage"))) {
                        newList.addAll(imageUrls);
                        if (imageUrls.get(imageUrls.size() - 1).getType().equals("defaultImage")) {
                            newList.remove(newList.size() - 1);
                        }
                        imageBrower(index, newList);
                    } else {
                        ShowPickDialog(imageUrls, linearLayout,imageindexp,imageindexc);
                    }
                }
            });
            lin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    imageIndexP=imageindexp;
                    imageIndexC=imageindexc;
                    if (!imageUrls.get(index).getType().equals("defaultImage")) {
                        if(showNormalDialogIsDel("提示", "是否确定删除此图片？", index, imageUrls, linearLayout,imageindexp,imageindexc)){

                        }
                    }
                    return true;
                }
            });
            linearLayout.addView(lin);
            imageUrlsNew=imageUrls;
            lin_image=linearLayout;
        }
    }
    private void ShowPickDialog(final ArrayList<ImagesBean> imageUrls,
                                final LinearLayout linearLayout,final int imageindexp,final int imageindexc){
        linearLayout.removeAllViews();
        getImageGridViews(imageUrls,linearLayout,imageindexp,imageindexc);
        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(CheckDetailActivity.this,
                                ImageChooseActivity.class);
                        Bundle bundle = new Bundle();
                        final ArrayList<String> url2 = new ArrayList<String>();
                        for (ImagesBean im : imageUrls) {
                            if (im.getType().equals("localImage")){
                                url2.add(im.getPath());
                            }
                        }
                        bundle.putStringArrayList("listurl", url2);
                        bundle.putInt("listsize", imageUrls.size()-1);
                        intent.putExtra("bundle", bundle);
                        startActivityForResult(intent,999);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        photoFileName = System.currentTimeMillis() + ((Math.random() * 9 + 1) * 1000) + "";
                        File destDir = new File("/sdcard/Wancheng/Photos/");
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory()+"/Wancheng/Photos/",
                                        photoFileName + ".jpg")));
                        startActivityForResult(intent, 998);
                    }
                }).show();
    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }

    protected void showNormalDialog(String mode,String guide,String base){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_mydialog,
                (ViewGroup) findViewById(R.id.dialog));
        TextView tv_mode=(TextView)layout.findViewById(R.id.tv_mode);
        TextView tv_guide=(TextView)layout.findViewById(R.id.tv_guide);
        TextView tv_base=(TextView)layout.findViewById(R.id.tv_base);
        tv_mode.setText(mode);
        tv_guide.setText(guide);
        tv_base.setText(base);
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setView(layout);
        normalDialog.setTitle("检查指南");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        normalDialog.show();
    }
    protected void showNormalDialog(String remark){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_remarkdialog,
                (ViewGroup) findViewById(R.id.dialog));
        TextView tv_remarks=(TextView)layout.findViewById(R.id.tv_remarks);
        tv_remarks.setText(remark);
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setView(layout);
        normalDialog.setTitle("注意");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        normalDialog.show();
    }
    protected void showNormalDialog1(String remark){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_remarkdialog,
                (ViewGroup) findViewById(R.id.dialog));
        TextView tv_remarks=(TextView)layout.findViewById(R.id.tv_remarks);
        tv_remarks.setText(remark);
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setView(layout);
        normalDialog.setTitle("注意");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("corpId",corpId);
                        intent.putExtra("ztlx",ztlx);
                        intent.putExtra("uid","2");
                        intent.putExtra("address",address);
                        intent.putExtra("insertid",msgId);
                        intent.putExtra("corpname",corpname);
                        intent.putExtra("fuzeren",fuzeren);
                        intent.putExtra("phone",phone);
                        intent.putExtra("permits",permits);
                        intent.putExtra("resultId",resultId);
                        intent.putExtra("data", getJsonStr());
                        intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
                        CheckDetailActivity.this.startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();
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

    public boolean fileIsExists(String path){
        try{
            File f=new File(path);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
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
                        if(databaseHelper.deletCheckeById(msgId)){
                            databaseHelper.deleteImageByMsgId(msgId);
                            finish();
                        }
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}
