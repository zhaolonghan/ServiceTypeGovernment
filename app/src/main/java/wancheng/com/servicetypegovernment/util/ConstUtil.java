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
	//public static String IP = "http://223.71.241.40/";
	public static String IP = "http://192.168.1.101:8181/";

	/**
	 * METHOD
	 */

	public static String METHOD_CHECK_CORP = "a/corp/corp/corpInfoApp";//查找企业接口
	public static String METHOD_GETCORPLIST = "a/app/corp/getCorpList";//查找企业接口
	public static String METHOD_LOGIN = "a/app/corp/login";//登录
	public static String METHOD_INSPECT = "a/app/inspect/getCorpInspect";//检查
	public static String METHOD_SAVEINSPECT = "a/app/inspect/saveInspect";//保存结果
	public static String METHOD_GETCORP = "a/app/corp/getCorp";//企业详情
	public static String METHOD_GETINSPECTRESULTLIST = "a/app/inspect/getInspectResultList";//检查清单列表
	public static String METHOD_GETCORPINSRES = "a/app/inspect/getCorpInsRes";//检查清单详情
	public static String METHOD_GETCORPTYPES = "a/app/corp/getCorpType";
	public static String METHOD_SAVECORP = "a/app/corp/saveCorp";
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

}
