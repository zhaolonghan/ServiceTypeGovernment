package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.util.JSONUtils;

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
        public TextView result;
        public TextView type;
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
        history history=null;
        if(convertView==null){
            history=new history();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.item_check_history, null);
            history.history_time = (TextView) convertView.findViewById(R.id.history_time);
            history.result= (TextView) convertView.findViewById(R.id.tv_left1);
            history.type= (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(history);
        }else{
            history=(history)convertView.getTag();
        }

            history.history_time.setText(data.get(i).get("history_time").toString());
            if("1".equals(data.get(i).get("result").toString())){
                history.result.setText("合");
                history.result.setTextColor(convertView.getResources().getColor(R.color.green));
                history.result.setBackground(convertView.getResources().getDrawable(R.drawable.shape_round_textviewgreen));
            }else  if("2".equals(data.get(i).get("result").toString())){
                history.result.setText("基");
                history.result.setTextColor(convertView.getResources().getColor(R.color.orange));
                history.result.setBackground(convertView.getResources().getDrawable(R.drawable.shape_round_textvieworage));

            }else{
                history.result.setText("不");
                history.result.setTextColor(convertView.getResources().getColor(R.color.red));
                history.result.setBackground(convertView.getResources().getDrawable(R.drawable.shape_round_textviewred));

            }
            history.type.setText(data.get(i).get("type").toString());

           /* contextmap.put("id", JSONUtils.getString(dataobject, "resultId", ""));
            contextmap.put("history_time", DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(JSONUtils.getString(dataobject, "time", "0")))));
            contextmap.put("result",JSONUtils.getString(dataobject, "result", ""));
            contextmap.put("type",JSONUtils.getString(dataobject, "type", ""));
            contextmap.put("result", JSONUtils.getString(dataobject, "result", ""));*/
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
