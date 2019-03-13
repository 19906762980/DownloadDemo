package com.sunyard;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Download2 {
	public final static void main(String[] args) throws Exception { 
        _getContentLengthClose("http://www.dowei.com/d/file/tuku/meinv/2016-01-26/1453788622507000.jpg");
    } 

    private static void _getContentLengthClose(String url) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();//新方法
        try { 
            HttpGet httpget = new HttpGet(url);
            //在请求中明确定义不要进行压缩
           httpget.setHeader("Accept-Encoding", "identity");

            CloseableHttpResponse response =httpclient.execute(httpget); //新方法
            HttpEntity entity =response.getEntity(); 
           System.out.println(response.getStatusLine()); 
            String webContent = "";
            if (entity != null) { 
            webContent= EntityUtils.toString(entity);
                System.out.println("Responsecontent    length: " +entity.getContentLength()); 
                System.out.println("ResponsetoString() length: " + webContent.length()); 
            } 
            httpget.abort();  
        }
        finally {         
        httpclient.close();
        }   
    }

}
