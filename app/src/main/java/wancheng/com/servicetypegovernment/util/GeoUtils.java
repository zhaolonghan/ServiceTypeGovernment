package wancheng.com.servicetypegovernment.util;


/**
 * Created by louis on 2014/9/2.
 */
public class GeoUtils {
    /**
     * ��������γ�ȵ�֮��ľ��루��λ���ף�
     * @param lng1  ����
     * @param lat1  γ��
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// ȡWGS84��׼�ο������еĵ��򳤰뾶(��λ:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }



}
