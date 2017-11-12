package com.bi.utils;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/7
 * Created by admin.
 *
 * @author admin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-jdbc.xml", "classpath:spring-mvc.xml", "classpath:spring-redis.xml"})
public class RedisCacheUtilTest {

    @Resource
    RedisTemplate redisTemplate;

    //@Before
    public void setUp() throws Exception {
        redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
    }

    @Test
    public void testSet() throws Exception {
        redisTemplate.opsForValue().set("aaa2", "bbbbb1");
    }

}