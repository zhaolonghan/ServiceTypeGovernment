package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.view.ChildLiistView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckQuestionAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ListView listView;
    public static  View listView1;
    public CheckQuestionAdspter(Context context, List<Map<String, Object>> data, ListView listView){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.listView=listView;
    }
    public final class Zujian{
        public TextView id;
        public TextView title;
        public TextView time;
        public TextView context;
        public ChildLiistView childListView;

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
                convertView=layoutInflater.inflate(R.layout.item_check_detail_one, null);
            //    zujian.childListView = (ChildLiistView) convertView.findViewById(R.id.check_question);
           zujian.childListView.setAdapter(new CheckQuestionChidAdspter(context,data,listView));
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView1=view;
            }
        });

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
//    public void updataView(View convertView,final List<Map<String,Object>> data,ArrayList<ImagesBean> imagesBean) {
//        Zujian holder = (Zujian) convertView.getTag();
//        CheckQuestionChidAdspter childAdapter = new CheckQuestionChidAdspter(context, data,imagesBean);
//        holder.childListView.setAdapter(childAdapter);
//        childAdapter.notifyDataSetInvalidated();
//    }
}
