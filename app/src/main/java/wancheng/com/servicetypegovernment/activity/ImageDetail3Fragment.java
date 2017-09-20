package wancheng.com.servicetypegovernment.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import wancheng.com.servicetypegovernment.R;

/**
 * 单张图片显示Fragment
 * @author hanzl
 */
public class ImageDetail3Fragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	public static ImageDetail3Fragment newInstance(String imageUrl) {
		final ImageDetail3Fragment f = new ImageDetail3Fragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments().getString("url");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.circle_image_detail_fragment, container, false);
			mImageView = (ImageView) v.findViewById(R.id.image);
			Uri imagepath = Uri.fromFile(new File(mImageUrl));
			mImageView.setImageURI(imagepath);
			mAttacher = new PhotoViewAttacher(mImageView);
			mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
					getActivity().finish();
				}
			});
//			mAttacher.setOnLongClickListener(new OnLongClickListener() {
//
//				@Override
//				public boolean onLongClick(View arg0) {
//					new myDialog()
//					.showDialog(R.layout.dialog, 80, 50);
//					return true;
//				}
//			});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	//自定义Dialog
	class myDialog extends Dialog{

		private Window window = null;
		public myDialog()
		{
			super(getActivity());
		}

		public void showDialog(int layoutResID, int x, int y){
			setContentView(layoutResID);
			LinearLayout rl_all_lay=(LinearLayout) findViewById(R.id.rl_all_lay);
			RelativeLayout rl_bottom =(RelativeLayout)findViewById(R.id.rl_bottom);
			LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final RelativeLayout item2 = (RelativeLayout) layoutInflater
					.inflate(R.layout.alert_expert_dialog, null);
			final RelativeLayout rl_type2=(RelativeLayout) item2.findViewById(R.id.rl_type);
			final TextView text2=(TextView)item2.findViewById(R.id.tv_text);
			text2.setText("删除");
			rl_type2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					Intent intent = new Intent(getActivity(), CheckDetailActivity.class);
					// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					Bundle bundle = new Bundle();
					bundle.putString("delurl", mImageUrl);
					intent.putExtra("bundle", bundle);
					getActivity().setResult(999, intent);
					getActivity().finish();

				}
			});
			rl_bottom.setOnClickListener(new View. OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			rl_all_lay.addView(item2);
			windowDeploy(x, y);
			//设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		}

		//设置窗口显示
		public void windowDeploy(int x, int y){
			window = getWindow(); //得到对话框
			window.setWindowAnimations(R.style.AnimBottom); //设置窗口弹出动画
			window.setBackgroundDrawableResource(R.color.vifrification); //设置对话框背景为透明
			WindowManager.LayoutParams wl = window.getAttributes();
			WindowManager m = getActivity().getWindowManager();
			Display d = m.getDefaultDisplay();
			wl.width = (int) d.getWidth(); 
			wl.gravity = Gravity.BOTTOM; //设置重力
			window.setAttributes(wl);
		}
	}
}
