package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
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
public class CheckDirecttoryAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private int type;
    private CheckDirecttoryChidAdspter childAdapter;
    public CheckDirecttoryAdspter(Context context, List<Map<String, Object>> data, int type){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.type=type;
    }
    public final class Directtory{
        public TextView id;
        public TextView directtory_tittle;
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
        Directtory zujian=null;
        if(convertView==null){
            zujian=new Directtory();

            // this.childAdapter  = new CheckDirecttoryChidAdspter(context,data);
            //获得组件，实例化组件
            if(type==0){
                convertView=layoutInflater.inflate(R.layout.item_check_directtory_one, null);
                zujian.directtory_tittle = (TextView) convertView.findViewById(R.id.directtory_tittle);
                zujian.directtory_tittle.setText(data.get(i).get("directtory_tittle").toString());
                zujian.childListView = (ChildLiistView) convertView.findViewById(R.id.check_info);
                zujian.childListView.setAdapter(new CheckDirecttoryChidAdspter(context,(List<Map<String, Object>>) data.get(i).get("infolist")));
            }


            convertView.setTag(zujian);
        }else{
            zujian=(Directtory)convertView.getTag();
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
