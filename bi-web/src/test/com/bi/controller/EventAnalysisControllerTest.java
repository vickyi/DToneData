package com.bi.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bi.utils.SSLClient;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/17
 * Created by xianggu.
 *
 * @author xianggu
 */
public class EventAnalysisControllerTest {
    @Test
    public void list() throws Exception {

    }


    @Test
    public void info() throws Exception {
    }

    @Test
    public void save() throws Exception {
        String uri = "http://localhost:8080/eventanalysis/save";
        String condision = "{\"measures\": [{\n" +
                "\t\t\"gmeId\": \"6\",\n" +
                "\t\t\"eventName\": \"浏览首页\",\n" +
                "\t\t\"tarId\": \"tar101001\",\n" +
                "\t\t\"tarName\": \"PV\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"gmeId\": \"6\",\n" +
                "\t\t\"eventName\": \"浏览首页\",\n" +
                "\t\t\"tarId\": \"tar101002\",\n" +
                "\t\t\"tarName\": \"UV\"\n" +
                "\t}],\n" +
                "\t\"filter\": {\n" +
                "\t\t\"relation\": \"and\",\n" +
                "\t\t\"conditions\": [{\n" +
                "\t\t\t\"attrId\": \"attr101002\",\n" +
                "\t\t\t\"attrName\": \"使用终端\",\n" +
                "\t\t\t\"function\": \"equal\",\n" +
                "\t\t\t\"params\": [2,3]\n" +
                "\t\t},{\n" +
                "            \"attrId\": \"attr101001\",\n" +
                "\t\t\t\"attrName\": \"浏览站点\",\n" +
                "\t\t\t\"function\": \"equal\",\n" +
                "\t\t\t\"params\": [1,2]\n" +
                "        }]\n" +
                "\t},\n" +
                "\t\"by_fields\": [\"attr101002\",\"attr101001\"],\n" +
                "\t\"from_date\": \"2017-07-23\",\n" +
                "\t\"to_date\": \"2017-07-23\"}";


        Object jsonObject = JSON.parseObject(condision);
        System.out.println(jsonObject.toString());
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(uri);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("condition", condision));
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
        System.out.println(stringResult);

    }

    @Test
    public void importAnalysisResult() throws Exception {
        String uri = "http://localhost:8080/eventanalysis/import/17";
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(uri);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
        System.out.println(stringResult);

    }

    @Test
    public void getEventAnalysisInfo() throws Exception {
        String uri = "http://localhost:8080/eventanalysis/getEventAnalysisInfo";
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(uri);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
        System.out.println(stringResult);
    }
    
    @Test
    public void getEventAnalysisResult() throws Exception {
        String uri = "http://localhost:8080/eventanalysis/getEventAnalysisResult/17";
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(uri);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
        System.out.println(stringResult);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
        String start = "2014-01-03";
        String end = "2014-03-05";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> listDate = getDatesBetweenTwoDate(dBegin, dEnd);
        for (int i = 0; i < listDate.size(); i++) {
            System.out.println(sdf.format(listDate.get(i)));
        }
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);
        return lDate;
    }
}