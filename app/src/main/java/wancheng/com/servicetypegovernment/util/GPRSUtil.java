package wancheng.com.servicetypegovernment.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.toolbox.ImageLoader;


public  class GPRSUtil {
    private AMapLocationClient locationClient = null;
    Context context=null;
    double lng=0;
    double lat=0;
    public GPRSUtil(final Context context) {
        this.context=context;
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationOption(getDefaultOption());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {
                if (null != loc) {
                    lng =loc.getLongitude();
                    lat=loc.getLatitude();
                    Log.e("位置",lng+"");
                } else {
                    Toast.makeText(context, " 无法获取当前的位置" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationClient.startLocation();
    }

    public  double[] getlatAndlng(){

        if(lng>0){
            double[] lngAndlat=new double[2];
            lngAndlat[0]=lat;
            lngAndlat[1]=lng;
            return lngAndlat;
        }
        return null;
    }
    public AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//
        mOption.setGpsFirst(false);//
        mOption.setHttpTimeOut(30000);//
        mOption.setInterval(200000);//
        mOption.setNeedAddress(true);//
        mOption.setOnceLocation(true);//
        mOption.setOnceLocationLatest(false);//
        mOption.setLocationCacheEnable(true);
        return mOption;
    }
}
