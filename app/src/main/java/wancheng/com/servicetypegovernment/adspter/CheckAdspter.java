package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckHistoryListActivity;
import wancheng.com.servicetypegovernment.activity.CheckOrderActivity;
import wancheng.com.servicetypegovernment.activity.CheckResultDetailActivity;
import wancheng.com.servicetypegovernment.activity.CompanyCheckListActivity;
import wancheng.com.servicetypegovernment.activity.CompanyDetailActivity;
import wancheng.com.servicetypegovernment.activity.InformActivity;

/**
 * Created by john on 2017/8/17.
 */
public class CheckAdspter extends BaseAdapter
{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private String id;
    private int typeadapter;//0企业列表  1执法检查 3问题处置
    public CheckAdspter(Context context, List<Map<String, Object>> data,int typeadapter){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.typeadapter=typeadapter;
    }
    public final class Zujian{
        public TextView id;
        //企业列表
        public TextView corp_name;
        public TextView corp_code;
        public TextView corp_person;
        public TextView corp_tel;
        public TextView corp_address;
        //执法检查
        public TextView check_date;
        public TextView check_corpnum;
        public TextView check_status;
        public TextView check_numed;
        public TextView check_numing;
        public TextView check_numthrought;
        public TextView check_numunthrought;
        public TextView check_radioing;
        public TextView check_radiothrought;
        //问题处置
        public TextView question_corpname;
        public TextView question_no;
        public TextView question_date;
        public TextView question_result;
        public TextView question_management;
        public TextView question_status;
        public TextView question_limit;
    }
    public final class corpZujian{
        public TextView id;


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
            int [] items={R.layout.item_checkcorp,R.layout.item_checkenforcement,R.layout.item_checkquestion};
            convertView=layoutInflater.inflate(items[typeadapter], null);
            if(typeadapter==0){
                zujian= corpzhujianAdd(zujian,convertView,i);
            }
            if(typeadapter==1){
               zujian= enforcementzhujianAdd(zujian,convertView,i);
            }
            if(typeadapter==2){
                 zujian= questionzhujianAdd(zujian, convertView, i);
            }
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
    public Zujian corpzhujianAdd(Zujian zujian, View convertView,int i) {
        // corpZujian.id=(TextView)convertView.findViewById(R.id.newsid);
        zujian.corp_name = (TextView) convertView.findViewById(R.id.corp_name);
        zujian.corp_code = (TextView) convertView.findViewById(R.id.corp_code);
        zujian.corp_person = (TextView) convertView.findViewById(R.id.corp_person);
        zujian.corp_tel = (TextView) convertView.findViewById(R.id.corp_tel);
        zujian.corp_address = (TextView) convertView.findViewById(R.id.corp_address);

        //  zujian.id.setText(data.get(i).get("id")+"");
        zujian.corp_name.setText(data.get(i).get("corp_name").toString());
        zujian.corp_code.setText(data.get(i).get("corp_code").toString());
        zujian.corp_person.setText(data.get(i).get("corp_person").toString());
        zujian.corp_tel.setText(data.get(i).get("corp_tel").toString());
        zujian.corp_address.setText(data.get(i).get("corp_address").toString());

        Button bt_history = (Button) convertView.findViewById(R.id.bt_history);
        bt_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(context, " 跳转历史检查列表页面", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent();
                intent.setClass(context, CheckHistoryListActivity.class);
                context.startActivity(intent);
            }
        });
        Button bt_check = (Button) convertView.findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(context, " 跳转告知页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(context, InformActivity.class);
                context.startActivity(intent);
            }
        });
        Button btDetail = (Button) convertView.findViewById(R.id.bt_detail);
        btDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, CompanyDetailActivity.class);
                context.startActivity(intent);
            }
        });
        Button btStartCheck = (Button) convertView.findViewById(R.id.bt_check);
        btStartCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, InformActivity.class);
                context.startActivity(intent);

            }
        });
        return  zujian;
    }




    public Zujian enforcementzhujianAdd(Zujian zujian, View convertView,int i) {

        // corpZujian.id=(TextView)convertView.findViewById(R.id.newsid);
       zujian.check_date = (TextView) convertView.findViewById(R.id.check_date);
        zujian.check_corpnum = (TextView) convertView.findViewById(R.id.check_corpnum);
        zujian.check_status = (TextView) convertView.findViewById(R.id.check_status);
        zujian.check_numed = (TextView) convertView.findViewById(R.id.check_numed);
        zujian.check_numing = (TextView) convertView.findViewById(R.id.check_numing);
        zujian.check_numthrought = (TextView) convertView.findViewById(R.id.check_numthrought);
        zujian.check_numunthrought = (TextView) convertView.findViewById(R.id.check_numunthrought);
        zujian.check_radioing = (TextView) convertView.findViewById(R.id.check_radioing);
        zujian.check_radiothrought = (TextView) convertView.findViewById(R.id.check_radiothrought);

        //  zujian.id.setText(data.get(i).get("id")+"");
        zujian.check_date.setText(data.get(i).get("check_date").toString());
        zujian.check_corpnum.setText(data.get(i).get("check_corpnum").toString());
        zujian.check_status.setText(data.get(i).get("check_status").toString());
        zujian.check_numed.setText(data.get(i).get("check_numed").toString());
        zujian.check_numing.setText(data.get(i).get("check_numing").toString());
        zujian.check_numthrought.setText(data.get(i).get("check_numthrought").toString());
        zujian.check_numunthrought.setText(data.get(i).get("check_numunthrought").toString());
        zujian.check_radioing.setText(data.get(i).get("check_radioing").toString());
        zujian.check_radiothrought.setText(data.get(i).get("check_radiothrought").toString());

        Button btStartCheck = (Button) convertView.findViewById(R.id.bt_start_check);
        btStartCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, CompanyCheckListActivity.class);
                context.startActivity(intent);
            }
        });
        return  zujian;
    }
    public Zujian questionzhujianAdd(Zujian zujian, View convertView,int i) {
        // corpZujian.id=(TextView)convertView.findViewById(R.id.newsid);

       zujian.question_corpname = (TextView) convertView.findViewById(R.id.question_corpname);
        zujian.question_no = (TextView) convertView.findViewById(R.id.question_no);
        zujian.question_date = (TextView) convertView.findViewById(R.id.question_date);
        zujian.question_result = (TextView) convertView.findViewById(R.id.question_result);
        zujian.question_management = (TextView) convertView.findViewById(R.id.question_management);
        zujian.question_status = (TextView) convertView.findViewById(R.id.question_status);
        zujian.question_limit = (TextView) convertView.findViewById(R.id.question_limit);

        //  zujian.id.setText(data.get(i).get("id")+"");
        zujian.question_corpname.setText(data.get(i).get("question_corpname").toString());
        zujian.question_no.setText(data.get(i).get("question_no").toString());
        zujian.question_date.setText(data.get(i).get("question_date").toString());
        zujian.question_result.setText(data.get(i).get("question_result").toString());
        zujian.question_management.setText(data.get(i).get("question_management").toString());
        zujian.question_status.setText(data.get(i).get("question_status").toString());
        zujian.question_limit.setText(data.get(i).get("question_limit").toString());

        Button btStartCheck = (Button) convertView.findViewById(R.id.bt_check_detail);
        btStartCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, CheckResultDetailActivity.class);
                context.startActivity(intent);
            }
        });
        return  zujian;
    }
}
