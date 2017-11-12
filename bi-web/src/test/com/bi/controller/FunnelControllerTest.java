package com.bi.controller;

import static com.alibaba.fastjson.JSON.parseObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.bi.bean.LabelBean;
import com.bi.entity.EventAnalysisEntity;
import com.bi.entity.FunnelEntity;
import com.bi.service.FunnelService;
import com.bi.utils.SSLClient;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/8/21
 * Created by xianggu.
 *
 * @author xianggu
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-jdbc.xml", "classpath:spring-presto.xml", "classpath:spring-mvc.xml",
        "classpath:spring-redis.xml", "classpath:bi-scheduler.xml"})*/
public class FunnelControllerTest {

    @Resource
    private FunnelService funnelService;

    @Test
    public void queryAnalysisResult() throws Exception {
        EventAnalysisEntity entity = new EventAnalysisEntity();
        entity.setId(564);
        entity.setResultTableName("ibase4j.funnel_analysis_1505358808313");
        entity.setTargetDesc("{\"groupByField\":{\"step\":\"步骤\",\"feea28\":\"坑位号\"},\"allTarget\":{\"uv\":\"UV\",\"col0\":\"商品数量\",\"col1\":\"销售总额\"},\"stepAndDim\":{\"1\":[\"feea28\"],\"2\":[\"feea28\"],\"3\":[\"feea28\"],\"4\":[\"feea28\"]},\"stepNameMap\":{\"1\":\"一级标签页\",\"2\":\"商品详情页\",\"3\":\"加购\",\"4\":\"支付\"},\"date_id\":\"行为时间\",\"steps\":[{\"dimension\":{\"feea28\":\"坑位号\"},\"target\":{},\"step\":\"1\"},{\"dimension\":{},\"target\":{},\"step\":\"2\"},{\"dimension\":{},\"target\":{\"col0\":\"商品数量\"},\"step\":\"3\"},{\"dimension\":{},\"target\":{\"col1\":\"销售总额\"},\"step\":\"4\"}],\"groupFieldByStep\":{\"1\":[\"feea28\"]},\"allTarAndDim\":{\"sum(col1) as col1\":\"销售总额\",\"sum(uv) as uv\":\"UV\",\"step\":\"步骤\",\"sum(col0) as col0\":\"商品数量\",\"feea28\":\"坑位号\"}}");
        String byFieldCondition = "{eventAnalysisId: 564, condition: [{code: \"feea28\", values: [\"1\", \"10\", \"11\"]}]}";
        Map<String, Object> condMap = (Map<String, Object>) JSON.parse(byFieldCondition);
        List<Map<String, Object>> condition = (List<Map<String, Object>>) condMap.get("condition");
        Map<String, Object> result = funnelService.queryAnalysisResult(entity, condition, null, null);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void getGroupFieldByAnalysisResult() {

        EventAnalysisEntity entity = new EventAnalysisEntity();
        entity.setResultTableName("ibase4j.funnel_analysis_1505358808313");
        entity.setTargetDesc("{\"groupByField\":{\"step\":\"步骤\",\"feea28\":\"坑位号\"},\"allTarget\":{\"uv\":\"UV\",\"col0\":\"商品数量\",\"col1\":\"销售总额\"},\"stepAndDim\":{\"1\":[\"feea28\"],\"2\":[\"feea28\"],\"3\":[\"feea28\"],\"4\":[\"feea28\"]},\"stepNameMap\":{\"1\":\"一级标签页\",\"2\":\"商品详情页\",\"3\":\"加购\",\"4\":\"支付\"},\"date_id\":\"行为时间\",\"steps\":[{\"dimension\":{\"feea28\":\"坑位号\"},\"target\":{},\"step\":\"1\"},{\"dimension\":{},\"target\":{},\"step\":\"2\"},{\"dimension\":{},\"target\":{\"col0\":\"商品数量\"},\"step\":\"3\"},{\"dimension\":{},\"target\":{\"col1\":\"销售总额\"},\"step\":\"4\"}],\"groupFieldByStep\":{\"1\":[\"feea28\"]},\"allTarAndDim\":{\"sum(col1) as col1\":\"销售总额\",\"sum(uv) as uv\":\"UV\",\"step\":\"步骤\",\"sum(col0) as col0\":\"商品数量\",\"feea28\":\"坑位号\"}}");
        List<Map<String, Object>> result = funnelService.getGroupFieldByAnalysisResult(entity);
        System.out.println(JSON.toJSONString(result));

    }

    @Test
    public void queryFunnelAllTarget() {
        /*String condition = "{'eventAnalysisId':539,'condition':[{'code':'fpa10','values':['316-男士','312-女装']}]}";*/
        String condition = "{'eventAnalysisId':660,'startDate':'', 'endDate':''}";
        
        try {
            String uri = "http://localhost:8080/funnel/queryFunnelAllTarget";
            Object jsonObject = parseObject(condition);
            System.out.println(jsonObject.toString());
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(condition, "utf-8");
            se.setContentType("text/html");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveFunnelTask() {
        try {
            String uri = "http://localhost:8080/funnel/saveTask";
            HttpClient httpClient = new SSLClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("funnelIds", new int[]{6});
            jsonObject.put("from_date", "2017-09-04");
            jsonObject.put("to_date", "2017-09-04");
            //String conditon = "{\"funnelIds\":[\"5\"],\"from_date\":\"2017-08-24\",\"to_date\":\"2017-08-24\"}";
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(jsonObject.toJSONString(), "utf-8");
            se.setContentType("text/html");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveFunnel() {
        try {
            String uri = "http://localhost:8080/funnel/save";
            FunnelEntity funnelEntity = new FunnelEntity();
            funnelEntity.setName("提交订单到支付转化_new1");
            funnelEntity.setStatus(1);
            funnelEntity.setCondition("{\"by_fields\":[{\"field\":[\"fpa10\"],\"fields_desc\":[\"类目\"],\"step\":\"2\"},{\"field\":[\"fpa73\"],\"fields_desc\":[\"商品来源\"],\"step\":\"3\"}],\"name\":\"提交订单到支付转化\",\"steps\":[{\"filter\":{\"conditions\":[],\"relation\":\"and\"},\"pageLevelId\":\"\",\"code\":\"fp1\",\"step_name\":\"全站\",\"step\":1,\"target\":[{\"code\":\"ft1\",\"tarName\":\"UV\"}]},{\"filter\":{\"conditions\":[{\"path\":[\"fpa10\"],\"params_desc\":[21342,23442],\"function\":\"equal\",\"path_desc\":[\"类目\"],\"params\":[312,316]},{\"path\":[\"fpea6\",\"feea28\"],\"params_desc\":[21342,23442],\"function\":\"equal\",\"path_desc\":[\"商品流\",\"坑位号\"],\"params\":[5,20]}],\"relation\":\"and\"},\"pageLevelId\":1,\"code\":\"fp3\",\"step_name\":\"一级标签页\",\"step\":2,\"target\":[{\"code\":\"ft5\",\"tarName\":\"UV\"}]},{\"filter\":{\"conditions\":[{\"path\":[\"fpa73\"],\"params_desc\":[21342,23442],\"function\":\"equal\",\"path_desc\":[\"商品来源\"],\"params\":[0,1]}],\"relation\":\"and\"},\"pageLevelId\":6,\"code\":\"fp15\",\"step_name\":\"商品详情页\",\"step\":3,\"target\":[{\"code\":\"ft38\",\"tarName\":\"UV\"}]},{\"filter\":{\"conditions\":[],\"relation\":\"and\"},\"pageLevelId\":6,\"code\":\"fp18\",\"step_name\":\"支付\",\"step\":4,\"target\":[{\"code\":\"ft56\",\"tarName\":\"UV\"},{\"code\":\"ft57\",\"tarName\":\"销量\"},{\"code\":\"ft59\",\"tarName\":\"实际支付金额\"}]}],\"convert_time\":\"1\"}");
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(JSON.toJSONString(funnelEntity), "utf-8");
            se.setContentType("text/html");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFunnelAnalysisResult() {
        /*String condition = "{'eventAnalysisId':539,'condition':[{'code':'fpa10','values':['316-男士','312-女装']}]}";*/
        String condition = "{'eventAnalysisId':539,'condition':[]}";


        try {
            String uri = "http://localhost:8080/funnel/getFunnelAnalysisResultNew";
            Object jsonObject = parseObject(condition);
            System.out.println(jsonObject.toString());
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(condition, "utf-8");
            se.setContentType("text/html");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFunnelInfoByFunnelId() {

        try {
            String uri = "http://localhost:8080/funnel/listFunnel/538";
            HttpClient httpClient = new SSLClient();
            HttpGet httpGet = new HttpGet(uri);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFunnelResultDataByTable() {
        try {
            String uri = "http://localhost:8080/funnel/table/539";
            HttpClient httpClient = new SSLClient();
            HttpGet httpGet = new HttpGet(uri);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getLabelData() {
        try {
            String uri = "http://localhost:8080/label/getLabelData";
            LabelBean LabelBean = new LabelBean();
            LabelBean.setLabelId(36);
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(JSON.toJSONString(LabelBean), "utf-8");
            se.setContentType("text/html");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String stringResult = new String(EntityUtils.toString(entity1, "utf-8"));
            System.out.println(stringResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        String s = "[{\"code\":\"fpa10\",\"values\":[21342]},{\"code\":\"feea28\",\"values\":[21342]}," +
                "{\"code\":\"fpa73\",\"values\":[21342]}]";
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(s);
        Object o = defaultJSONParser.parse();
        List<Object> map = (List<Object>) o;
    }

}