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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;

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
    public NewsAdspter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);

        Log.e("datasize", data.size()+"");
    }
    public final class Zujian{
        public TextView id;
        public TextView title;
        public TextView time;
        public TextView context;
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
            convertView=layoutInflater.inflate(R.layout.item_news, null);
            zujian.id=(TextView)convertView.findViewById(R.id.newsid);
            zujian.title=(TextView)convertView.findViewById(R.id.newslist1);
            zujian.time=(TextView)convertView.findViewById(R.id.tv_time);
            zujian.context=(TextView)convertView.findViewById(R.id.tv_content);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }

        //绑定数据
        id=data.get(i).get("id")+"";
        zujian.id.setText(data.get(i).get("id")+"");
        zujian.title.setText(data.get(i).get("title").toString());
        zujian.time.setText(data.get(i).get("time").toString());
        zujian.context.setText(data.get(i).get("context").toString());
       /* if(convertView!=null) ((RelativeLayout)convertView.findViewById(R.id.newsid).getParent()).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //打开详情
                Intent intent = new Intent();
                intent.putExtra("id", id);
                Log.e("no", "定位失败");
                intent.setClass(context, IndexActivity.class);
                context.startActivity(intent);

            }
        });*/
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
