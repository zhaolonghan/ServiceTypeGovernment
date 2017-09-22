package wancheng.com.servicetypegovernment.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import wancheng.com.servicetypegovernment.R;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailHeadFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private String type;

	public static ImageDetailHeadFragment newInstance(String imageUrl,String type) {
		final ImageDetailHeadFragment f = new ImageDetailHeadFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		args.putString("type", type);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		type = getArguments() != null ? getArguments().getString("type") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if("localImage".equals(type)){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bm = BitmapFactory.decodeFile(mImageUrl, options);
			mImageView.setImageBitmap(bm);
		}else if("defaultImage".equals(type)){
			mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
		} else{
			ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progressBar.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "下载错误";
							mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
							break;
						case DECODING_ERROR:
							message = "图片无法显示";
							mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
							break;
						case NETWORK_DENIED:
							message = "网络有问题，无法下载";
							mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
							break;
						case OUT_OF_MEMORY:
							message = "图片太大无法显示";
							mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
							break;
						case UNKNOWN:
							message = "未知的错误";
							mImageView.setImageDrawable(getResources().getDrawable(R.drawable.touxiang1));
							break;
					}
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
					mAttacher.update();

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					progressBar.setVisibility(View.GONE);
					mAttacher.update();
				}
			});
		}
	}
}
