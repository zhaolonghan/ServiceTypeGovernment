package wancheng.com.servicetypegovernment.adspter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.ImagesBean;

public class NoScrollGridResultAdapter extends BaseAdapter {


	private boolean isShowDelete;
	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<ImagesBean> imageUrls;

	public NoScrollGridResultAdapter(Context ctx, ArrayList<ImagesBean> urls) {
		this.ctx = ctx;
		this.imageUrls = urls;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.item_resultgridview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

		ImagesBean image=imageUrls.get(position);
		if (image.getType().equals("netImage")){
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.cacheInMemory(true)//
				.cacheOnDisk(true)//
				.bitmapConfig(Config.RGB_565)//
				.build();
			ImageLoader.getInstance().displayImage(image.getPath(), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					view.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				}
			});
		}
		if(image.getType().equals("defaultImage")){
			imageView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.add_img));
		}
		if(image.getType().equals("localImage")){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bm = BitmapFactory.decodeFile(image.getPath(), options);
			imageView.setImageBitmap(bm);
		}




//		final int index=position;
//		Toast.makeText(ctx, ifShowDelete+"", Toast.LENGTH_SHORT).show();
//		if(ifShowDelete){
//			view.setVisibility(View.VISIBLE);
//			view.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					imageUrls.remove(index);
//					notifyDataSetChanged();
//				}
//			});
//		}




		return view;
	}
	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}


}
