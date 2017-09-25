package wancheng.com.servicetypegovernment.adspter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ListView;
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
import wancheng.com.servicetypegovernment.view.ViewHolder;

/**
 * Created by john on 2017/8/17.
 */
public class CheckQuestionChidAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private static ListView listView;
    private Context context;
    private ArrayList<ImagesBean> imageUrls;
    private ArrayList<ImagesBean>  newList;
    private String photoFileName;
    private NoScrollGridView noScrollGridVieww;
    private static NoScrollGridAdapter noScrollGridAdapter;
    public CheckQuestionChidAdspter(Context context, List<Map<String, Object>> data,ListView listView){
        this.context=context;
        /// this.itemEntities=itemEntities;
        this.data=data;
        this.listView=listView;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public final class Zujian{
        private TextView zhinan;
        private View layout;
        private NoScrollGridView noScrollGridVieww;
        private NoScrollGridAdapter noScrollGridAdapter;
        private ArrayList<ImagesBean> imageUrls;

    }

    public CheckQuestionChidAdspter() {
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
        final int index=i;
       final Zujian zujian;

        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件

            convertView=layoutInflater.inflate(R.layout.item_check_detail_two, null);
            zujian.noScrollGridVieww=(NoScrollGridView)convertView.findViewById(R.id.gridview);
            zujian.layout = layoutInflater.inflate(R.layout.layout_mydialog,
                    (ViewGroup) convertView.findViewById(R.id.dialog));
            zujian.zhinan=(TextView)convertView.findViewById(R.id.tv_zhinan);
            zujian.zhinan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            final View layout=zujian.layout;
            zujian.zhinan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showNormalDialog(layout);
                }
            });
           imageUrls=new ArrayList<ImagesBean>();
            imageUrls=(ArrayList<ImagesBean>)data.get(i).get("imageUrls");
            if (imageUrls == null ||imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
                zujian.noScrollGridVieww.setVisibility(View.GONE);


            } else {
                zujian.noScrollGridAdapter=new NoScrollGridAdapter(context,imageUrls);
                zujian.noScrollGridVieww.setAdapter(zujian.noScrollGridAdapter);

            }
            // 点击回帖九宫格，查看大图
            zujian.noScrollGridVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

                        ShowPickDialog(index);
                        //Log.e("要更新的item索引值是2：", index + "");
                    }
                }
            });
            noScrollGridVieww=zujian.noScrollGridVieww;
            noScrollGridAdapter=zujian.noScrollGridAdapter;
            zujian.noScrollGridVieww.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!imageUrls.get(position).getType().equals("defaultImage")) {
                        showNormalDialogIsDel("提示", "是否确定删除此图片？", position,imageUrls,noScrollGridAdapter,noScrollGridVieww);
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
    protected void showNormalDialog(View layout){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        ViewGroup parent = (ViewGroup) layout.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
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

    protected void showNormalDialogIsDel(String title,String mesage,final int position,final  ArrayList<ImagesBean> imageUrls,final NoScrollGridAdapter noScrollGridAdapter,final NoScrollGridView noScrollGridView){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        this.noScrollGridAdapter=noScrollGridAdapter;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(mesage);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        delete(position,imageUrls);
                        CheckQuestionChidAdspter.noScrollGridAdapter = new NoScrollGridAdapter(context, imageUrls);//重新绑定一次adapter
                        noScrollGridView.setAdapter(noScrollGridAdapter);
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
    private void delete(int position,ArrayList<ImagesBean> imageUrls) {//删除选中项方法
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
    private void ShowPickDialog(final int index) {
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
                        bundle.putSerializable("index", index);
                        bundle.putStringArrayList("listurl", url2);
                        bundle.putInt("listsize", imageUrls.size() - 1);
                        intent.putExtra("bundle", bundle);
                        ((Activity)context).startActivityForResult(intent, 999);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
//                        CheckDetailActivity.photoFileName = System.currentTimeMillis() + ((Math.random() * 9 + 1) * 1000) + "";
//                        Log.e("TAG时间1",CheckDetailActivity.photoFileName);
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory() + "/sgin/",
                                        photoFileName + ".jpg")));
                        ((Activity)context).startActivityForResult(intent, 998);
                    }
                }).show();
    }
    public void updataView(int index,View view,ArrayList<String> list,Context context) {
        //Log.e("list size" ,list.size()+"");

        this.layoutInflater=LayoutInflater.from(context);
        final int index1=index;
        Zujian zujian=null;
         view = CheckQuestionAdspter.listView1;
        if(view==null){
            zujian=new Zujian();
            //获得组件，实例化组件

            view=layoutInflater.inflate(R.layout.item_check_detail_two, null);
            zujian.noScrollGridVieww=(NoScrollGridView)view.findViewById(R.id.gridview);
            zujian.layout = layoutInflater.inflate(R.layout.layout_mydialog,
                    (ViewGroup) view.findViewById(R.id.dialog));
            zujian.zhinan=(TextView)view.findViewById(R.id.tv_zhinan);
            zujian.zhinan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }else{
             zujian = (Zujian) view.getTag();

        }
        imageUrls=new ArrayList<ImagesBean>();
        for(String s:list){
            imageUrls.add(new ImagesBean("localImage",s));
        }

        //Log.e("imageUrls size" ,imageUrls.size()+"");
        zujian.noScrollGridVieww.setAdapter(zujian.noScrollGridAdapter);
        zujian.noScrollGridAdapter=new NoScrollGridAdapter(context,imageUrls);
        zujian.noScrollGridAdapter.notifyDataSetChanged();


//        View view=null;
//        //得到第一个可显示控件的位置，
//        int visiblePosition = listView.getFirstVisiblePosition();
//        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
//        if (index - visiblePosition >=0 ) {
//            //得到要更新的item的view
//
//            //调用adapter更新界面
//          //  mAdapter.updateView(view, itemIndex);
//        }
//        view = listView.getChildAt(1);
//        Log.e("要更新的item索引值是：",index+"");
//        view.getTag();
//        ArrayList<ImagesBean> list1 = new ArrayList<ImagesBean>();
//        imageUrls.remove(imageUrls.size() - 1);
//        for(int i=0;i<imageUrls.size();i++){
//            String type=imageUrls.get(i).getType();
//            if("localImage".equals(type)){
//                imageUrls.remove(i);
//                --i;
//            }
//        }
//        for(String s:list){
//            imageUrls.add(new ImagesBean("localImage",s));
//        }
//        if(imageUrls.size()<5){
//            imageUrls.add(new ImagesBean("defaultImage", ""));
//        }
//        holder.noScrollGridAdapter=new NoScrollGridAdapter(context,imageUrls);
//        holder.noScrollGridVieww.setAdapter(holder.noScrollGridAdapter);
//        holder.noScrollGridAdapter.notifyDataSetInvalidated();
    }

}
