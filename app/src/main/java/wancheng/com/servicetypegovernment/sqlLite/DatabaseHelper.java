package wancheng.com.servicetypegovernment.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final int version = 1; //数据库版本
    public DatabaseHelper(Context context) {

        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类

        super(context, name, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db=getWritableDatabase();
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
        db.close();

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