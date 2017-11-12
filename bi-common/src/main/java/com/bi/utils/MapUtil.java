package com.bi.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/8/28
 * Created by xianggu.
 *
 * @author xianggu
 */
public final class MapUtil {

    private MapUtil() {

    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static <V> Map<String, V> sortMapByKey(Map<String, V> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, V> sortMap = new TreeMap<String, V>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static <V> Map<String, V> sortMapByKey(Map<String, V> map, Comparator<String> comparator) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, V> sortMap = new TreeMap<String, V>(comparator);
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 对Map的Value字段进行排序
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
