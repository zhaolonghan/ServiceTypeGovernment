package wancheng.com.servicetypegovernment.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NoScrollGridAdapter;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.ItemEntity;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.NoScrollGridView;

public class CheckDetailActivity extends BaseActivity {
    private ArrayList<ItemEntity> itemEntities;
    private ItemEntity itemEntity;
    private NoScrollGridView noScrollGridVieww;
    private NoScrollGridAdapter noScrollGridAdapter;
    private ArrayList<ImagesBean> imageUrls;
    private ArrayList<ImagesBean> newList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        noScrollGridVieww=(NoScrollGridView) findViewById(R.id.gridview);
        initData();
        itemEntity=itemEntities.get(3);
        imageUrls = itemEntity.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            noScrollGridVieww.setVisibility(View.GONE);
        } else {
            noScrollGridAdapter=new NoScrollGridAdapter(this, imageUrls);
            noScrollGridVieww.setAdapter(noScrollGridAdapter);
        }
        // 点击回帖九宫格，查看大图
        noScrollGridVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(!imageUrls.get(position).getType().equals("defaultImage")){
                    newList=new ArrayList<ImagesBean>();
                    newList.addAll(imageUrls);
                    if(imageUrls.get(imageUrls.size()-1).getType().equals("defaultImage")){
                        newList.remove(newList.size()-1);
                    }
                    imageBrower(position, newList);
                }
            }
        });
        noScrollGridVieww.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               if(!imageUrls.get(position).getType().equals("defaultImage")){
                   showNormalDialogIsDel("提示","是否确定删除此图片？",position);
               }
                return true;
            }
        });
//        bt_check=(Button)findViewById(R.id.bt_check);
//        bt_check.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Toast.makeText(CheckDetailActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(CheckDetailActivity.this, InformActivity.class);
//                CheckDetailActivity.this.startActivity(intent);
//            }
//        });

        TopBean topBean=new TopBean("检查要点","返回","下一步",true,true);
        getTopView(topBean);
        TextView tv = (TextView) findViewById(R.id.tv_zhinan);
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CheckDetailActivity.this, CheckResultActivity.class);
                CheckDetailActivity.this.startActivityForResult(intent, 0);
            }
        });

    }
    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }

    protected void showNormalDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_mydialog,
                (ViewGroup) findViewById(R.id.dialog));
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setView(layout);
        normalDialog.setTitle("检查指南");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        normalDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        itemEntities = new ArrayList<ItemEntity>();
        // 1.无图片
        ItemEntity entity1 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698871_3655.jpg", "张三", "今天天气不错...", null);
        itemEntities.add(entity1);
        // 2.1张图片
        ArrayList<ImagesBean> urls_1 = new ArrayList<ImagesBean>();
        urls_1.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg"));
        ItemEntity entity2 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg", "李四", "今天雾霾呢...", urls_1);
        itemEntities.add(entity2);
        // 3.3张图片
        ArrayList<ImagesBean> urls_2 = new ArrayList<ImagesBean>();
        urls_2.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg"));
        urls_2.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg"));
        urls_2.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg"));
        ItemEntity entity3 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg", "王五", "今天好大的太阳...", urls_2);
        itemEntities.add(entity3);
        // 4.6张图片
        ArrayList<ImagesBean> urls_3 = new ArrayList<ImagesBean>();
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg"));
        urls_3.add(new ImagesBean("netImage","http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg"));
   //     urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
        ItemEntity entity4 = new ItemEntity(//
                "http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg", "赵六", "今天下雨了...", urls_3);
        itemEntities.add(entity4);
    }
    protected void showNormalDialogIsDel(String title,String context,final int position){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(context);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        delete(position);
                        noScrollGridAdapter = new NoScrollGridAdapter(CheckDetailActivity.this, imageUrls);//重新绑定一次adapter
                        noScrollGridVieww.setAdapter(noScrollGridAdapter);
                        noScrollGridAdapter.notifyDataSetChanged();//刷新gridview

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    private void delete(int position) {//删除选中项方法
        ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
        boolean isDefult=false;
        imageUrls.remove(position);
        newList.addAll(imageUrls);
        imageUrls.clear();
        imageUrls.addAll(newList);
        for(ImagesBean image:imageUrls){
            if(image.getType().equals("defaultImage")){
                isDefult=true;
            }
        }
        if(!isDefult){
            imageUrls.add(new ImagesBean("defaultImage",""));
        }
    }
}
