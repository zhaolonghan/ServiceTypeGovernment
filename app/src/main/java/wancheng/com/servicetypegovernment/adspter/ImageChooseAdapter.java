package wancheng.com.servicetypegovernment.adspter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.activity.CheckDetailActivity;
import wancheng.com.servicetypegovernment.activity.ImageChooseActivity;
import wancheng.com.servicetypegovernment.activity.ImagePager3Activity;
import wancheng.com.servicetypegovernment.activity.ImagePagerActivity;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.view.ViewHolder;

/**
 * Created by HANZHAOLONG on 2017/8/24.
 */
public class ImageChooseAdapter extends CommonAdapter<String>
{

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public  ArrayList<String> mSelectedImage = new ArrayList<String>();
    private int count=0;
    private Context context;
    private TextView mImageCount;
    private TextView tv_looking;
    /**
     * 文件夹路径
     */
    private String mDirPath;
    public ImageChooseAdapter(Context context, List<String> mDatas, int itemLayoutId,
                              String dirPath,TextView mImageCount,TextView tv_looking,int count, ArrayList<String> mSelectedImage)
    {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.context=context;
        this.mImageCount=mImageCount;
        this.tv_looking=tv_looking;
        this.mSelectedImage=mSelectedImage;
        if(mSelectedImage!=null&&mSelectedImage.size()>0){
            this.count=mSelectedImage.size()+count;
        }else{
            this.count=count;
        }


    }

    @Override
    public void convert(final ViewHolder helper, final String item)
    {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);
//		if(chooseUrls!=null&&chooseUrls.size()>0){
//			for(String s:chooseUrls){
//				if (s.contains(mDirPath + "/" + item))
//				{
//					mSelect.setImageResource(R.drawable.pictures_selected);
//					mImageView.setColorFilter(Color.parseColor("#77000000"));
//				}
//			}
//		}
        mImageView.setColorFilter(null);
        mImageCount.setText(""+count+"张 确定");
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener()
        {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {

                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item))
                {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                    count--;
                } else if(count<5){
                    // 未选择该图片
                    {
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                        count++;
                    }
                }else{
                    Toast.makeText(context, "您最多可以选择5张图片", Toast.LENGTH_SHORT).show();
                }
                mImageCount.setText(""+count+"张 确定");
            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item))
        {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }


        tv_looking.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(count==0){
                    Toast.makeText(context, "您还没有选择图片", Toast.LENGTH_SHORT).show();
                }else{
                    imageBrower(0,mSelectedImage);
                }

            }
        });
        mImageCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(count==0){
                    Toast.makeText(context, "您还没有选择图片", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, CheckDetailActivity.class);
                    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("listurl", mSelectedImage);
                    intent.putExtra("bundle", bundle);
                    ((ImageChooseActivity) context).setResult(999, intent);
                    ((ImageChooseActivity) context).finish();
                }

            }
        });
    }
    protected void imageBrower(int position, ArrayList<String> url2) {
        Intent intent = new Intent(context, ImagePager3Activity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        List<ImagesBean> list=new ArrayList<ImagesBean>();
        for(String s:url2){
            list.add(new ImagesBean("localImage",s));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, url2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
