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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.TopBean;
public class CheckDetailActivity extends BaseActivity {
    private ArrayList<ImagesBean> imageUrls;
    private ArrayList<ImagesBean> imageUrlsNew;
    private LinearLayout lin_image;
    private  String photoFileName;
    private  List<Map<String, Object>>  dataList;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private boolean isDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        TopBean topBean=new TopBean("检查要点","返回","下一步",true,true);
        getTopView(topBean);
        imageUrls=new ArrayList<ImagesBean>();
//        imageUrls.add(new ImagesBean("netImage", "http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg"));
//        imageUrls.add(new ImagesBean("netImage", "http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg"));
//        imageUrls.add(new ImagesBean("netImage", "http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg"));
//        imageUrls.add(new ImagesBean("netImage", "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg"));
        imageUrls.add(new ImagesBean("defaultImage", ""));
        dataList= newlistcontext(1,5,imageUrls);
        linearLayout=(LinearLayout)findViewById(R.id.lin_data_list);
        init();
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
                CheckDetailActivity.this.startActivityForResult(intent, 0);
            }
        });

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
                String path=Environment.getExternalStorageDirectory() + "/sgin/Photos/" + photoFileName + ".jpg";
                int size = f_imageUrlNew.size();
                if (fileIsExists(path)) {
                    f_imageUrlNew.remove(size - 1);
                    f_imageUrlNew.add(new ImagesBean("localImage", path));
                    if (size <5) {
                        f_imageUrlNew.add(new ImagesBean("defaultImage", ""));
                    }
                    f_lin_image.removeAllViews();
                    getImageGridViews(f_imageUrlNew,f_lin_image);
                }
                break;
        }
        switch (resultCode) {
            case 999: // 选择带回
                if(data!=null){
                    final LinearLayout f_lin_image=lin_image;
                    final ArrayList<ImagesBean> f_imageUrlNew=imageUrlsNew;
                    ArrayList<String> list = data.getBundleExtra("bundle").getStringArrayList("listurl");
                    ArrayList<ImagesBean> list1 = new ArrayList<ImagesBean>();
                    f_imageUrlNew.remove(f_imageUrlNew.size() - 1);
                    for(int i=0;i<f_imageUrlNew.size();i++){
                        String type=f_imageUrlNew.get(i).getType();
                        if("localImage".equals(type)){
                            f_imageUrlNew.remove(i);
                            --i;
                        }
                    }
                    for (String s : list) {
                        f_imageUrlNew.add(new ImagesBean("localImage",s));
                    }
                    if(f_imageUrlNew.size()<5){
                        f_imageUrlNew.add(new ImagesBean("defaultImage", ""));
                    }
                    f_lin_image.removeAllViews();
                    getImageGridViews(f_imageUrlNew,f_lin_image);
                }


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);



    }
    private void init(){
        if(dataList!=null&&dataList.size()>0){
            LinearLayout item ;
            for(int i=0;i<dataList.size();i++){
                item = (LinearLayout) layoutInflater.inflate(R.layout.item_check_detail_one, null);
                LinearLayout itemChild;
                LinearLayout linChlid=(LinearLayout)item.findViewById(R.id.check_question);
                for(int j=0;j<dataList.size();j++){
                    itemChild = (LinearLayout) layoutInflater.inflate(R.layout.item_check_detail_two, null);
                    final LinearLayout iv_images=(LinearLayout)itemChild.findViewById(R.id.lin_images);
                    final ArrayList<ImagesBean> imagesBeans=new ArrayList<ImagesBean>();
                    final TextView zhinan=(TextView)itemChild.findViewById(R.id.tv_zhinan);
                    final RadioGroup radioGroup=(RadioGroup)itemChild.findViewById(R.id.rg_yse_no);
                    final RadioButton rb_yes=(RadioButton)itemChild.findViewById(R.id.rb_yes);
                    final RadioButton rb_np=(RadioButton)itemChild.findViewById(R.id.rb_no);
                    final RadioButton rb_rational=(RadioButton)itemChild.findViewById(R.id.rb_rational);
                    final LinearLayout lin_no=(LinearLayout)itemChild.findViewById(R.id.lin_no);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(rb_yes.getId()==checkedId){
                                lin_no.setVisibility(View.GONE);
                            }
                            if(rb_np.getId()==checkedId){
                                lin_no.setVisibility(View.VISIBLE);
                            }
                            if(rb_rational.getId()==checkedId){
                                lin_no.setVisibility(View.GONE);
                            }
                        }
                    });
                    zhinan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    zhinan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNormalDialog();
                        }
                    });
                    imagesBeans.addAll(imageUrls);
                    getImageGridViews(imagesBeans,iv_images);
                    linChlid.addView(itemChild);
                }
                linearLayout.addView(item);
            }


        }
    }


    protected boolean showNormalDialogIsDel(String title,String context,final int position,final ArrayList<ImagesBean> imageUrls,
                                            final LinearLayout linearLayout){
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
                        delete(position, imageUrls, linearLayout);
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
    private void delete(int position, final ArrayList<ImagesBean> imageUrls, final LinearLayout linearLayout) {//删除选中项方法
        ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
        boolean isDefult = false;
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
        getImageGridViews(imageUrls, linearLayout);
    }
    private void getImageGridViews(final ArrayList<ImagesBean> imageUrls,final LinearLayout linearLayout){
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
                    final ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
                    if (!(imageUrls.get(index).getType().equals("defaultImage"))) {
                        newList.addAll(imageUrls);
                        if (imageUrls.get(imageUrls.size() - 1).getType().equals("defaultImage")) {
                            newList.remove(newList.size() - 1);
                        }
                        imageBrower(index, newList);
                    } else {
                        ShowPickDialog(imageUrls, linearLayout);
                    }
                }
            });
            lin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!imageUrls.get(index).getType().equals("defaultImage")) {
                        if(showNormalDialogIsDel("提示", "是否确定删除此图片？", index, imageUrls, linearLayout)){

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
                                final LinearLayout linearLayout){
        linearLayout.removeAllViews();
        getImageGridViews(imageUrls,linearLayout);
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
                        File destDir = new File("/sdcard/sgin/Photos/");
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory()+"/sgin/Photos/",
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

    protected void showNormalDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_mydialog,
                (ViewGroup) findViewById(R.id.dialog));
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


}
