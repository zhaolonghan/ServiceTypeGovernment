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
import wancheng.com.servicetypegovernment.util.ChildLiistView;

/**
 * Created by john on 2017/8/17.
 */
public class CheckHistoryAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private CheckResultChidAdspter childAdapter;
    public CheckHistoryAdspter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public final class history{
        public TextView id;
        public TextView history_time;
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
        history history=null;
        if(convertView==null){
            history=new history();

           // this.childAdapter  = new CheckQuestionChidAdspter(context,data);
            //获得组件，实例化组件
                convertView=layoutInflater.inflate(R.layout.item_check_history, null);

            history.history_time = (TextView) convertView.findViewById(R.id.history_time);
            history.history_time.setText(data.get(i).get("history_time").toString());

            convertView.setTag(history);
        }else{
            history=(history)convertView.getTag();
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
