package wancheng.com.servicetypegovernment.adspter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        if(data!=null&&data.size()>0){


        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();

           // this.childAdapter  = new CheckQuestionChidAdspter(context,data);
            //获得组件，实例化组件
            if(type==0){
                convertView=layoutInflater.inflate(R.layout.item_check_result_detail_one, null);
            }
            zujian.detail_title = (TextView) convertView.findViewById(R.id.detail_title);
            zujian.isshow = (TextView) convertView.findViewById(R.id.remark);;
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        zujian.childListView = (ChildLiistView) convertView.findViewById(R.id.check_question);
        zujian.detail_title.setText(data.get(i).get("detail_title").toString());


        if(data.get(i).get("infolist")!=null){
            final CheckResultChidAdspter adapter=new CheckResultChidAdspter(context, (List<Map<String, Object>>) data.get(i).get("infolist"));

            zujian.childListView.setAdapter(adapter);

        }
        if(data.get(i).get("remark")!=null){
            final String remark=data.get(i).get("detail_remarks").toString();
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
