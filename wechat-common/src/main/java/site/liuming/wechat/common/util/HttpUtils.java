package site.liuming.wechat.common.util;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定义一些常用的get和post请求方法
 *
 * @author liuming
 * @date 2017/11/15 10:46
 */
public class HttpUtils {

    /**
     * get方式请求给定url
     *
     * @param url 请求目标url
     * @return 请求响应的字符串内容
     * @throws IOException
     */
    public static String doGet(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        response.close();
        return result;
    }

    /**
     * post方式请求给定url，请求参数以键值对的形式保存在Map中
     *
     * @param url    请求的url
     * @param params 保存请求参数的map
     * @return 请求响应的字符串内容
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        response.close();
        return result;
    }

    /**
     * post方式请求给定url，请求参数为字符串内容
     *
     * @param url       请求的url
     * @param paramsStr 字符串内容的请求参数
     * @return 请求响应的字符串内容
     * @throws IOException
     */
    public static String doPost(String url, String paramsStr) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(paramsStr, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        response.close();
        httpClient.close();
        return result;
    }

}
