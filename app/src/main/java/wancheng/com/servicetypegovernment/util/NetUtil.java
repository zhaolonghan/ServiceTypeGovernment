package wancheng.com.servicetypegovernment.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;


/**
 * 数据交互工具类<br>
 * <br>
 * 
 * @author hanlaishuo
 * 
 */
public class NetUtil  {
	/*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());    
        return resultData;
    }
	/*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), encode))
                            .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
	/*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(HashMap<String, String> params, String encode, String method) {
        
        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
      //  Log.e("getRequestData(params, encode).toString()", getRequestData(params, encode).toString());
        try {            

			String url = ConstUtil.IP+method;
			URL realUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)realUrl.openConnection();
            httpURLConnection.setConnectTimeout(3000);//设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            
            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

	public  String sendPost(String method , Map<String,Object> map)
	{
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try
		{
			String url = ConstUtil.IP+method;
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			if(!appendParams(map,null).equals(null)){
				out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				out.print(appendParams(map,null));  //②
				// flush输出流的缓冲
				out.flush();}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result +=  line;
			}
		}
		catch (Exception e)
		{
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	public String posturl(String method , Map<String,Object> map){
		InputStream is = null;
		String result = "";
		String url = ConstUtil.IP+method+appendParams(map,'?');
		Log.e("请求的接口地址", url);
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					10000); // 请求超时设置
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 10000);// 连接超时
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			return "Fail to establish http connection!"+e.toString();
		}

		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			Log.v("返回值",result.toString());
		}catch(Exception e){
			return "Fail to convert net stream!";
		}

		return result;
	}
	public String posturl1(String method){
		InputStream is = null;
		String result = "";
		String url = ConstUtil.IP+method;
		Log.e("请求的接口地址", url);
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					10000); // 请求超时设置
			httpclient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 10000);// 连接超时
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			return "Fail to establish http connection!"+e.toString();
		}

		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			Log.v("返回值",result.toString());
		}catch(Exception e){
			return "Fail to convert net stream!";
		}

		return result;
	}

	public String appendParams(Map<String,Object> map,Character flag){
		if(map == null){
			throw new IllegalArgumentException("Map is null!");
		}
		StringBuilder sb = new StringBuilder(flag==null?"":flag.toString());
		for(Iterator<String> it = map.keySet().iterator();it.hasNext();){
			String key = it.next();
			sb.append(key).append('=').append(map.get(key)==null?"":map.get(key)).append('&');
		}
		if(sb.charAt(sb.length()-1)=='&'){
			sb.deleteCharAt(sb.length()-1);
		}
		Log.e("sb.toString()", sb.toString());
		return sb.toString();
	}

}

