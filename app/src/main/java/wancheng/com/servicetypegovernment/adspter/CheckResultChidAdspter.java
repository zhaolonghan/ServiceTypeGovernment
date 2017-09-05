package wancheng.com.servicetypegovernment.adspter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        private TextView detail_info;
        private ImageView tv_result3;
        private LinearLayout detail_remark;
        private LinearLayout detail_image;


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

           convertView=layoutInflater.inflate(R.layout.item_check_result_detail_two, null);

            zujian.detail_info = (TextView) convertView.findViewById(R.id.detail_info);
            zujian.detail_info.setText(data.get(i).get("detail_info").toString());

            zujian.detail_image = (LinearLayout) convertView.findViewById(R.id.detail_image);
            zujian.detail_remark = (LinearLayout) convertView.findViewById(R.id.detail_remark);
           // zujian.detail_remark.setText(data.get(i).get("detail_remark").toString());
            ((TextView)zujian.detail_remark.getChildAt(1)).setText(data.get(i).get("detail_remark").toString());
            String isstatus= data.get(i).get("tv_result3").toString();
            zujian. tv_result3= (ImageView) convertView.findViewById(R.id.tv_result3);
            if("1".equals(isstatus)){
                //图片不变

                 zujian.detail_remark.setVisibility(View.GONE);
                zujian.detail_image.setVisibility(View.GONE);

            }else if("2".equals(isstatus)){
               //图片是
                zujian. tv_result3.setImageResource(R.drawable.shi);
                zujian.detail_remark.setVisibility(View.GONE);
                zujian.detail_image.setVisibility(View.GONE);
            }else{
                //图片否
                zujian. tv_result3.setImageResource(R.drawable.fou);

                Log.e("isstatus1", isstatus);
               zujian.detail_remark.setVisibility(View.VISIBLE);

                Log.e("isstatus2", isstatus);
                zujian.detail_image.setVisibility(View.VISIBLE);
                Log.e("isstatus3", isstatus);
            }




          /*  zujian.detail_image = (TextView) convertView.findViewById(R.id.detail_info);
            zujian.detail_info.setText(((List<Map<String, Object>>) data.get(i).get("tv_result3")).get(i).get("detail_info").toString());

            zujian.detail_info = (TextView) convertView.findViewById(R.id.detail_info);
            zujian.detail_info.setText(((List<Map<String, Object>>) data.get(i).get("tv_result3")).get(i).get("detail_info").toString());*/


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

}
