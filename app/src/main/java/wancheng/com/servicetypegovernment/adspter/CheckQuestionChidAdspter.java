package wancheng.com.servicetypegovernment.adspter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckDetailActivity;
import wancheng.com.servicetypegovernment.activity.ImageChooseActivity;
import wancheng.com.servicetypegovernment.activity.ImagePagerActivity;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.ItemEntity;
import wancheng.com.servicetypegovernment.view.NoScrollGridView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckQuestionChidAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    public ArrayList<ImagesBean> imageUrls;

    public NoScrollGridView noScrollGridVieww;
    public ArrayList<ImagesBean> newList;
    private LayoutInflater layoutInflater;
    private Context context;
    public NoScrollGridAdapter noScrollGridAdapter;
    public String photoFileName;

    public ItemEntity itemEntity;
    public ArrayList<ItemEntity> itemEntities=new ArrayList<ItemEntity>();
    public int type=1;
    public CheckQuestionChidAdspter(Context context, List<Map<String, Object>> data){
        this.context=context;
       /// this.itemEntities=itemEntities;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public final class Zujian{
        public TextView id;
        public TextView title;
        public TextView time;
        public TextView context;;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件

                convertView=layoutInflater.inflate(R.layout.item_check_detail_two, null);
               initData();
            itemEntity=itemEntities.get(3);
             imageUrls = itemEntity.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
           noScrollGridVieww.setVisibility(View.GONE);


        } else {
            noScrollGridAdapter=new NoScrollGridAdapter(context,imageUrls);
            noScrollGridVieww.setAdapter(noScrollGridAdapter);

        }
        // 点击回帖九宫格，查看大图
        noScrollGridVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(!(imageUrls.get(position).getType().equals("defaultImage"))){
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
                if (!imageUrls.get(position).getType().equals("defaultImage")) {
                    showNormalDialogIsDel("提示", "是否确定删除此图片？", position);
                }
                return true;
            }
        });

/*            zujian.id=(TextView)convertView.findViewById(R.id.newsid);
            zujian.title=(TextView)convertView.findViewById(R.id.newslist1);
            zujian.time=(TextView)convertView.findViewById(R.id.tv_time);
            zujian.context=(TextView)convertView.findViewById(R.id.tv_content);*/
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }


        return convertView;
    }
    public void add(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new LinkedList<>();
        }
        data.addAll(datas);
        //删除的话用remove
        notifyDataSetChanged();
    }
    public void update(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new LinkedList<>();
        }

        data=datas;
        //删除的话用remove
        notifyDataSetChanged();
    }
    /**
     * 初始化数据
     */
    private void initData() {
        itemEntities = new ArrayList<ItemEntity>();

        // 4.6张图片
        ArrayList<ImagesBean> urls_3 = new ArrayList<ImagesBean>();
        urls_3.add(new ImagesBean("defaultImage", ""));
      /*     urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg"));*/
        //    urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
        ItemEntity entity4 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg", "赵六", "今天下雨了...", urls_3);
        itemEntities.add(entity4);
    }
    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    protected void showNormalDialogIsDel(String title,String mesage,final int position){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(mesage);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        delete(position);
                        noScrollGridAdapter = new NoScrollGridAdapter(context, imageUrls);//重新绑定一次adapter
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
        new AlertDialog.Builder(context)
                .setTitle("选择图片")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(context,
                                ImageChooseActivity.class);
                        Bundle bundle = new Bundle();
                        final ArrayList<String> url2=new ArrayList<String>();
                        for(ImagesBean im:imageUrls){
                            if (im.getType().equals("localImage")){
                                url2.add(im.getPath());
                            }
                        }
                        bundle.putStringArrayList("listurl", url2);
                        bundle.putInt("listsize", imageUrls.size() - 1);
                        intent.putExtra("bundle", bundle);
                       // (Activity)context.startActivityForResult(intent, 999);
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
                                        .getExternalStorageDirectory() + "/sgin/",
                                        photoFileName + ".jpg")));
                    //    startActivityForResult(intent, 998);
                    }
                }).show();
    }
}
