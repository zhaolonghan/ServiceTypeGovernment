package wancheng.com.servicetypegovernment.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HANZHAOLONG on 2017/9/7.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String name = "db_check.db"; //数据库名称
    private static final int version = 2; //数据库版本
    public DatabaseHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类

        super(context, name, null, version);

    }
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(name);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建告知表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_msg (" +
                "id integer primary key autoincrement, " +
                "companyId INTEGER," +
                "comoanyAdd TEXT," +
                "checker1 INTEGER," +
                "checker2 INTEGER," +
                "checkdate TEXT," +
                "ifBack TEXT," +
                "companySign TEXT," +
                "companySignDate TEXT," +
                "checkSign TEXT," +
                "checkSignDate TEXT" +
                ")");
        //创建检查表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_check (" +
                "id integer primary key autoincrement, " +
                "pid INTEGER," +
                "cid INTEGER," +
                "isImp INTEGER," +
                "msgId INTEGER," +
                "checkResult INTEGER," +
                "checkNote TEXT" +
                ")");
        //创建图片表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_check_image (" +
                "id integer primary key autoincrement, " +
                "checkId INTEGER," +
                "imagePath TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_app_version (" +
                "id integer primary key autoincrement, " +
                "versionName TEXT,"+
                "versionCode TEXT,"+
                "updataDate TEXT"+
                ")");
      

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列
    }

    public boolean insertImages(int checkid,String imagepath){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("checkId",checkid);
            cValue.put("imagePath", imagepath);
            db.insert("tb_check_image", null, cValue);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }

        db.close();
        return flag;
    }
    public boolean updataVersion(int id,String versionCode){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            ContentValues cv = new ContentValues();
            cv.put("versionCode", versionCode);
            db.update("tb_app_version", cv, "id="+id,null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
    public boolean deleteVersion(int id){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            if(id>0){
                db.delete("tb_app_version", "id="+id, null);
                Log.e("delete version ",1 + "");
            }else{
                db.delete("tb_app_version", null, null);
                Log.e("delete version ", 2 + "");
            }
            flag=true;
        }catch (Exception e){
        }
        db.close();
        return flag;
    }
    public boolean insertVersion(String versionName,String versionCode){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("versionName",versionName);
            cValue.put("versionCode",versionCode);
            cValue.put("updataDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            db.insert("tb_app_version", null, cValue);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;

    }
    public String findVersion() {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> versionList = null;
        Cursor cursor = db.query("tb_app_version", null, null, null, null,  null, "id desc", null);
        versionList = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> image = new HashMap<String,String>();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String versionCode = cursor.getString(cursor.getColumnIndex("versionCode"));
            image.put("id",id+"");
            image.put("versionCode",versionCode+"");
            versionList.add(image);
            Log.e("id version ",id + "");
        }
        db.close();
        Log.e("versionList size", versionList.size()+"");
        if(versionList!=null&&versionList.size()>0){
            Log.e("id version ",versionList.get(0).get("id" + ""));
            return versionList.get(0).get("versionCode");
        }
        return "";
    }
    public List<Map<String,String>> findVersionList() {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> versionList = null;
        Cursor cursor = db.query("tb_app_version", null, null, null, null, null, null);
        versionList = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> image = new HashMap<String,String>();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String versionName = cursor.getString(cursor.getColumnIndex("versionName"));
            String versionCode = cursor.getString(cursor.getColumnIndex("versionCode"));
            image.put("id",id+"");
            image.put("versionName",versionName+"");
            image.put("versionCode",versionCode+"");
            versionList.add(image);
        }
        db.close();
      
        return versionList;
    }
    /**

     * 查，查询表中所有的数据

     */

    public List<Map<String,String>> find(int checkid) {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> images = null;
        Cursor cursor = db.query("tb_check_image", null, "checkId="+checkid, null, null, null, null);
            images = new ArrayList<Map<String,String>>();
            while(cursor.moveToNext()){
                Map<String,String> image = new HashMap<String,String>();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int checkId = cursor.getInt(cursor.getColumnIndex("checkId"));
                String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
                image.put("id",id+"");
                image.put("checkId",checkId+"");
                image.put("imagePath",imagePath);

                images.add(image);

            }
        db.close();
        return images;
    }

    public boolean deleteByCheckId(int checkId){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
           db.delete("tb_check_image", "checkId="+checkId, null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
    public boolean deleteById(int id){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            db.delete("tb_check_image", "id="+id, null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
}