package com.bi.buildsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/12
 * Created by xianggu.
 *
 * @author xianggu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-jdbc.xml",
        "classpath:spring-presto.xml"})
public class PrestoConnectTest {

    private Connection conn;

    private String TBL = "dw." + "dim_page";
    private String sql = String.format("select * from %s limit 10", TBL);

    @Resource
    @Qualifier("prestoTemplate")
    private JdbcTemplate prestoTemplate;

    @Test
    public void select() throws SQLException {

       /* System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", "squid.bi.org");
        System.getProperties().put("proxyPort", "3128");
        Authenticator.setDefault(new BasicAuthenticator("yunmo", "0293745t0klaysdv9gws8o"));*/
        
        System.out.println(sql);
        DataSource dataSource = prestoTemplate.getDataSource();
        conn = dataSource.getConnection();
        conn.setCatalog("hive");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 执行查询数据库接口
//        Connection connection = DriverManager.getConnection(url,"hadoop","hadoop");
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("----------" + sdf.format(new Date()) + "----------");
        List<Map<String, Object>> maps = prestoTemplate.queryForList(sql);

        JSONArray arr = new JSONArray();
        for (Map<String, Object> map : maps) {
            JSONObject itemJSONObj = new JSONObject(map);
            arr.add(itemJSONObj);
        }
        System.out.println(arr.toJSONString());
        String alterTableStr = String.format("update od_table set datas='%s' where table_name = 'dim_page' and database_name = 'dw' ", arr.toJSONString());
        System.out.println("----------" + sdf.format(new Date()) + "----------");
    }

    /**
     * 关闭连接
     *
     * @throws java.sql.SQLException
     */
    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
