package wancheng.com.servicetypegovernment.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.BusResultListAdapter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.bean.UserDateBean;
import wancheng.com.servicetypegovernment.route.DrivingRouteOverLay;
import wancheng.com.servicetypegovernment.route.WalkRouteOverLays;
import wancheng.com.servicetypegovernment.util.AMapUtil;
import wancheng.com.servicetypegovernment.util.Base64Coder;
import wancheng.com.servicetypegovernment.util.ConstUtil;
import wancheng.com.servicetypegovernment.util.GeoUtils;
import wancheng.com.servicetypegovernment.util.JSONUtils;
import wancheng.com.servicetypegovernment.util.NetUtil;
import wancheng.com.servicetypegovernment.util.ToastUtil;

/**
 * test
 */
public class MapActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private TextView bus_resultno;
    private LatLonPoint mStartPoint =null;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = null;//终点，116.481288,39.995576
    private LatLonPoint mStartPoint_bus = null;//起点，111.670801,40.818311
    private LatLonPoint mEndPoint_bus = null;//终点，
    private String mCurrentCityName = "北京";
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;

    private LinearLayout mBusResultLayout;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ImageView mBus;
    private ImageView mDrive;
    private ImageView mWalk;
    private ListView mBusResultList;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private	RelativeLayout onDriveClick;
    private Intent intent =null;
    private  String corpname="";
    private Button bt_go;//导航
    private Button bt_check;//检查

    MyLocationStyle myLocationStyle;
    public Marker startMarker;
    public Marker endMarker;

    public List<Map<String,Object>> corplist;
    // uid，lat，lng，area
    //传送参数
    public  String  uid="";
    public  double  nowLoclat=0;
    public  double  nowLoclng=0;
    public  double  oldLoclat=0;
    public  double  oldLoclng=0;
    public  String  area="500";
    private  boolean isadd=true;//是否添加
    private MarkerOptions yi_markop;
    private Marker yi_marker;
    private List<Marker> markerlistobj;
    public ImageView  loc_btn;
    public String firstname="";
    public int notInspectNum=0;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.map_activity);

        mContext = this.getApplicationContext();
        intent= getIntent();
        TopBean topBean=new TopBean("我的辖区","返回","",true,false);
        getTopView(topBean);
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(bundle);// 此方法必须重写
        //设置基本样式
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        init();
        //定位
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        //获取数据
        double loclng=Double.parseDouble(intent.getStringExtra("loc_lng") == null ? "0" : intent.getStringExtra("loc_lng"));
        double loclat=Double.parseDouble(intent.getStringExtra("loc_lat") == null ? "0" : intent.getStringExtra("loc_lat"));
        Log.e("位置", intent.getStringExtra("city_name"));
        if(intent.getStringExtra("city_name").indexOf("天津")<0){
            firstname=intent.getStringExtra("city_name");
            loclat=39.1395300000;
            loclng=117.1596600000;
        }
        LatLng latLng = new LatLng(loclat,loclng);
        // getCorpListData(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());

        yi_marker=aMap.addMarker(new MarkerOptions().title("选取点").position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.yi_mark)));
        yi_marker.setObject("0");
      //  getCorpListData(latLng.latitude, latLng.longitude,false);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(yi_marker!=null){
                    if("0".equals(yi_marker.getObject().toString())){
                        yi_marker.setPosition(cameraPosition.target);
                    }
                }

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                //yi_mark.title("选取点").position(cameraPosition.target).icon(BitmapDescriptorFactory.fromResource(R.drawable.yi_mark));
                if(yi_marker!=null){
                    if("0".equals(yi_marker.getObject().toString())) {
                        if(firstname.indexOf("天津")<0){
                            LatLng latLng = new LatLng(39.1395300000,117.1596600000);
                            yi_marker.setPosition(latLng);
                        }else{
                            yi_marker.setPosition(cameraPosition.target);
                        }
                    }
                }

                //加载数据
                if(isadd){
                    if(yi_marker.getObject()==null||(yi_marker.getObject()!=null&&!"1".equals(yi_marker.getObject().toString()))) {
                        isadd = false;
                        if(firstname.indexOf("天津")<0){
                            getCorpListData(39.1395300000,117.1596600000,false);
                        }else{
                            getCorpListData(aMap.getCameraPosition().target.latitude, aMap.getCameraPosition().target.longitude,false);
                        }
                    }
                }

            }
        });

    }
    //获取mark
    private void getMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if("选取点".equals( marker.getTitle())){
                yi_marker=marker;
            }
           /* if (marker.getObject() instanceof xxx) {
                marker.remove();//移除当前Marker
            }*/
        }
        //aMap.invalidate();//刷新地图
    }

    @Override
    public void updateView() {
            //描点
            setfromandtoMarker();
        isadd=true;

    }

    public  void getCorpListData(double nowLoclats,double nowLoclngs,boolean isreload){
        if(firstname.indexOf("天津")<0){
            firstname="天津";
        }
        mBottomLayout.setVisibility(View.GONE);


        nowLoclat=nowLoclats;
        nowLoclng=nowLoclngs;
        //计算当前标记距离和上次标记距离,默认500m
        double nowdistance = GeoUtils.getDistance(oldLoclng, oldLoclat, nowLoclng, nowLoclat);

        if(ConstUtil.parityArea <nowdistance||isreload){
            corplist=new ArrayList<Map<String, Object>>();

            oldLoclat=nowLoclats;
            oldLoclng=nowLoclngs;
            pd = ProgressDialog.show(this, "", "请稍候...");
            new Thread() {
                public void run() {
                    String url= ConstUtil.METHOD_ADDMARKS;
                    Map<String, Object> map = new HashMap<String, Object>();
                    try{
                        JSONObject jsonQuery = new JSONObject();
                        jsonQuery.put("uid", UserDateBean.getInstance().getId());
                        jsonQuery.put("lat", nowLoclat);
                        jsonQuery.put("lng", nowLoclng);
                        jsonQuery.put("area", ConstUtil.Area);
                        map.put("data", Base64Coder.encodeString(jsonQuery.toString()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    NetUtil net = new NetUtil();
                    String res = net.posturl(url , map);
                    if(res==null||"".equals(res)||res.contains("Fail to establish http connection!")){
                        handler.sendEmptyMessage(4);
                    }else{
                        Message msg=new Message();
                        msg.what=15;
                        if (!res.isEmpty()) {
                            JSONObject jsonObj;
                            try {
                                jsonObj = new JSONObject(res);
                                String msg_code = testStringNull(jsonObj.optString("msg"));
                                String code = testStringNull(jsonObj.optString("code"));
                                if("0".equals(code)){
                                    String  data= Base64Coder.decodeString(jsonObj.getString("data"));
                                    if(data!=null&&data!=""){
                                        JSONArray dataArray=new JSONArray(data);
                                        if (dataArray!=null&&dataArray.length()>0) {
                                            setdata(dataArray);
                                        }
                                    }else{
                                        msg.obj="附近没有查询到企业";
                                    }

                                    msg.what=14;
                                }else{
                                    if(msg_code!=null&&!msg_code.isEmpty())
                                        msg.obj=msg_code;
                                    else
                                        msg.obj="请求异常，请稍后重试！";

                                }
                            } catch (JSONException e) {

                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Log.getStackTraceString(e);
                                msg.obj="请求异常，请稍后重试！";
                            }
                            handler.sendMessage(msg);
                        }
                    }
                }

                ;
            }.start();
        }else{
            isadd=true;
        }
    }
    public void setdata( JSONArray   dataArray) throws JSONException{
        if(dataArray!=null){
            Map<String, Object> contextmap=null;
            for(int i=0;i<dataArray.length();i++){
                JSONObject dataobject = dataArray.getJSONObject(i);
                if(dataobject!=null){
                    contextmap=new HashMap<String, Object>();
                    if(JSONUtils.getString(dataobject, "id", "")!=null&& JSONUtils.getString(dataobject, "id", "").length()>0&&JSONUtils.getString(dataobject, "name", "").length()>0) {
                        contextmap.put("corpid", JSONUtils.getString(dataobject, "id", ""));
                        contextmap.put("corpname", JSONUtils.getString(dataobject, "name", ""));
                        LatLng latLng2 = new LatLng( JSONUtils.getDouble(dataobject, "lat", 0),JSONUtils.getDouble(dataobject, "lng", 0));
                        contextmap.put("inum",i);
                        contextmap.put("latLng", latLng2);

                        // contextmap.put("corp_name", JSONUtils.getString(dataobject, "name", ""));
                        contextmap.put("corp_code", JSONUtils.getString(dataobject, "code", ""));
                        contextmap.put("corp_person", JSONUtils.getString(dataobject, "fuzeren", ""));
                        contextmap.put("corp_tel", JSONUtils.getString(dataobject, "fuzerenTel", ""));
                        contextmap.put("corp_address", JSONUtils.getString(dataobject, "jydz", ""));
                        contextmap.put("resultId", JSONUtils.getString(dataobject, "resultId", ""));
                        contextmap.put("tzsbId", JSONUtils.getString(dataobject, "tzsbId", ""));
                        //距离
                        contextmap.put("lng", JSONUtils.getString(dataobject, "lng", "0"));
                        contextmap.put("lat", JSONUtils.getString(dataobject, "lat", "0"));
                        contextmap.put("distance", JSONUtils.getString(dataobject, "distance", ""));
                       //未检查数
                        contextmap.put("notInspectNum", JSONUtils.getString(dataobject, "notInspectNum", "0"));

                         contextmap.put("inspectNum1", JSONUtils.getString(dataobject, "inspectNum1", "0"));
                        contextmap.put("inspectNum2", JSONUtils.getString(dataobject, "inspectNum2", "0"));
                        contextmap.put("inspectNum3", JSONUtils.getString(dataobject, "inspectNum3", "0"));

                        ArrayList<Map<String,Object>> corpTypeList;
                        JSONArray dataTypeArray=null;
                        Map<String, Object> type=null;
                        if(JSONUtils.getString(dataobject, "inspectTable", "").length()>0){
                            corpTypeList=new ArrayList<Map<String,Object>>();
                            dataTypeArray=new JSONArray(JSONUtils.getString(dataobject, "inspectTable", ""));
                            if(dataTypeArray!=null&&dataTypeArray.length()>0){
                                for(int j=0;j<dataTypeArray.length();j++){
                                    JSONObject typeobject = dataTypeArray.getJSONObject(j);
                                    type=new HashMap<String, Object>();
                                    type.put("tableName", JSONUtils.getString(typeobject, "tableName", ""));
                                    type.put("ztlx2", JSONUtils.getString(typeobject, "ztlx2", ""));
                                    corpTypeList.add(type);
                                }
                            }
                            contextmap.put("corpTypeList", corpTypeList);
                        }

                        corplist.add(contextmap);
                    }
                }

            }
        }
    }
    private void setfromandtoMarker() {

        if(markerlistobj!=null&&markerlistobj.size()>0){
            for(Marker m:markerlistobj){
               m.remove();
            }
        }

        double loclng=nowLoclng;
        double loclat=nowLoclat;
        if(nowLoclat==0||nowLoclng==0){
            loclng=aMap.getMyLocation().getLongitude();
            loclat=aMap.getMyLocation().getLatitude();
        }
        //标记选取点
        LatLng latLng = new LatLng(loclat,loclng);
        yi_marker.setPosition(latLng);

        aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        markerlistobj=new ArrayList<Marker>();
        if(corplist!=null&&corplist.size()>0){
            for(int i=0;i<corplist.size();i++){

               if("0".equals( corplist.get(i).get("notInspectNum").toString())){
                   Marker marker= aMap.addMarker(new MarkerOptions().title("企业").position((LatLng) (corplist.get(i).get("latLng"))).snippet(i + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.corpmark)));
                   marker .setObject("0");
                   markerlistobj.add(marker);
               }else{
                   Marker marker= aMap.addMarker(new MarkerOptions().title("企业").position((LatLng) (corplist.get(i).get("latLng"))).snippet(i + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.corpmark_yellow)));
                   marker .setObject("0");
                   markerlistobj.add(marker);
               }

            }
        }

        mStartPoint=new LatLonPoint(loclat,loclng);

        mCurrentCityName="北京";

        LatLng marker1 = new LatLng(nowLoclat, nowLoclng);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        isadd=true;
    }
    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (aMap != null) {
            if("企业".equals(marker.getTitle())){
                if("1".equals(marker.getObject().toString())){
                    aMap.addPolyline(null);
                    setfromandtoMarker();
                    PolylineOptions options=new PolylineOptions();
                    Polyline polyline = aMap.addPolyline(options);

                }else{
                    int i=Integer.parseInt(marker.getSnippet());
                    corpname=corplist.get(i).get("corpname").toString();
                    marker.setObject("1");
                    jumpPoint(marker);
                    //绘制路线
                    endMarker=marker;
                    onWalkClick(marker.getPosition());

                    //编辑检查按钮
                    int inum=Integer.parseInt(endMarker.getSnippet());
                    notInspectNum=corplist.get(inum).get("notInspectNum")==null?0:Integer.parseInt(corplist.get(inum).get("notInspectNum").toString());
                    if(notInspectNum>0){
                        String teststyle="开始检查(<span style='color:red'>"+notInspectNum+"</span>)";
                        bt_check.setText(Html.fromHtml(teststyle));
                    }else{
                        bt_check.setText("开始检查");
                    }
                    final String corpId =corplist.get(inum).get("corpid").toString();
                    final String phone =corplist.get(inum).get("corp_tel").toString().trim().replaceAll(" ", "");
                    String ztlx2 ="";
                    if(corplist.get(inum).get("ztlx")!=null){
                        ztlx2=corplist.get(inum).get("ztlx").toString();
                    }
                    final String ztlx = ztlx2;
                    final ArrayList<Map<String,Object>> dataTypeArray=(ArrayList<Map<String,Object>>)corplist.get(inum).get("corpTypeList");
                    String str="";
                    if(dataTypeArray!=null&&dataTypeArray.size()>1){
                        for(Map<String,Object>map:dataTypeArray){
                            str+=","+map.get("tableName").toString();
                        }
                    }
                    final String specialId=corplist.get(inum).get("specialId")!=null?corplist.get(inum).get("specialId").toString():"";
                    final String resultId=corplist.get(inum).get("resultId")!=null?corplist.get(inum).get("resultId").toString():"";
                    final String tzsbId=corplist.get(inum).get("tzsbId")!=null?corplist.get(inum).get("tzsbId").toString():"";
                    final String str1=str;
                    final String corp_address=corplist.get(inum).get("corp_address").toString();


                    bt_check.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                                UserDateBean.getInstance().setLng(aMap.getMyLocation().getLongitude());
                                UserDateBean.getInstance().setLat(aMap.getMyLocation().getLatitude());
                            //判断文字
                            if(dataTypeArray!=null&&dataTypeArray.size()>0){
                                if(dataTypeArray.size()==1){
                                    Intent intent = new Intent();
                                    intent.putExtra("corpId",corpId);
                                    intent.putExtra("specialId",specialId);
                                    intent.putExtra("corp_name",corpname);
                                    intent.putExtra("corp_address",corp_address);
                                    intent.putExtra("ztlx",dataTypeArray.get(0).get("ztlx2").toString());
                                    intent.putExtra("resultId",resultId);
                                    intent.putExtra("tzsbId",tzsbId);
                                    intent.setClass(MapActivity.this, InformActivity.class);
                                    MapActivity.this.startActivity(intent);
                                }else{
                                    final String[]  strArray=str1.substring(1).split(",");
                                    AlertDialog dialog = new AlertDialog.Builder(MapActivity.this).setTitle("选择检查表")
                                            .setSingleChoiceItems(strArray, -1, new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(MapActivity.this, strArray[which], Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent();
                                                    intent.putExtra("corpId",corpId);
                                                    intent.putExtra("specialId",specialId);
                                                    intent.putExtra("corp_name",corpname);
                                                    intent.putExtra("corp_address",corp_address);
                                                    intent.putExtra("resultId",resultId);
                                                    intent.putExtra("tzsbId",tzsbId);
                                                    intent.putExtra("ztlx",dataTypeArray.get(which).get("ztlx2").toString());
                                                    intent.setClass(MapActivity.this, InformActivity.class);
                                                    MapActivity.this.startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();
                                }
                            }else{
                                Toast.makeText(MapActivity.this, "该企业没有主体类型，暂时无法检查！", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        }
        return true;
    }
    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    /**
     * 初始化AMap对象
     */
    private void init() {

        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        bt_go=(Button)findViewById(R.id.bt_go);
        bt_check=(Button)findViewById(R.id.bt_check);
        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mDrive = (ImageView)findViewById(R.id.route_drive);
        mBus = (ImageView)findViewById(R.id.route_bus);
        mWalk = (ImageView)findViewById(R.id.route_walk);
        mBusResultList = (ListView) findViewById(R.id.bus_result_list);
        bus_resultno=(TextView)findViewById(R.id.bus_resultno);
        loc_btn=(ImageView)findViewById(R.id.loc_btn);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.clear();
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
                myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
                if(intent.getStringExtra("city_name").indexOf("天津")<0){
                    LatLng marker1 = new LatLng(39.1395300000,117.1596600000);
                    yi_marker=aMap.addMarker(new MarkerOptions().title("选取点").position(marker1).icon(BitmapDescriptorFactory.fromResource(R.drawable.yi_mark)));
                    yi_marker.setObject("0");
                    getCorpListData(marker1.latitude,marker1.longitude,true);
                }else{
                    LatLng marker1 = new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
                    yi_marker=aMap.addMarker(new MarkerOptions().title("选取点").position(marker1).icon(BitmapDescriptorFactory.fromResource(R.drawable.yi_mark)));
                    yi_marker.setObject("0");
                    getCorpListData(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude(),true);
                }

            }
        });
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    public void onBusClick(View view) {   }

    public void onDriveClick(View view) {  }

    public void onWalkClick(View view) {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_select);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
        bus_resultno.setVisibility(View.GONE);
    }
    public void onWalkClick(LatLng corpLatlog) {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault, corpLatlog);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
        bus_resultno.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
       /*
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
        bus_resultno.setVisibility(View.GONE);*/

    }
    public void onCrosstownBusClick(View view) {
        searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.GONE);
        mBusResultLayout.setVisibility(View.VISIBLE);
        bus_resultno.setVisibility(View.GONE);

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(
                    mStartPoint_bus, mEndPoint_bus);
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo_bus, mode,
                    "呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.setCityd("农安县");
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode,LatLng corplatlng) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (corplatlng == null) {
            ToastUtil.show(mContext, "企业位置获取失败");

        }else{
            mStartPoint=new LatLonPoint(yi_marker.getPosition().latitude,yi_marker.getPosition().longitude);
            mEndPoint=new LatLonPoint(corplatlng.latitude,corplatlng.longitude);
            yi_marker.setObject("1");
            startMarker=yi_marker;
            showProgressDialog();
            final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    mStartPoint, mEndPoint);
            if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
                mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
            }
        }

    }
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mBottomLayout.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null&&result.getPaths().size()>0) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);

                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                    bus_resultno.setVisibility(View.VISIBLE);

                }else{
                    bus_resultno.setVisibility(View.VISIBLE);

                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
                bus_resultno.setVisibility(View.VISIBLE);

            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    //获取企业名称
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+mStartPoint.getLatitude()+"&slon="+mStartPoint.getLongitude()+"&sname="+"我的位置"+"&dlat="+mEndPoint.getLatitude()+"&dlon="+mEndPoint.getLongitude()+"&dname="+corpname+"&dev=0&m=0&t=0");
                                if(isInstallByread("com.autonavi.minimap")){
                                    startActivity(intent);
                                }else {
                                    Log.e("提示", "没有安装高德地图客户端") ;
                                }
                            }catch (Exception e){

                            }
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        setfromandtoMarker();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    //new WalkRouteOverlay()
                    WalkRouteOverLays walkRouteOverlay = new WalkRouteOverLays(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos(),null,startMarker,endMarker);

                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        mBottomLayout.setVisibility(View.VISIBLE);


                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    //获取企业名称
                    mRouteDetailDes.setText(corpname);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    bt_go.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("corpname", corpname);
							/*intent.putExtra("walk_path", walkPath);
							intent.putExtra("walk_result",
									mWalkRouteResult);
							startActivity(intent);*/
                            try {
                                intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat=" + mStartPoint.getLatitude() + "&slon=" + mStartPoint.getLongitude() + "&sname=" + "我的位置" + "&dlat=" + mEndPoint.getLatitude() + "&dlon=" + mEndPoint.getLongitude() + "&dname=" + corpname + "&dev=0&m=0&t=4");
                                if (isInstallByread("com.autonavi.minimap")) {
                                    startActivity(intent);
                                } else {
                                    Log.e("提示", "没有安装高德地图客户端");
                                }
                            } catch (Exception e) {

                            }
                        }
                    });


                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


}
