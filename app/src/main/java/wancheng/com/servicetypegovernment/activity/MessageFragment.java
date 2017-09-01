package wancheng.com.servicetypegovernment.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wancheng.com.servicetypegovernment.R;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class MessageFragment  extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contactsLayout = inflater.inflate(R.layout.activity_index,
                container, false);
        return contactsLayout;
    }
    @Override
    protected void lazyLoad() {

    }
}