package wancheng.com.servicetypegovernment.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.util.JSONUtils;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class CoreActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 用于展示首页的Fragment
     */
    private IndexFragment indexFragment;
    /**
     * 用于展示通知公告的Fragment
     */
    private MessageFragment messageFragment;
    /**
     * 用于展示法律法规的Fragment
     */
    private LegalFragment legalFragment;
    /**
     * 用于展示新闻动态的Fragment
     */
    private NewsFragment newsFragment;
    /**
     * 用于展示个人中心的Fragment
     */
    private MyCenterFragment myCenterFragment;



    private LinearLayout lin_index;
    private LinearLayout lin_message;
    private LinearLayout lin_legal;
    private LinearLayout lin_news;
    private LinearLayout lin_myCenter;

    private ImageView iv_index;
    private ImageView iv_message;
    private ImageView iv_legal;
    private ImageView iv_news;
    private ImageView iv_myCenter;

    private TextView tv_index;
    private TextView tv_message;
    private TextView tv_legal;
    private TextView tv_news;
    private TextView tv_myCenter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_view);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
        Log.e("loginName", UserDateBean.getInstance().getUsername() + "");
        Log.e("uid", UserDateBean.getInstance().getId() + "");
        Log.e("name", UserDateBean.getInstance().getName()+"");
        Log.e("mobile", UserDateBean.getInstance().getPhone() + "");
    }
    private void initViews() {
        lin_index=(LinearLayout)findViewById(R.id.lin_foot1);
        lin_message=(LinearLayout)findViewById(R.id.lin_foot2);
        lin_legal=(LinearLayout)findViewById(R.id.lin_foot3);
        lin_news=(LinearLayout)findViewById(R.id.lin_foot4);
        lin_myCenter=(LinearLayout)findViewById(R.id.lin_foot5);
        iv_index=(ImageView)findViewById(R.id.iv_index);
        iv_message=(ImageView)findViewById(R.id.iv_message);
        iv_legal=(ImageView)findViewById(R.id.iv_legal);
        iv_news=(ImageView)findViewById(R.id.iv_news);
        iv_myCenter=(ImageView)findViewById(R.id.iv_my_center);
        tv_index=(TextView)findViewById(R.id.tv_index);
        tv_message=(TextView)findViewById(R.id.tv_message);
        tv_legal=(TextView)findViewById(R.id.tv_legal);
        tv_news=(TextView)findViewById(R.id.tv_news);
        tv_myCenter=(TextView)findViewById(R.id.tv_my_center);
        lin_index.setOnClickListener(this);
        lin_message.setOnClickListener(this);
        lin_legal.setOnClickListener(this);
        lin_news.setOnClickListener(this);
        lin_myCenter.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_foot1:
                // 当点击了首页tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.lin_foot2:
                // 当点击了通知公告tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.lin_foot3:
                // 当点击了法律法规动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.lin_foot4:
                // 当点击了新闻动态tab时，选中第4个tab
                setTabSelection(3);
                break;
            case R.id.lin_foot5:
                // 当点击了个人中心tab时，选中第4个tab
                setTabSelection(4);
                break;
            default:
                break;
        }
    }
    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {

        int btnblue=getResources().getColor(R.color.btnblue);
        int white=getResources().getColor(R.color.white);
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                default:
                // 当点击了首页tab时，改变控件的图片和文字颜色
                lin_index.setBackgroundColor(white);
                iv_index.setImageResource(R.drawable.sy01);
                tv_index.setTextColor(btnblue);
                if (indexFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.content, indexFragment);
                } else {
                    // 如果indexFragment不为空，则直接将它显示出来
                    transaction.show(indexFragment);
                }
                break;
            case 1:
                // 当点击了通知公告tab时，改变控件的图片和文字颜色
                lin_message.setBackgroundColor(white);
                iv_message.setImageResource(R.drawable.tz01);
                tv_message.setTextColor(btnblue);
                if (messageFragment == null) {
                    // 如果messageFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {
                    // 如果messageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 2:
                lin_legal.setBackgroundColor(white);
                // 当点击了法律法规tab时，改变控件的图片和文字颜色
                iv_legal.setImageResource(R.drawable.fl01);
                tv_legal.setTextColor(btnblue);
                if (legalFragment== null) {
                    // 如果legalFragment为空，则创建一个并添加到界面上
                    legalFragment = new LegalFragment();
                    transaction.add(R.id.content, legalFragment);
                } else {
                    // 如果legalFragment不为空，则直接将它显示出来
                    transaction.show(legalFragment);
                }
                break;
            case 3:
                lin_news.setBackgroundColor(white);
                // 当点击了新闻动态tab时，改变控件的图片和文字颜色
                iv_news.setImageResource(R.drawable.xw01);
                tv_news.setTextColor(btnblue);
                if (newsFragment== null) {
                    // 如果newsFragment为空，则创建一个并添加到界面上
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content, newsFragment);
                } else {
                    // 如果newsFragment不为空，则直接将它显示出来
                    transaction.show(newsFragment);
                }
                break;
            case 4:
                lin_myCenter.setBackgroundColor(white);
                // 当点击了个人中心tab时，改变控件的图片和文字颜色
                iv_myCenter.setImageResource(R.drawable.gr01);
                tv_myCenter.setTextColor(btnblue);
                if (myCenterFragment== null) {
                    // 如果myCenterFragmentt为空，则创建一个并添加到界面上
                    myCenterFragment = new MyCenterFragment();
                    transaction.add(R.id.content, myCenterFragment);
                } else {
                    // 如果myCenterFragmentt不为空，则直接将它显示出来
                    transaction.show(myCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        //未选
        int white=getResources().getColor(R.color.white);
        int btnblue=getResources().getColor(R.color.btnblue);

        iv_index.setImageResource(R.drawable.sy02);
        tv_index.setTextColor(white);
        lin_index.setBackgroundColor(btnblue);
        iv_message.setImageResource(R.drawable.tz02);
        tv_message.setTextColor(white);
        lin_message.setBackgroundColor(btnblue);
        iv_legal.setImageResource(R.drawable.fl02);
        tv_legal.setTextColor(white);
        lin_legal.setBackgroundColor(btnblue);
        iv_news.setImageResource(R.drawable.xw02);
        tv_news.setTextColor(white);
        lin_news.setBackgroundColor(btnblue);
        iv_myCenter.setImageResource(R.drawable.gr02);
        tv_myCenter.setTextColor(white);
        lin_myCenter.setBackgroundColor(btnblue);
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (legalFragment != null) {
            transaction.hide(legalFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (myCenterFragment != null) {
            transaction.hide(myCenterFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (indexFragment != null) {
            indexFragment.clickBack(keyCode, event,this);
        }
        if (messageFragment != null) {
            messageFragment.clickBack(keyCode, event,this);
        }
        if (legalFragment != null) {
            legalFragment.clickBack(keyCode, event,this);
        }
        if (newsFragment != null) {
            newsFragment.clickBack(keyCode, event,this);
        }
        if (myCenterFragment != null) {
            myCenterFragment.clickBack(keyCode, event,this);
        }
            return true;
    }

}
