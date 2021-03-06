package wancheng.com.servicetypegovernment.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import java.io.File;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.BusResultListAdapter;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.route.DrivingRouteOverLay;
import wancheng.com.servicetypegovernment.util.AMapUtil;
import wancheng.com.servicetypegovernment.util.ToastUtil;


public class RouteActivity extends BaseActivity implements OnMapClickListener,
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
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
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.route_activity);
		
		mContext = this.getApplicationContext();
		intent= getIntent();
		TopBean topBean=new TopBean("地图","返回","",true,false);
		getTopView(topBean);
		//获取地图控件引用
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(bundle);// 此方法必须重写
		init();
//		getIntentData();
		setfromandtoMarker();
		/*onDriveClick = (RelativeLayout) findViewById(R.id.onDriveClick);
		onDriveClick(onDriveClick);*/
		
	}
	
	private void setfromandtoMarker() {
	double corplng=Double.parseDouble(intent.getStringExtra("corp_lng") == null ? "0" : intent.getStringExtra("corp_lng"));
		double corplat=Double.parseDouble(intent.getStringExtra("corp_lat") == null ? "0" : intent.getStringExtra("corp_lat"));
		double loclng=Double.parseDouble(intent.getStringExtra("loc_lng") == null ? "0" : intent.getStringExtra("loc_lng"));
		double loclat=Double.parseDouble(intent.getStringExtra("loc_lat") == null ? "0" : intent.getStringExtra("loc_lat"));
	/*	double corplng=117.2096800000;
		double corplat=39.1369600000;
		double loclng=117.1723973751;
		double loclat=39.1371423535;*/
		corpname=intent.getStringExtra("corp_name");
		LatLng latLng = new LatLng(corplat,corplng);
		aMap.addMarker(new MarkerOptions().position(latLng).title("企业").snippet(corpname)).showInfoWindow();
		mStartPoint=new LatLonPoint(loclat,loclng);
		mEndPoint=new LatLonPoint(corplat,corplng);
		mStartPoint_bus=new LatLonPoint(loclat,loclng);
		mEndPoint_bus=new LatLonPoint(corplat,corplng);
		mCurrentCityName=intent.getStringExtra("now_city");


		LatLng marker1 = new LatLng(corplat, corplng);
		//设置中心点和缩放比例
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
	/*	aMap.addMarker(new MarkerOptions()
				.position(AMapUtil.convertToLatLng(mStartPoint))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));

		aMap.addMarker(new MarkerOptions()
				.position(AMapUtil.convertToLatLng(mEndPoint))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));*/
		/*aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mStartPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mEndPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));*/
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();	
		}
		registerListener();
		mRouteSearch = new RouteSearch(this);
		mRouteSearch.setRouteSearchListener(this);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline);
		mDrive = (ImageView)findViewById(R.id.route_drive);
		mBus = (ImageView)findViewById(R.id.route_bus);
		mWalk = (ImageView)findViewById(R.id.route_walk);
		mBusResultList = (ListView) findViewById(R.id.bus_result_list);
		bus_resultno=(TextView)findViewById(R.id.bus_resultno);
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(RouteActivity.this);
		aMap.setOnMarkerClickListener(RouteActivity.this);
		aMap.setOnInfoWindowClickListener(RouteActivity.this);
		aMap.setInfoWindowAdapter(RouteActivity.this);
		
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
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onBusClick(View view) {
		searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_select);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.GONE);
		mBusResultLayout.setVisibility(View.VISIBLE);


	}
	
	public void onDriveClick(View view) {
		searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
		mDrive.setImageResource(R.drawable.route_drive_select);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
		bus_resultno.setVisibility(View.GONE);

	}

	public void onWalkClick(View view) {
		searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_select);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
		bus_resultno.setVisibility(View.GONE);

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
			BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
					mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
					null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		} else if (routeType == ROUTE_TYPE_CROSSTOWN) {
			RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(
					mStartPoint_bus, mEndPoint_bus);
			BusRouteQuery query = new BusRouteQuery(fromAndTo_bus, mode,
					"呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			query.setCityd("农安县");
			mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
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
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.VISIBLE);
					int taxiCost = (int) mDriveRouteResult.getTaxiCost();
					mRouteDetailDes.setText("打车约" + taxiCost + "元");
					mBottomLayout.setOnClickListener(new OnClickListener() {
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
		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mWalkRouteResult = result;
					final WalkPath walkPath = mWalkRouteResult.getPaths()
							.get(0);
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
							this, aMap, walkPath,
							mWalkRouteResult.getStartPos(),
							mWalkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) walkPath.getDistance();
					int dur = (int) walkPath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.GONE);
					mBottomLayout.setOnClickListener(new OnClickListener() {
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
							 intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+mStartPoint.getLatitude()+"&slon="+mStartPoint.getLongitude()+"&sname="+"我的位置"+"&dlat="+mEndPoint.getLatitude()+"&dlon="+mEndPoint.getLongitude()+"&dname="+corpname+"&dev=0&m=0&t=4");
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

