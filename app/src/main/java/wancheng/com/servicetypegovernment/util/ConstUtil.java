package wancheng.com.servicetypegovernment.util;


import android.os.Environment;

/**
 * 常量工具类<br>
 * <br>
 *
 * 定义工程中的所有常量
 *
 * @author hanzl
 */
public class ConstUtil {
	/**
	 * 路径
	 */
	public static String rootDir = Environment.getExternalStorageDirectory()+ "/temp/";// 

	/**
	 * URL
	 */
	public static String IP = "http://223.71.241.40/";
	//public static String IP = "http://192.168.1.111:8181/";
	//public static String IP = "http://192.168.1.147:8181/";
	/**
	 * METHOD
	 */
	public static double Area = 3000; //地图加载距离
	public static double parityArea = 200; //相差距离刷新


	public static String METHOD_CHECK_CORP = "a/corp/corp/corpInfoApp";//查找企业接口
	public static String METHOD_GETCORPLIST = "a/app/corp/getCorpList";//查找企业接口
	public static String METHOD_LOGIN = "a/app/corp/login";//登录
	public static String METHOD_INSPECT = "a/app/inspect/getCorpInspect";//检查
	public static String METHOD_SAVEINSPECT = "a/app/inspect/saveInspect";//保存结果
	public static String METHOD_GETCORP = "a/app/corp/getCorp";//企业详情
	public static String METHOD_GETINSPECTRESULTLIST = "a/app/inspect/getInspectResultList";//检查清单列表
	public static String METHOD_GETCORPINSRES = "a/app/inspect/getCorpInsRes";//检查清单详情
	public static String METHOD_GETCORPTYPES = "a/app/corp/getCorpType";
	public static  String METHOD_SAVECORP = "a/app/corp/saveCorp";
	public static String METHOD_SAVECORPLOW = "a/app/corp/saveCorpLow";
	public static String METHOD_GETVERSION = "a/app/corp/version";
	public static String METHOD_CHECKDETAIL="a/inspect/inspectItem/getzhiFaResult?t=phone&id=";
	public static String METHOD_OANOTIFYLIST= "a/app/oaNotify/getList";//公告列表
	public static String METHOD_LAWSLIST = "a/app/laws/getList";//法律列表
	public static String METHOD_NEWSLIST = "a/app/news/getList";//新闻列表
	public static String METHOD_INDEXLIST = "a/app/corp/index";//首页列表
	public static String METHOD_GETCORPLIST_FIRST = "a/app/corp/getCorpListByztlx";//初次进入列表页数据
	public static String METHOD_SPECIALLIST = "a/app/special/getSpecialList";//执法检查列表
	public static String METHOD_INSPECTDAILYLIST = "a/app/inspectDaily/getResultList ";//
	public static String METHOD_GIODELIST = "a/app/inspectDaily/guide";//检查指南
	public static String METHOD_HISTORYLIST ="a/app/inspectDaily/getHistoryList";//历史记录列表
	public static String METHOD_INSPECTRESULTDETAIL ="a/app/inspectDaily/getInspectResultDetail";//检查详情
	public static String METHOD_GETGZY ="a/app/inspectDaily/getGzy";//告知页接口
	public static String METHOD_GETJCNR ="a/app/inspectDaily/getJcnr";//研判接口
	public static String METHOD_QUESTIONLIST="a/app/inspectDaily/getResultList";//问题处置
	public static String METHOD_UPDATERESULT="a/app/inspectDaily/updateResult";//问题处置状态修改
	public static String METHOD_SPECIALCORPLIST = "a/app/special/getSpecialCorpList";//查找检查企业接口
	public static String METHOD_SAVERESULT = "a/app/inspectDaily/saveResult";//查找检查企业接口
	public static String METHOD_UPLOADIMAGE = "a/app/images/uploadImage";//修改图片
	public static String METHOD_UPDATEPASS = "a/app/corp/updatePass";//修改密码
	public static String METHOD_UPDATEUSER ="a/app/corp/updateUser";//修改信息
	public static String METHOD_UPLOADHEADIMAGE = "a/app/images/updatePhoto";//修改头像
	public static String METHOD_ONOFF = "a/app/inspectDaily/onOff"; //开关
	public static String METHOD_CORPGPRS = "a/app/corp/updateLngLat"; //企业定位
	public static String METHOD_USERGPRS = "a/app/corp/lngLat"; //用户定位
	public static String METHOD_ADDMARKS = "a/app/corp/mapByuid"; //地图加载坐标



}
