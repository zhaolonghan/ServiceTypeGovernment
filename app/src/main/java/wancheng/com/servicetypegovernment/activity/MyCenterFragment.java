package wancheng.com.servicetypegovernment.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;


import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;

/**
 * Created by HANZHAOLONG on 2017/8/31.
 */
public class MyCenterFragment   extends BaseFragment {

    private ArrayList<ImagesBean> imageUrls;
    private Context thiscontext = null;
    private ArrayList<ImagesBean> imageUrlsNew;
    private String photoFileName;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private boolean isDel;
    private RelativeLayout updatelayout;//修改密码
    private RelativeLayout myinfo;//我的资料
    /* private RelativeLayout updatelayout;//我的工作*/
    private RelativeLayout myinfocollection;//我的收藏

    private RelativeLayout dataanalysis;//数据分析
    private RelativeLayout clearspace;//清理空间


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.activity_my_center,
                container, false);
        initView();
        //事件
        onOperationEvent();
        return contactsLayout;
    }

    private void initView() {
        TopBean topBean = new TopBean("个人中心", "", "", false, false);
        getTopView(topBean, contactsLayout);
        thiscontext = getContext();
        layoutInflater = LayoutInflater.from(thiscontext);
        updatelayout = (RelativeLayout) contactsLayout.findViewById(R.id.updatelayout);
        myinfocollection = (RelativeLayout) contactsLayout.findViewById(R.id.myinfocollection);
        dataanalysis = (RelativeLayout) contactsLayout.findViewById(R.id.dataanalysis);
        clearspace = (RelativeLayout) contactsLayout.findViewById(R.id.clearspace);
        myinfo = (RelativeLayout) contactsLayout.findViewById(R.id.myinfo);

    }

    @Override
    public void startActivity(Intent intent) {
        com.makeramen.roundedimageview.RoundedImageView userimage = (com.makeramen.roundedimageview.RoundedImageView) contactsLayout.findViewById(R.id.iv_user);
        // if(UserDateBean.getUser().getPhoto()!=null&&UserDateBean.getUser().getPhoto().length()>0){
        //  String url = "http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
        String url = "";
        if (UserDateBean.getUser().getPhotoimage() != null && UserDateBean.getUser().getPhotoimage().length() > 0) {
            url = UserDateBean.getUser().getPhotoimage();
        }
        //得到可用的图片
        final com.makeramen.roundedimageview.RoundedImageView iv_images = (com.makeramen.roundedimageview.RoundedImageView) contactsLayout.findViewById(R.id.iv_user);
        ImagesBean imagesBeans = new ImagesBean();
        if (url.length() == 0) {
            imagesBeans.setPath(url);
            imagesBeans.setType("defaultImage");
        } else {
            imagesBeans.setPath(url);
            imagesBeans.setType("netImage");

        }

        getImageGridViews(imagesBeans, iv_images);

    }

    public void onOperationEvent() {
        updatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thiscontext, NewpasswordActivity.class);
                thiscontext.startActivity(intent);
            }
        });
        myinfocollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "暂未开放，请敬请期待吧！", Toast.LENGTH_SHORT).show();
            }
        });
        dataanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "暂未开放，请敬请期待吧！", Toast.LENGTH_SHORT).show();
            }
        });
        clearspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "暂未开放，请敬请期待吧！", Toast.LENGTH_SHORT).show();
            }
        });
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thiscontext, MyinfoActivity.class);
                thiscontext.startActivity(intent);
            }
        });
    }

    @Override
    protected void lazyLoad() {


    }

    @Override
    public void updateView() {
        TextView username = (TextView) contactsLayout.findViewById(R.id.tv_username);
        username.setText(UserDateBean.getUser().getName());
        com.makeramen.roundedimageview.RoundedImageView userimage = (com.makeramen.roundedimageview.RoundedImageView) contactsLayout.findViewById(R.id.iv_user);
        // if(UserDateBean.getUser().getPhoto()!=null&&UserDateBean.getUser().getPhoto().length()>0){
        //  String url = "http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
        String url = "";
        if (UserDateBean.getUser().getPhotoimage() != null && UserDateBean.getUser().getPhotoimage().length() > 0) {
            url = UserDateBean.getUser().getPhotoimage();
        }
        //得到可用的图片
        final com.makeramen.roundedimageview.RoundedImageView iv_images = (com.makeramen.roundedimageview.RoundedImageView) contactsLayout.findViewById(R.id.iv_user);
        ImagesBean imagesBeans = new ImagesBean();
        if (url.length() == 0) {
            imagesBeans.setPath(url);
            imagesBeans.setType("defaultImage");
        } else {
            imagesBeans.setPath(url);
            imagesBeans.setType("netImage");
        }

        getImageGridViews(imagesBeans, iv_images);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("laialal","lailess....................");
        updateView();

    }
    private void getImageGridViews(final ImagesBean image, final RoundedImageView linearLayout) {
        if (image.getType().equals("netImage")) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()//
                    .cacheInMemory(true)//
                    .cacheOnDisk(true)//
                    .bitmapConfig(Bitmap.Config.RGB_565)//
                    .build();
            ImageLoader.getInstance().displayImage(image.getPath(), linearLayout, options);
        }
        if (image.getType().equals("defaultImage")) {
            linearLayout.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
        }
        if (image.getType().equals("localImage")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(image.getPath(), options);
            linearLayout.setImageBitmap(bm);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<ImagesBean> newList = new ArrayList<ImagesBean>();
                newList.add(image);
                imageBrower(0, newList);

            }
        });

        imageUrlsNew = imageUrls;

    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<ImagesBean> urls2) {
        Intent intent = new Intent(thiscontext, ImagePagerHeadActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        thiscontext.startActivity(intent);
    }
}