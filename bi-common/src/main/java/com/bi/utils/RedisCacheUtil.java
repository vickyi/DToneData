package com.bi.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/7
 * Created by xianggu.
 *
 * @author xianggu
 */
@Component
public class RedisCacheUtil {

    private static RedisTemplate redisTemplate;

    /**
     * 注解static属性,无法直接注入，需要增加的普通属性
     */
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplateTemp;

    /**
     * IoC容器实例化当前受管Bean时，@PostConstruct注解的方法会被自动触发
     */
    @PostConstruct
    public void init() {
        redisTemplate = this.redisTemplateTemp;
    }

    /**
     * 正则匹配获取redis key 集合
     *
     * @param pattern 正则
     * @return
     */
    public static Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除key及对应的值
     *
     * @param key
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param key
     */
    public static void delete(Collection<String> key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 存储数据到redis
     *
     * @param key
     * @param obj
     */
    public static void set(String key, Object obj) {
        if (StringUtils.isEmpty(key) || obj == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, obj);

    }

    /**
     * 存储数据到redis,并设置超时时间
     *
     * @param key
     * @param obj
     * @param timeout
     * @param unit
     */
    public static void setTime(String key, Object obj, Long timeout, TimeUnit unit) {
        if (StringUtils.isEmpty(key) || obj == null) {
            return;
        }

        if (timeout != null) {
            redisTemplate.opsForValue().set(key, obj, timeout, unit);
        } else {
            redisTemplate.opsForValue().set(key, obj);
        }
    }

    public static void setList(String key, List<Object> list) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(list)) {
            return;
        }

        ListOperations<String, Object> lists = redisTemplate.opsForList();
        for (Object o : list) {
            lists.rightPush(key, o);
        }
    }

    public static List<Object> getList(String key, long start, long end) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        ListOperations<String, Object> lists = redisTemplate.opsForList();
        return lists.range(key, start, end);
    }

    public static void setHashMap(String key, Map<String, Object> map) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(map)) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, map);
    }

    public static Object getHashMapValue(String key, String mapKey) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(mapKey)) {
            return "";
        }
        return redisTemplate.opsForHash().get(key, mapKey);
    }

    public RedisTemplate getRedisTemplateTemp() {
        return redisTemplateTemp;
    }

    public void setRedisTemplateTemp(RedisTemplate redisTemplateTemp) {
        this.redisTemplateTemp = redisTemplateTemp;
    }
}
