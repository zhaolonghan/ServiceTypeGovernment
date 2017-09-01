package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.CheckResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.CheckQuestionAdspter;
import wancheng.com.servicetypegovernment.adspter.ImageChooseAdapter;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.adspter.NoScrollGridAdapter;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.ItemEntity;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.NoScrollGridView;

public class CheckDetailActivity extends BaseActivity {
    private ArrayList<ItemEntity> itemEntities;
    private ItemEntity itemEntity;
    private NoScrollGridView noScrollGridVieww;
    private NoScrollGridAdapter noScrollGridAdapter;
    private ArrayList<ImagesBean> imageUrls;
    private ArrayList<ImagesBean> newList;
    private int type=1;
    private String photoFileName;

    List<Map<String, Object>>  listnews;
    private CheckQuestionAdspter madapter = null;
    private ListView listView=null;
    private CheckQuestionAdspter  infomadapter = null;
    private ListView infolistView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        noScrollGridVieww=(NoScrollGridView) findViewById(R.id.gridview);
        Intent intent=getIntent();

        listnews= newlistcontext(1,5);
        listView=(ListView)findViewById(R.id.checkquestionlist);
        madapter = new CheckQuestionAdspter(this, listnews,0);
        listView.setAdapter(madapter);


        initData();
 /*       itemEntity=itemEntities.get(3);
        imageUrls = itemEntity.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            noScrollGridVieww.setVisibility(View.GONE);


        } else {
            noScrollGridAdapter=new NoScrollGridAdapter(this, imageUrls);
            noScrollGridVieww.setAdapter(noScrollGridAdapter);

        }
        // 点击回帖九宫格，查看大图
        noScrollGridVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(!imageUrls.get(position).getType().equals("defaultImage")){
                    newList=new ArrayList<ImagesBean>();
                    newList.addAll(imageUrls);
                    if(imageUrls.get(imageUrls.size()-1).getType().equals("defaultImage")){
                        newList.remove(newList.size()-1);
                    }
                    imageBrower(position, newList);
                }else{
                    ShowPickDialog();
                }
            }
        });
        noScrollGridVieww.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               if(!imageUrls.get(position).getType().equals("defaultImage")){
                   showNormalDialogIsDel("提示","是否确定删除此图片？",position);
               }
                return true;
            }
        });*/
//        bt_check=(Button)findViewById(R.id.bt_check);
//        bt_check.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Toast.makeText(CheckDetailActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(CheckDetailActivity.this, InformActivity.class);
//                CheckDetailActivity.this.startActivity(intent);
//            }
//        });
//            photoFileName=System.currentTimeMillis()+((Math.random()*9+1)*1000)+"";
//            Intent intent1 = new Intent(
//                    MediaStore.ACTION_IMAGE_CAPTURE);
//            // 指定调用相机拍照后的照片存储的路径
//            intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
//                    .fromFile(new File(Environment
//                            .getExternalStorageDirectory(),
//                            photoFileName + ".jpg")));
//             Toast.makeText(CheckDetailActivity.this, photoFileName, Toast.LENGTH_SHORT).show();
//            startActivityForResult(intent1, 998);




        TopBean topBean=new TopBean("检查要点","返回","下一步",true,true);
        getTopView(topBean);
        /*TextView tv = (TextView) findViewById(R.id.tv_zhinan);
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });*/
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
                CheckDetailActivity.this.startActivityForResult(intent, 0);
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 998:// 拍照带回
                String path=Environment.getExternalStorageDirectory() + "/sgin/" + photoFileName + ".jpg";
                if (fileIsExists(path)) {
                    int size = imageUrls.size();
                    if (size == 5) {
                        imageUrls.remove(size - 1);
                        imageUrls.add(new ImagesBean("localImage", path));
                    } else {
                        imageUrls.add(new ImagesBean("localImage", path));
                        imageUrls.add(new ImagesBean("defaultImage", ""));
                    }

                    noScrollGridAdapter = new NoScrollGridAdapter(CheckDetailActivity.this, imageUrls);//重新绑定一次adapter
                    noScrollGridVieww.setAdapter(noScrollGridAdapter);
                    noScrollGridAdapter.notifyDataSetChanged();//刷新gridview
                    break;
                }
        }
        switch (resultCode) {
            case 999: // 选择带回
                if(data!=null){
                    ArrayList<String> list = data.getBundleExtra("bundle").getStringArrayList("listurl");
                    ArrayList<ImagesBean> list1 = new ArrayList<ImagesBean>();
                    imageUrls.remove(imageUrls.size() - 1);
//                    for(int i=0;i<list.size();i++){
//                        boolean isAdd=true;
//                        String newPath=list.get(i).toString();
//                        for(int j=0;j<imageUrls.size();j++){
//                            if(imageUrls.get(j).getPath().toString().equals(newPath)){
//                                isAdd=false;
//                            }
//                        }
//                        if(isAdd){
//                            imageUrls.add(new ImagesBean("localImage",newPath));
//                        }
//                    }
                    for(int i=0;i<imageUrls.size();i++){
                        String type=imageUrls.get(i).getType();
                        if("localImage".equals(type)){
                            imageUrls.remove(i);
                            --i;
                        }
                    }
                    for(String s:list){
                        imageUrls.add(new ImagesBean("localImage",s));
                    }
                  if(imageUrls.size()<5){
                      imageUrls.add(new ImagesBean("defaultImage", ""));
                  }

                    noScrollGridAdapter = new NoScrollGridAdapter(CheckDetailActivity.this, imageUrls);//重新绑定一次adapter
                    noScrollGridVieww.setAdapter(noScrollGridAdapter);
                    noScrollGridAdapter.notifyDataSetChanged();//刷新gridview
                }


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);



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
    /**
     * 初始化数据
     */
    private void initData() {
        itemEntities = new ArrayList<ItemEntity>();

        // 4.6张图片
        ArrayList<ImagesBean> urls_3 = new ArrayList<ImagesBean>();
        urls_3.add(new ImagesBean("defaultImage",""));
      /*     urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg"));*/
   //    urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
        ItemEntity entity4 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg", "赵六", "今天下雨了...", urls_3);
        itemEntities.add(entity4);
    }
    protected void showNormalDialogIsDel(String title,String context,final int position){
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


                        delete(position);
                        noScrollGridAdapter = new NoScrollGridAdapter(CheckDetailActivity.this, imageUrls);//重新绑定一次adapter
                        noScrollGridVieww.setAdapter(noScrollGridAdapter);
                        noScrollGridAdapter.notifyDataSetChanged();//刷新gridview

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
    private void delete(int position) {//删除选中项方法
        ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
        boolean isDefult=false;
        imageUrls.remove(position);
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
    }
    private void ShowPickDialog() {
        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(CheckDetailActivity.this,
                                ImageChooseActivity.class);
                        Bundle bundle = new Bundle();
                        final ArrayList<String> url2=new ArrayList<String>();
                        for(ImagesBean im:imageUrls){
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
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory()+"/sgin/",
                                        photoFileName + ".jpg")));
                        startActivityForResult(intent, 998);
                    }
                }).show();
    }
}
