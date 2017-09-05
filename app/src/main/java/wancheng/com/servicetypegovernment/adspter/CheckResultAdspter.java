package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.view.ChildLiistView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckResultAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private int type;
    private CheckResultChidAdspter childAdapter;
    public CheckResultAdspter(Context context, List<Map<String, Object>> data, int type){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.type=type;
    }
    public final class Zujian{
        public TextView id;
        public TextView detail_title;

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

           // this.childAdapter  = new CheckQuestionChidAdspter(context,data);
            //获得组件，实例化组件
            if(type==0){
                convertView=layoutInflater.inflate(R.layout.item_check_result_detail_one, null);
               zujian.childListView = (ChildLiistView) convertView.findViewById(R.id.check_question);
                Log.e("11111111111111111", ((List<Map<String, Object>>) data.get(i).get("infolist")).size() + "");
                final CheckResultChidAdspter adapter=new CheckResultChidAdspter(context, (List<Map<String, Object>>) data.get(i).get("infolist"));

                zujian.childListView.setAdapter(adapter);
            }
            zujian.detail_title = (TextView) convertView.findViewById(R.id.detail_title);
            zujian.detail_title.setText(data.get(i).get("detail_title").toString());

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
