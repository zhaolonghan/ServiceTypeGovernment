package wancheng.com.servicetypegovernment.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * getSharedPreferences 帮助单元
 * 
 */
public class PreferenceUtil {
    public static String PREFERENCE_NAME = "AndroidHuoseUtil";
//    public static String HAVA_UNREAD_ACTIVE = "hava_unread_active";
	//防止实例化
	private PreferenceUtil(){
		throw new AssertionError();
	}
	/**
	 * 兼容2.0之前版本数据存储格式：先读取老版本格式数据，有就备份到新的版本里，没有再去读取新文件里的数据
	 * @param mcontext
	 * @param colum
	 * @return
	 */
	public static String getString(Context mcontext, String colum) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			String sResult = pre.getString(colum, "");
			return sResult;
		} catch (Exception e) {
		}
		return "";
	}

	public static boolean putString(Context mcontext, String colum, String value) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			editor.putString(colum, value);
			return editor.commit();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}
	public static boolean putStringArray(Context mcontext,Map<String, String> valuePairs){
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			if(valuePairs != null){
				for(Map.Entry<String, String> entry:valuePairs.entrySet()){
					editor.putString(entry.getKey(), entry.getValue());
				}
			}
			return editor.commit();
		} catch (Exception e) {
		}
		return false;
	}
	public static boolean putStringArray(String PreferencesName, Context mcontext , Map<String, String> valuePairs){
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PreferencesName, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			if(valuePairs != null){
				for(Map.Entry<String, String> entry:valuePairs.entrySet()){
					editor.putString(entry.getKey(), entry.getValue());
				}
			}
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean putLong(Context mcontext, String colum, Long value) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			editor.putLong(colum, value);
			return editor.commit();
		} catch (Exception e) {
		}
		return false;
	}

	public static Long getLong(Context mcontext, String colum) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Long lResult = pre.getLong(colum, 0);
			return lResult;
		} catch (Exception e) {
		}
		return 0L;
	}
	public static int getIntger(String PreferencesName,Context mcontext, String colum,int defaultValue) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PreferencesName, Context.MODE_PRIVATE);
			int lResult =  pre.getInt(colum, defaultValue);
			return lResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static String getString(String PreferencesName,Context mcontext, String colum,String defaultValue) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PreferencesName, Context.MODE_PRIVATE);
			String lResult =  pre.getString(colum, defaultValue);
			return lResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	
	public static boolean  putBoolean(Context mcontext, String colum, Boolean value) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			editor.putBoolean(colum, value);
			return editor.commit();
		} catch (Exception e) {
		}
		return false;
	}
	
	public static boolean getBoolean(Context mcontext, String colum) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Boolean lResult = pre.getBoolean(colum,false);
			return lResult;
		} catch (Exception e) {
		}
		return false;
	}
	public static boolean putFloat(Context mcontext, String colum, float value) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			editor.putFloat(colum,  value);
			return editor.commit();
		} catch (Exception e) {
		}
		return false;
	}
	
	public static float getFloat(Context mcontext, String colum) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			float lResult = pre.getFloat(colum,0f);
			return lResult;
		} catch (Exception e) {
		}
		return 0f;
	}
	/**
	 *获取整数，defaultvale : 0
	 * @param mcontext
	 * @param colum
	 * @return
	 */
	public static int getInt(Context mcontext, String colum) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			int lResult = pre.getInt(colum,0);
			return lResult;
		} catch (Exception e) {
		}
		return 0;
	}
	public static boolean putInt(Context mcontext, String colum, int value) {
		try {
			SharedPreferences pre = mcontext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = pre.edit();
			editor.putInt(colum, value);
			return editor.commit();
		} catch (Exception e) {
		}
		return false;
	}
	
}
