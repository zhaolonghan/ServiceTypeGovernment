package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import wancheng.com.servicetypegovernment.activity.ImageChooseActivity;
import wancheng.com.servicetypegovernment.activity.ImagePagerActivity;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.ItemEntity;
import wancheng.com.servicetypegovernment.view.NoScrollGridView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckDirecttoryChidAdspter extends BaseAdapter
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
    public CheckDirecttoryChidAdspter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public final class Directtory{
        public TextView id;
        public TextView directtory_info_tittle;
        public TextView directtory_info_laws;
        public TextView directtory_info_way;;
        public TextView directtory_info_explain;;
        public View  isred;

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
        Directtory zujian=null;
        if(convertView==null){
            zujian=new Directtory();
            convertView=layoutInflater.inflate(R.layout.item_check_directtory_two, null);
            zujian.directtory_info_tittle = (TextView) convertView.findViewById(R.id.directtory_info_tittle);
            zujian.directtory_info_laws = (TextView) convertView.findViewById(R.id.directtory_info_laws);
            zujian.directtory_info_way = (TextView) convertView.findViewById(R.id.directtory_info_way);
            zujian.directtory_info_explain = (TextView) convertView.findViewById(R.id.directtory_info_explain);
            zujian.isred= convertView.findViewById(R.id.isred);
            convertView.setTag(zujian);
        }else{
            zujian=(Directtory)convertView.getTag();
        }

        zujian.directtory_info_tittle.setText(data.get(i).get("directtory_info_tittle").toString());
        zujian.directtory_info_laws.setText(data.get(i).get("directtory_info_laws").toString());
        zujian.directtory_info_way.setText(data.get(i).get("directtory_info_way").toString());
        zujian.directtory_info_explain.setText(data.get(i).get("directtory_info_explain").toString());
        String isPoint=data.get(i).get("isPoint").toString();
        if("1".equals(isPoint)){
            zujian.isred.setVisibility(View.VISIBLE);
        }
        //isPoint
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

    }

}
