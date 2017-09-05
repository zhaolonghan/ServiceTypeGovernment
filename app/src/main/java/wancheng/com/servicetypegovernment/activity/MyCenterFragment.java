package wancheng.com.servicetypegovernment.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class MyCenterFragment   extends BaseFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.activity_my_center,
                container, false);
        lazyLoad();
        return contactsLayout;
    }
    @Override
    protected void lazyLoad() {
        TopBean topBean=new TopBean("个人中心","","",false,false);
        getTopView(topBean,contactsLayout);

    }

}