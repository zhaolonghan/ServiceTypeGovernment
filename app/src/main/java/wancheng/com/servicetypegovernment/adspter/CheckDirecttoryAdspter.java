package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        public TextView isshow;
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
                zujian.childListView = (ChildLiistView) convertView.findViewById(R.id.check_info);
                zujian.isshow= (TextView) convertView.findViewById(R.id.isshow);
            }


            convertView.setTag(zujian);
        }else{
            zujian=(Directtory)convertView.getTag();
        }
        if(type==0) {
            zujian.directtory_tittle.setText(data.get(i).get("directtory_tittle").toString());
            zujian.childListView.setAdapter(new CheckDirecttoryChidAdspter(context, (List<Map<String, Object>>) data.get(i).get("infolist")));
           final String remark=data.get(i).get("directtory_remark").toString();
            final View  onconvertView=convertView;
            if(remark!=null&&remark.length()>0){
                zujian.isshow.setVisibility(View.VISIBLE);
                zujian.isshow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showNormalDialog(onconvertView, remark);
                    }
                });

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
    protected void showNormalDialog(View convertView,String remark){
        LayoutInflater inflater =LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_mystarsdialog,  (ViewGroup)  convertView.findViewById(R.id.dialog));
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        TextView tipview=(TextView)layout.findViewById(R.id.isTip);
        tipview.setText(remark);
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setView(layout);
        normalDialog.setTitle("注意事项");
       // normalDialog.setMessage(data.get(i).get("directtory_remark").toString());
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        normalDialog.show();
    }
}
