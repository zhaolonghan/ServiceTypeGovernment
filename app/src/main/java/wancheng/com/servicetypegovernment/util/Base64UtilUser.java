package wancheng.com.servicetypegovernment.util;


import android.util.Base64;

public class Base64UtilUser {

private static final String systemLineSeparator = System.getProperty("line.separator");

   public static  String  encode(String reg){
      reg= Base64.encodeToString(reg.getBytes(), Base64.DEFAULT);
      return reg;
   }
   public static  String  decode(String reg){
      reg =new String(Base64.decode(reg, Base64.DEFAULT));
      return reg;
   }
}