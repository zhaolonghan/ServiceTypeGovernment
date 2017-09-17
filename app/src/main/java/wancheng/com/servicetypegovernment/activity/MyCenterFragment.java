package wancheng.com.servicetypegovernment.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;

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
        Context context= getContext();
        TextView username=(TextView)contactsLayout.findViewById(R.id.tv_username );
        username.setText(UserDateBean.getUser().getName());
        com.makeramen.roundedimageview.RoundedImageView userimage=(com.makeramen.roundedimageview.RoundedImageView)contactsLayout.findViewById(R.id.iv_user);
        if(UserDateBean.getUser().getPhotoimage()!=null&&UserDateBean.getUser().getPhotoimage().length()>0){
            String url = "http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
            //得到可用的图片
            Bitmap bitmap = getHttpBitmap(url);
            if(bitmap!=null){
                //显示
                userimage.setImageBitmap(bitmap);
            }

        }
        //我的资料
    }
    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
            bitmap=null;
        }

        return bitmap;

    }
}