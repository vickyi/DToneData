package com.bi.utils;

import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/14
 * Created by xianggu.
 *
 * @author xianggu
 */
public class SSLClientTest {

    @Test
    public void httpRequest() throws Exception {
        try {
            SSLClient httpClient = new SSLClient();
            URI uri = new URI("http://b9603b9ece8b.bi.org/PublicApi/getAccessByUid");
            HttpPost httpPost = new HttpPost(uri);
            StringEntity entity = new StringEntity("uid=22989");
            entity.setContentType("application/x-www-form-urlencoded");

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String stringResult = new String(EntityUtils.toString(resEntity, "utf-8"));
                    Map<String, Object> o = (Map<String, Object>) JSON.parse(stringResult);
                    String code = (String) o.get("status");
                    if ("1000".equals(code)) {
                        Map<String, String> data = (Map<String, String>) o.get("data");
                        String username = data.get("username");

                    } else {

                    }
                    //System.out.println(stringResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getUid() throws Exception {
        /*try {
            SSLClient httpClient = new SSLClient();
            URI uri = new URI("http://b9603b9ece8b.bi.org/publicApi/login");
            HttpPost httpPost = new HttpPost(uri);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new BasicNameValuePair("username", "六行"));
            nameValuePairList.add(new BasicNameValuePair("password", "123457"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String stringResult = new String(EntityUtils.toString(resEntity, "utf-8"));
                    Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(stringResult);
                    int status = (int) resultMap.get("status");
                    Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                    String uid = (String) dataMap.get("uid");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        int a = (int) (Math.random() * (9000)) + 1000;
        System.out.println(a);

    }

}