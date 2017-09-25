package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;

import wancheng.com.servicetypegovernment.activity.ContextDetailActivity;
import wancheng.com.servicetypegovernment.activity.IndexActivity;
import wancheng.com.servicetypegovernment.activity.NewsInfoActivity;

/**
 * Created by john on 2017/8/17.
 */
public class NewsAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    public  static List<TextView>  veiwlist=new ArrayList<TextView>();;

    public NewsAdspter(Context context, List<Map<String, Object>> data){
        super();
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public final class Zujian{
        public TextView tv_day  ;
        public TextView tv_year;
        public TextView tv_content_title;
        public TextView tv_count;
        public TextView tv_source;
        public TextView tv_url;
        public TextView tv_count_read;
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
        if(data!=null){
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.item_news, null);
            zujian.tv_day=(TextView)convertView.findViewById(R.id.tv_day);
            veiwlist.add(zujian.tv_day);

            zujian.tv_year=(TextView)convertView.findViewById(R.id.tv_year);
            zujian.tv_content_title=(TextView)convertView.findViewById(R.id.tv_content_title);
            zujian.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
            zujian.tv_source=(TextView)convertView.findViewById(R.id.tv_source);
            zujian.tv_count_read=(TextView)convertView.findViewById(R.id.tv_count_read);

            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }

        //绑定数据

        zujian.tv_day.setText(data.get(i).get("day").toString());
        zujian.tv_year.setText(data.get(i).get("year").toString());
        zujian.tv_content_title.setText(data.get(i).get("title").toString());
        if("已读".equals(data.get(i).get("count").toString())||"未读".equals(data.get(i).get("count").toString())){
            zujian.tv_count.setVisibility(View.GONE);
            zujian.tv_count.setText(data.get(i).get("count").toString());
            zujian.tv_count_read.setText(data.get(i).get("count").toString());
            if("未读".equals(data.get(i).get("count").toString())){
                zujian.tv_count_read.setTextColor(context.getResources().getColor(R.color.red));
            }
        }else{
            zujian.tv_count.setVisibility(View.VISIBLE);
            zujian.tv_count_read.setText("阅读");
            zujian.tv_count.setText(data.get(i).get("count").toString());
        }
        zujian.tv_source.setText(data.get(i).get("source").toString());
          final  String url=  data.get(i).get("url").toString();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("title", "详情");
                    intent.putExtra("url",url);
                    intent.setClass(context, ContextDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
    public boolean updataView(View view,int count){
        boolean flag=false;
        if(view!=null){
            Zujian holder=new Zujian();
            holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
            holder.tv_count.setText(count+"");
            flag=true;
        }
        return flag;
    }

    public void add(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new LinkedList<>();
        }
        if (data == null) {
            data = new LinkedList<>();
        }
        data.addAll(datas);
        //删除的话用remove
        notifyDataSetChanged();
    }
    public void update(List<Map<String, Object>> datas){
        if (datas == null) {
            datas = new LinkedList<>();
        }
        if (data == null) {
            data = new LinkedList<>();
        }
        data=datas;
        //删除的话用remove
        notifyDataSetChanged();
    }
    public TextView getOtemView(int i){
        if(veiwlist!=null){
        }

        if(veiwlist==null||veiwlist.size()==0){
            return null;
        }else{
            return  veiwlist.get(i);
        }
    }
}
