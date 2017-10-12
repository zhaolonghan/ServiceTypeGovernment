package wancheng.com.servicetypegovernment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImagesBean;
import wancheng.com.servicetypegovernment.view.HackyViewPager;

/**
 * 图片查看器
 */
public class ImagePagerUploadHeadActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private  String photoFileName;

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	protected TextView tv_left;
	protected TextView  tv_right;
	private int imageIndexP=-1;//父索引;
	private int imageIndexC=-1;//子索引

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_uploadheadpager2);
		tv_left=(TextView)findViewById(R.id.tv_left);
		tv_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		tv_right=(TextView)findViewById(R.id.tv_right);
		tv_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ArrayList<ImagesBean> urls = (ArrayList<ImagesBean>)getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);
				ShowPickDialog(urls);
			}
		});

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		ArrayList<ImagesBean> urls = (ArrayList<ImagesBean>)getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<ImagesBean> fileList;
		public int count ;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<ImagesBean> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position).getPath();
			String type = fileList.get(position).getType();
			return ImageDetailHeadFragment.newInstance(url,type);
		}

	}
	private void ShowPickDialog(final ArrayList<ImagesBean> imageUrls){
		new AlertDialog.Builder(this)
				.setTitle("选择图片")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(ImagePagerUploadHeadActivity.this,
								ImageChooseActivity.class);
						Bundle bundle = new Bundle();
						final ArrayList<String> url2 = new ArrayList<String>();
						for (ImagesBean im : imageUrls) {
							if (im.getType().equals("localImage")){
								url2.add(im.getPath());
							}
						}
						bundle.putStringArrayList("listurl", url2);
						bundle.putInt("listsize", imageUrls.size()-1);
						intent.putExtra("bundle", bundle);
						startActivityForResult(intent,999);
					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						photoFileName = System.currentTimeMillis() + ((Math.random() * 9 + 1) * 1000) + "";
						File destDir = new File("/sdcard/Wancheng/Photos/");
						if (!destDir.exists()) {
							destDir.mkdirs();
						}
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory() + "/Wancheng/Photos/",
										photoFileName + ".jpg")));
						startActivityForResult(intent, 998);
					}
				}).show();
	}}

