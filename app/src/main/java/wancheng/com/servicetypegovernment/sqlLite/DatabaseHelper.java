package wancheng.com.servicetypegovernment.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                "checker1 TEXT," +
                "checker2 TEXT," +
                "checkdate TEXT," +
                "ifBack TEXT," +
                "companySign TEXT," +
                "companySignDate TEXT," +
                "checkSign TEXT," +
                "checkSignDate TEXT," +
                "tzsbId TEXT," +

                "content TEXT" +
                ")");
        //创建检查表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_check (" +
                "id integer primary key autoincrement, " +
                "pid TEXT," +
                "cid TEXT," +
                "isImp INTEGER," +
                "msgId INTEGER," +
                "checkResult INTEGER," +
                "content_sort TEXT," +
                "isDel INTEGER," +
                "checkNote TEXT" +
                ")");
        //创建图片表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_check_image (" +
                "id integer primary key autoincrement, " +
                "checkId INTEGER," +
                "msgId INTEGER," +
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
    public long insertMsg(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put("companyId",map.get("companyId").toString());
        cValue.put("checker1",map.get("checker1").toString());
        cValue.put("checker2",map.get("checker2").toString());
        cValue.put("checkdate", map.get("checkdate").toString());
        cValue.put("ifBack",map.get("ifBack").toString());
        cValue.put("companySign",map.get("companySign").toString());
        cValue.put("companySignDate",map.get("companySignDate").toString());
        cValue.put("checkSign", map.get("checkSign").toString());
        cValue.put("checkSignDate", map.get("checkSignDate").toString());
        cValue.put("content", map.get("content").toString());
        if(map.get("tzsbId")!=null){
            cValue.put("tzsbId", map.get("tzsbId").toString());
        }
        long id=db.insert("tb_msg", null, cValue);
        db.close();
        return id;
    }
    public boolean updataMsg(Map<String,Object> map,long id){
        boolean flag=false;
        SQLiteDatabase db=getWritableDatabase();
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("companyId",map.get("companyId").toString());
            cValue.put("checker1",map.get("checker1").toString());
            cValue.put("checker2",map.get("checker2").toString());
            cValue.put("checkdate",map.get("checkdate").toString());
            cValue.put("ifBack",map.get("ifBack").toString());
            cValue.put("companySign",map.get("companySign").toString());
            cValue.put("companySignDate",map.get("companySignDate").toString());
            cValue.put("checkSign", map.get("checkSign").toString());
            cValue.put("checkSignDate", map.get("checkSignDate").toString());
            cValue.put("content", map.get("content").toString());
            cValue.put("tzsbId", map.get("tzsbId").toString());
            db.update("tb_msg", cValue, "id="+id, null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();

        return flag;
    }
    public boolean findMsg(long id) {
        boolean falg=false;
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> images = null;
        Cursor cursor = db.query("tb_msg", null, "id=" + id, null, null, null, null);
        if( cursor.getCount()>0){
            falg=true;
        }
        db.close();
        return falg;
    }
    public Map<String,Object> findMsgByid(long id) {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,Object>> versionList = null;
        Cursor cursor = db.query("tb_msg", null, "id="+id, null, null,  null, null, null);
        versionList = new ArrayList<Map<String,Object>>();
        while(cursor.moveToNext()){
            Map<String,Object> msg = new HashMap<String,Object>();
            msg.put("id",cursor.getString(cursor.getColumnIndex("id")));
            msg.put("companyId",cursor.getString(cursor.getColumnIndex("companyId")));
            msg.put("checker1",cursor.getString(cursor.getColumnIndex("checker1")));
            msg.put("checker2",cursor.getString(cursor.getColumnIndex("checker2")));
            msg.put("checkdate",cursor.getString(cursor.getColumnIndex("checkdate")));
            msg.put("ifBack",cursor.getString(cursor.getColumnIndex("ifBack")));
            msg.put("companySign",cursor.getString(cursor.getColumnIndex("companySign")));
            msg.put("companySignDate",cursor.getString(cursor.getColumnIndex("companySignDate")));
            msg.put("checkSign",cursor.getString(cursor.getColumnIndex("checkSign")));
            msg.put("checkSignDate",cursor.getString(cursor.getColumnIndex("checkSignDate")));
            msg.put("content", cursor.getString(cursor.getColumnIndex("content")));
            versionList.add(msg);
        }
        db.close();
        Log.e("versionList size", versionList.size() + "");
        if(versionList!=null&&versionList.size()>0){
            return versionList.get(0);
        }
        return null;
    }
    public List<Map<String,Object>> findCheckByMsgid(long id) {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,Object>> versionList = null;
        Cursor cursor = db.query("tb_check", null, "msgId="+id+" and isDel !=1 ", null, null,  null, null, null);
        versionList = new ArrayList<Map<String,Object>>();
        while(cursor.moveToNext()){
            Map<String,Object> msg = new HashMap<String,Object>();
            msg.put("id",cursor.getString(cursor.getColumnIndex("id")));
            msg.put("pid",cursor.getString(cursor.getColumnIndex("pid")));
            msg.put("cid",cursor.getString(cursor.getColumnIndex("cid")));
            msg.put("isImp",cursor.getString(cursor.getColumnIndex("isImp")));
            msg.put("msgId",cursor.getString(cursor.getColumnIndex("msgId")));
            msg.put("checkResult",cursor.getString(cursor.getColumnIndex("checkResult")));
            msg.put("checkNote",cursor.getString(cursor.getColumnIndex("checkNote")));
            msg.put("content_sort",cursor.getString(cursor.getColumnIndex("content_sort")));
            versionList.add(msg);
        }
        db.close();
        return versionList;
    }
    public long findCheck(String pid,String cid,long msgId) {
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor = db.query("tb_check", null, "pid='" + pid + "' and cid='" + cid + "' and msgId=" + msgId, null, null, null, null);
        if( cursor.getCount()>0){
            while(cursor.moveToNext()){
                Map<String,String> image = new HashMap<String,String>();
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                return id;
            }
        }
        db.close();
        return -1;
    }
    public boolean updataCheck(Map<String,Object> map){
        boolean flag=false;
        SQLiteDatabase db=getWritableDatabase();
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("isImp",map.get("isImp").toString());
            cValue.put("checkResult", map.get("checkResult").toString());
            cValue.put("checkNote", map.get("checkNote").toString());
            cValue.put("isDel", map.get("isDel").toString());
            long msgid=Long.parseLong(map.get("msgId").toString());
            String isDel="0";
            if(map.get("isDel")!=null){
                isDel=map.get("isDel").toString();
            }
            db.update("tb_check", cValue, "pid='"+map.get("pid").toString()+"' and cid='"+map.get("cid").toString()+"' and msgId="+msgid +"  and isDel= '"+isDel+"'" , null);
            flag=true;
            //  Log.e("修改","修改了检查表");
        }catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }
    public long insertCheck(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put("pid",map.get("pid").toString());
        cValue.put("cid",map.get("cid").toString());
        cValue.put("isImp",map.get("isImp").toString());
        cValue.put("msgId",map.get("msgId").toString());
        cValue.put("checkResult",map.get("checkResult").toString());
        cValue.put("checkNote", map.get("checkNote").toString());
        cValue.put("content_sort", map.get("content_sort").toString());
        if(map.get("isDel")!=null){
            cValue.put("isDel", map.get("isDel").toString());
        }
        long id=db.insert("tb_check", null, cValue);

        //   Log.e("插入","插入了检查表");
        // Log.e("插入id", id + "");
        return id;
    }
    public boolean delMsg(long id){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            db.delete("tb_msg", "id="+id, null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
    public boolean insertImages(long checkid,String imagepath,long msgId){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("checkId",checkid);
            cValue.put("msgId", msgId);
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
            db.update("tb_app_version", cv, "id=" + id, null);
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
        Log.e("versionList size", versionList.size() + "");
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
    public List<Map<String,String>> findImageByMsgId(long msgId) {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> images = null;
        // Log.e("查询","查询");
        Cursor cursor = db.query("tb_check_image", null, "msgId="+msgId, null, null, null, null);
        images = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> image = new HashMap<String,String>();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int checkId = cursor.getInt(cursor.getColumnIndex("checkId"));
            String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
            String msgId1 = cursor.getString(cursor.getColumnIndex("msgId"));
            image.put("id",id+"");
            image.put("checkId",checkId+"");
            image.put("msgId",msgId1+"");
            image.put("imagePath",imagePath);

            images.add(image);

        }
        db.close();
        return images;
    }
    public List<Map<String,String>> findAllImages() {
        SQLiteDatabase db=getWritableDatabase();
        List<Map<String,String>> images = null;
        Cursor cursor = db.query("tb_check_image", null, null, null, null, null, null);
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
    public boolean deleteImageByCheckId(long checkId){
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
    public boolean deleteImageByMsgId(long msgId){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            db.delete("tb_check_image", "msgId="+msgId, null);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
    public boolean deletCheckeById(long msgId){
        SQLiteDatabase db=getWritableDatabase();
        boolean flag=false;
        try {
            db.delete("tb_check", "msgId="+msgId, null);
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
