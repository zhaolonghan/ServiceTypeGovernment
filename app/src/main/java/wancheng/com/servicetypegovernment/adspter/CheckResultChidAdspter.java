package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.ImageChooseActivity;
import wancheng.com.servicetypegovernment.activity.ImagePagerActivity;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.ItemEntity;
import wancheng.com.servicetypegovernment.view.NoScrollGridView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckResultChidAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;

    private LayoutInflater layoutInflater;
    private Context context;

    public int type=1;
    public CheckResultChidAdspter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;

        this.layoutInflater=LayoutInflater.from(context);
    }
    private final class Zujian{
        private TextView id;
        private TextView tv_left3;
        private ImageView tv_result3;
        private LinearLayout detail_remark;
        private LinearLayout detail_image;
        private TextView detail_info;
        private NoScrollGridView noScrollGridView;
        private NoScrollGridResultAdapter noScrollGridAdapter;
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
        if(data!=null&&data.size()>0){

        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件
           convertView=layoutInflater.inflate(R.layout.item_check_result_detail_two, null);
            zujian.detail_info = (TextView) convertView.findViewById(R.id.detail_info);
            zujian.detail_image = (LinearLayout) convertView.findViewById(R.id.detail_image);
            zujian.detail_remark = (LinearLayout) convertView.findViewById(R.id.detail_remark);
            zujian.tv_result3= (ImageView) convertView.findViewById(R.id.tv_result3);
            zujian.tv_left3=(TextView) convertView.findViewById(R.id.tv_left3);
            zujian.noScrollGridView=(NoScrollGridView) convertView.findViewById(R.id.gridview);
           /* String isstatus= data.get(i).get("tv_result3").toString();
            if("0".equals(isstatus)){
                //图片否
                if(data.get(i).get("detail_image")!=null){
                    ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)data.get(i).get("detail_image");
                    Log.e("图片数量",imageUrls.size()+"");
                    getImageGridViews(imageUrls, zujian.linearLayout);
                }
            }*/
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }

        zujian.detail_info.setText(data.get(i).get("detail_info")!=null?data.get(i).get("detail_info").toString():"");
        if(data.get(i).get("isPoint")!=null&&"1".equals(data.get(i).get("isPoint").toString())){
            zujian.tv_left3.setVisibility(View.VISIBLE);
        }
        ((TextView)zujian.detail_remark.getChildAt(1)).setText(data.get(i).get("remarks").toString());
        String isstatus= data.get(i).get("tv_result3").toString();
        if("0".equals(isstatus)){
            //图片否
            zujian. tv_result3.setImageResource(R.drawable.fou);
            zujian.detail_remark.setVisibility(View.VISIBLE);
            zujian.detail_image.setVisibility(View.VISIBLE);
            if(data.get(i).get("detail_image")!=null){
                final ArrayList<ImagesBean> imageUrls=(ArrayList<ImagesBean>)data.get(i).get("detail_image");
                if (imageUrls == null ||imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
                    zujian.noScrollGridView.setVisibility(View.GONE);


                } else {
                    zujian.noScrollGridAdapter=new NoScrollGridResultAdapter(context,imageUrls);
                    zujian.noScrollGridView.setAdapter(zujian.noScrollGridAdapter);

                }
                // 点击回帖九宫格，查看大图
                zujian.noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, imageUrls);
                    }
                });
                //Log.e("图片数量",imageUrls.size()+"");
               // getImageGridViews(imageUrls, zujian.linearLayout);
            }
        }else if("2".equals(isstatus)){
            //合理
            zujian. tv_result3.setImageResource(R.drawable.heli);
            zujian.detail_remark.setVisibility(View.GONE);
            zujian.detail_image.setVisibility(View.GONE);
        }else{
            //通过
            zujian. tv_result3.setImageResource(R.drawable.shi);
            zujian.detail_remark.setVisibility(View.GONE);
            zujian.detail_image.setVisibility(View.GONE);
        }

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


            lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageBrower(index, imageUrls);
                }
            });

            linearLayout.addView(lin);

        }
    }
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

}
