package com.datang.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 11:03
 * @Version 1.0
 **/
@Slf4j
public class HttpClientUtils {

    private static final int REQ_CONNECT_TIMEOUT = 50000;
    private static final int REQ_SOCKET_TIMEOUT = 30000;
    private static final int RES_SUCCEED_CODE = 200;

    public static String httpPost(String url,String jsonstr){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        String resultString="";
        try{
            httpclient =  HttpClientUtils.sslClient();

            StringEntity postingString = new StringEntity(jsonstr,"utf-8");// json传递
            postingString.setContentType("text/json");
            postingString.setContentEncoding(new BasicHeader("Content-Type", "application/json"));

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-Type", "application/json");
            response = httpclient.execute(httpPost);
            if(response != null && response.getStatusLine().getStatusCode() == RES_SUCCEED_CODE)
            {
                HttpEntity entity = response.getEntity();
                resultString = entityToString(entity);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String httpGet(String url){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        String resultString="";
        try{
            RequestConfig config = RequestConfig.custom().setConnectTimeout(REQ_CONNECT_TIMEOUT).setSocketTimeout(REQ_SOCKET_TIMEOUT).build();
            httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet httpGet = new HttpGet(url);
            response = httpclient.execute(httpGet);
            if(response != null && response.getStatusLine().getStatusCode() == RES_SUCCEED_CODE)
            {
                HttpEntity entity = response.getEntity();
                resultString = entityToString(entity);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * @Description: 主要目的请求头带cookie可访问12306
     * @Author: lc
     * @Date: 2021/11/20 16:53
     **/
    public static String httpCookieGet(String url){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        String resultString="";
        try{
            RequestConfig config = RequestConfig.custom().setConnectTimeout(REQ_CONNECT_TIMEOUT).setSocketTimeout(REQ_SOCKET_TIMEOUT).build();
            httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
            httpGet.setHeader("Cookie","_uab_collina=WX:xiaosin2333; JSESSIONID=WX:xiaosin2333;");
            response = httpclient.execute(httpGet);
            if(response != null && response.getStatusLine().getStatusCode() == RES_SUCCEED_CODE)
            {
                HttpEntity entity = response.getEntity();
                resultString = entityToString(entity);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     *  输出字符编码转换
     * @param entity
     * @return
     * @throws IOException
     */
    private static String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if(entity != null)
        {
            long lenth = entity.getContentLength();
            if(lenth != -1 && lenth < 2048)
            {
                result = EntityUtils.toString(entity,"UTF-8");
            }else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }

    /**
     * 跳过ssl认证证书
     * @return
     */
    public static CloseableHttpClient sslClient(){
        RequestConfig config = RequestConfig.custom().setConnectTimeout(REQ_CONNECT_TIMEOUT).setSocketTimeout(REQ_SOCKET_TIMEOUT).build();
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).setDefaultRequestConfig(config).build();
            return httpclient;
        } catch (Exception e) {
            log.error("绕过证书异常", e);
            return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        }
    }
}
