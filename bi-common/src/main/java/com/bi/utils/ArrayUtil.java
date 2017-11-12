package com.bi.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * <p>
 * <b>ArrayUtil</b><br/>
 * </p>
 * <p>
 * </p>
 *
 * @author xianggu
 */
public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static <T> boolean contains(T[] array, T find) {
        if (find == null) {
            return false;
        }

        if (isEmpty(array)) {
            return false;
        }
        for (T e : array) {
            if (e.equals(find)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T[] create(Class<T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }

    public static <T> String toString(T[] array, char split) {
        if (ArrayUtil.isEmpty(array)) {
            return "";
        }
        T e = null;
        StringBuilder result = new StringBuilder();
        for (int i = 0, j = array.length; i < j; i++) {
            e = array[i];
            if (e != null) {
                result.append(String.valueOf(e));
                if (i < j - 1) {
                    result.append(split);
                }
            }
        }
        return result.toString();
    }

    public static <T> String[] toString(List<T> array) {
        String[] t = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            t[i] = String.valueOf(array.get(i));
        }
        return t;
    }

    public static <T> String toString(T[] array, String split) {
        if (ArrayUtil.isEmpty(array)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, j = array.length; i < j; i++) {
            result.append(array[i].toString());
            if (i < j - 1) {
                result.append(split);
            }
        }
        return result.toString();
    }

    public static <T, K> List<T> distinctArray(List<T> list, ObjectTransformer<T, K> objectTransformer) {
        List<K> listTemp = new ArrayList<K>();
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T a = it.next();
            K k = objectTransformer.transform(a);
            if (listTemp.contains(k)) {
                it.remove();
            } else {
                listTemp.add(k);
            }
        }
        return list;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isEmpty(T[] array) {
        if (array == null) {
            return true;
        }
        if (array.length == 0) {
            return true;
        }
        return false;
    }

    public interface ObjectTransformer<O, R> {
        R transform(O obj);
    }

}
