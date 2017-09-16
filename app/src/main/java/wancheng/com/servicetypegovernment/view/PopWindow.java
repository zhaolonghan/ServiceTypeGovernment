package wancheng.com.servicetypegovernment.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckDirectoryActivity;
import wancheng.com.servicetypegovernment.activity.CompanyCheckListActivity;

/**
 * <p>Title:PopWindow</p>
 * <p>Description: 自定义PopupWindow</p>
 * @author syz
 * @date 2016-3-14
 */
public class PopWindow extends PopupWindow{
    private View conentView;
    public PopWindow(final Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_window, null);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 20);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        //整理数据，编写按钮
      /*  conentView.findViewById(R.id.about).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
                PopWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.ability_logout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something before signing out
                context.finish();
                PopWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.food_make).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something you need here

                Intent intent = new Intent();
                intent.setClass(context, CheckDirectoryActivity.class);
                context.startActivity(intent);
            }
        });*/
    }
    public PopWindow(final Activity context,final  List<Map<String, Object>> bottondatalist){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_window, null);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 20);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        //整理数据，编写按钮
        LinearLayout boxslay=(LinearLayout)conentView.findViewById(R.id.boxs);

        if(bottondatalist!=null&&bottondatalist.size()>0){
            for(int i=0;i<bottondatalist.size();i++){
                TextView textView =(TextView)boxslay.getChildAt(2*i);
                View link =boxslay.getChildAt(2*i+1);
                textView.setText(bottondatalist.get(i).get("name").toString());
                final String  name=bottondatalist.get(i).get("name").toString();
                final String  ztlx=bottondatalist.get(i).get("ztlx").toString();

                textView.setVisibility(View.VISIBLE);
                link.setVisibility(View.VISIBLE);

                //点击事件
                textView .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.putExtra("name",name);
                        intent.putExtra("ztlx",ztlx);
                        intent.setClass(context, CheckDirectoryActivity.class);
                        context.startActivity(intent);
                   }
               });
            }
        }
       // boxsLAY.addView();
      /*  <TextView
        android:id="@+id/food_make"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:gravity="center"
        android:padding="12dp"
        android:text="食品生产"
        android:textSize="16dp"
        android:textColor="@color/white"/>

        <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/white" />*/
      /*  conentView.findViewById(R.id.about).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
                PopWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.ability_logout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something before signing out
                context.finish();
                PopWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.food_make).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something you need here

                Intent intent = new Intent();
                intent.setClass(context, CheckDirectoryActivity.class);
                context.startActivity(intent);
            }
        });*/
    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);
        } else {
            this.dismiss();
        }
    }
}