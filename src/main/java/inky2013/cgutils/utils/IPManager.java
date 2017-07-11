package inky2013.cgutils.utils;

import inky2013.cgutils.CGUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan Brews on 29/06/2017.
 */
public class IPManager {

    public static String getServerAddress(URLData urlData) {

        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20 * 1000).build();
            HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpPost httppost = new HttpPost(urlData.url);
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);

            for (int i=0;i<urlData.formdata.size();i++) {
                params.add(new BasicNameValuePair(urlData.formdata.get(i).get(0), urlData.formdata.get(i).get(1)));
            }

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException err) {
            CGLogger.error(err);
        }
        return null;
    }

    public static void uploadServerAddress(URLData urlData) {

        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20 * 1000).build();
            HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpPost httppost = new HttpPost(urlData.url);
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);

            for (int i=0;i<urlData.formdata.size();i++) {
                params.add(new BasicNameValuePair(urlData.formdata.get(i).get(0), urlData.formdata.get(i).get(1)));
            }
            String ipaddress = getGlobalIpAddress();
            params.add(new BasicNameValuePair("address", ipaddress));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httpclient.execute(httppost);
            CGLogger.info("Sent request to "+urlData.url+" with address: "+ipaddress);
        } catch (IOException err) {
            CGLogger.error(err);
        }

    }

    public static void uploadServerAddress(String command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
        } catch (IOException err) {
            CGLogger.error(err);
        }
    }

    private static String getGlobalIpAddress() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
        HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpget = new HttpGet(CGUtils.globalIPAddressCheckUrl);
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException err) {
            CGLogger.error("IOException getting global IP address");
        }
        return null;
    }

}
