package wancheng.com.servicetypegovernment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class AlarmReceiver extends BroadcastReceiver {
    private AMapLocationClient locationClient = null;
    public  Context thiscontext;
    @Override
    public void onReceive(Context context, Intent intent) {
        thiscontext=context;
       final String uid=intent.getStringExtra("uid");
        if(uid.length()>0){
            locationClient = new AMapLocationClient(context);
            locationClient.setLocationOption(getDefaultOption());
            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation loc) {
                    if (null != loc) {
                        final double lat = loc.getLatitude();
                        final double lng = loc.getLongitude();
                        Intent i = new Intent(thiscontext, UserlngTrajectoryService.class);
                        i.putExtra("uid",uid);
                        i.putExtra("lat",lat);
                        i.putExtra("lng",lng);
                        thiscontext.startService(i);
                    }
                }
            });
            locationClient.startLocation();

        }
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

