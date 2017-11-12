package com.bi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

/**
 * <p>
 * <b>CollectionUtil</b><br/>
 * </p>
 * <p>
 * <br/>
 * </p>
 *
 * @author xianggu
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <T> T get(Collection<T> collection, int index) {
        if (index < 0) {
            return null;
        }
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        if (index >= collection.size()) {
            return null;
        }
        return (T) CollectionUtils.get(collection, index);
    }

    public static <T> String toString(List<T> list, String split) {
        return toString(list, split, null);
    }
    
    public static <T> String toString(List<T> list) {
        return toString(list, ",", null);
    }

    public static <T> String toString(List<T> list, StringSpecifier<T> specifier) {
        return toString(list, ",", specifier);
    }

    public static <T> String toString(List<T> list, String split, StringSpecifier<T> specifier) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int count = 1;
        int size = list.size();
        for (T entity : list) {
            result.append(specifier == null ? entity.toString() : specifier.specify(entity));
            if (count++ < size) {
                result.append(split);
            }
        }
        return result.toString();
    }

    public static <T> T first(List<T> list) {
        return first(list, null);
    }

    public static <T> T first(List<T> list, T defaultValue) {
        if (CollectionUtils.isEmpty(list)) {
            return defaultValue;
        }
        return list.get(0);
    }

    public static <T> T last(List<T> list) {
        return last(list, null);
    }

    public static <T> T last(List<T> list, T defaultValue) {
        if (CollectionUtils.isEmpty(list)) {
            return defaultValue;
        }
        return list.get(list.size() - 1);
    }

    public static <O, R> List<R> transform(List<O> sources, ElementTransformer<O, R> elementTransformer) {
        List<R> results = CollectionUtils.isEmpty(sources) ? new ArrayList<R>(0) : new ArrayList<R>(sources.size());
        for (O element : sources) {
            CollectionUtil.addToList(elementTransformer.transform(element), results);
        }
        return results;
    }

    public static <K, V> Map<K, V> toMap(List<V> list, MapKeySpecifier<K, V> mapKeySpecifier) {
        Map<K, V> map = new HashMap<K, V>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        map = new HashMap<K, V>(list.size());
        for (V e : list) {
            map.put(mapKeySpecifier.specify(e), e);
        }
        return map;
    }

    public static <K, V, T> Map<K, V> toMap(List<T> list, MapKeyValueSpecifier<K, V, T> specifier) {
        Map<K, V> map = new HashMap<K, V>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        map = new HashMap<K, V>(list.size());
        for (T e : list) {
            map.put(specifier.specifyKey(e), specifier.specifyValue(e));
        }
        return map;
    }

    public static <K, V, T> Map<K, V> group(List<T> list, MapKeyValueSpecifier<K, V, T> specifier) {
        Map<K, V> map = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        map = new LinkedHashMap<>(list.size());

        for (T e : list) {
            map.put(specifier.specifyKey(e), specifier.specifyValue(e));
        }
        return map;
    }

    public static <K, V> Map<K, List<V>> group(List<V> list, GroupKeySpecifier<K, V> groupKeySpecifier) {
        Map<K, List<V>> map = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        K key = null;
        List<V> innerList = null;
        for (V e : list) {
            key = groupKeySpecifier.specify(e);
            if (key == null) {
                continue;
            }
            if (map.containsKey(key)) {
                innerList = map.get(key);
            } else {
                innerList = new LinkedList<>();
                map.put(key, innerList);
            }
            innerList.add(e);
        }
        return map;
    }

    public static <T> List<T> merge(List<T> ltl, List<T> rtl, ListMerger<T> merger) {
        int ltlSize = 0;
        int rtlSize = 0;
        if (CollectionUtils.isNotEmpty(ltl)) {
            ltlSize = ltl.size();
        }
        if (CollectionUtils.isNotEmpty(rtl)) {
            rtlSize = rtl.size();
        }
        List<T> result = new ArrayList<T>(Math.max(ltlSize, rtlSize));
        List<T> shorter = null;
        List<T> longer = null;
        int minSize = 0;
        if (ltlSize > rtlSize) {
            longer = ltl;
            shorter = rtl;
            minSize = rtlSize;
        } else if (ltlSize == rtlSize) {
            longer = ltl;
            shorter = rtl;
            minSize = ltlSize;
        } else if (ltlSize < rtlSize) {
            longer = rtl;
            shorter = ltl;
            minSize = ltlSize;
        }

        for (int i = 0; i < minSize; i++) {
            result.add(merger.merge(ltl.get(i), rtl.get(i)));
        }
        for (int i = 0, j = Math.abs(ltlSize - rtlSize); i < j; i++) {
            result.add(longer.get(minSize + i));
        }
        return result;
    }

    public static <R> R[] toArray(List<R> list, Class<R> arrayType) {
        if (CollectionUtils.isEmpty(list)) {
            return ArrayUtil.create(arrayType, 0);
        }
        int size = list.size();
        R[] array = ArrayUtil.create(arrayType, size);
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static <R, E> R[] toArray(List<E> list, Class<R> arrayType, ValuePicker<R, E> valuePicker) {
        if (CollectionUtils.isEmpty(list)) {
            return ArrayUtil.create(arrayType, 0);
        }
        int size = list.size();
        R[] r = ArrayUtil.create(arrayType, size);
        for (int i = 0; i < size; i++) {
            r[i] = valuePicker.pick(list.get(i));
        }
        return r;
    }

    public static <T> List<T> unmodifiableEmptyList() {
        return Collections.unmodifiableList(new ArrayList<T>());
    }

    public static <T> void addToListIfAbsent(final T source, final List<T> target) {
        if (source == null) {
            return;
        }
        if (target == null) {
            return;
        }
        if (target.contains(source)) {
            return;
        }
        target.add(source);
    }

    public static <T> void addToList(final List<T> target, final T... sources) {
        if (target != null) {
            if (ArrayUtil.isNotEmpty(sources)) {
                for (T e : sources) {
                    addToList(e, target);
                }
            }
        }
    }

    /**
     * 添加元素至列表中
     *
     * @param value        元素
     * @param defaultValue 默认元素（如果元素为空，则将默认元素加入列表）
     * @param list         列表
     * @param <T>
     */
    public static <T> void addToList(final T value, final T defaultValue, final List<T> list) {
        if (value == null) {
            addToList(defaultValue, list);
        } else {
            list.add(value);
        }
    }

    /**
     * 添加元素至列表中
     *
     * @param e
     * @param list
     * @param <T>
     */
    public static <T> void addToList(final T e, final List<T> list) {
        if (e != null && list != null) {
            list.add(e);
        }
    }

    public static <T> void addToList(final T e, final List<T> list, AddToListValidator<T> validator) {
        if (e != null && list != null) {
            if (validator != null) {
                if (validator.validate(e, list)) {
                    list.add(e);
                }
            }
            list.add(e);
        }
    }

    public static <T> void addToList(final List<T> target, final List<T> sources) {
        if (target != null) {
            if (CollectionUtils.isNotEmpty(sources)) {
                for (T e : sources) {
                    addToList(e, target);
                }
            }
        }
    }

    public static <T> void iterate(Iterator<T> iterator, IteratorCallback<T> callback) {
        if (iterator != null) {
            int index = 0;
            T entity = null;
            while (iterator.hasNext()) {
                entity = iterator.next();
                callback.call(entity, new Iterate(iterator, index));
                index++;
            }
        }
    }

    public interface AddToListValidator<T> {
        boolean validate(T e, List<T> list);
    }

    /**
     * 分组键指示器
     *
     * @param <K> 键类型
     * @param <T> 值类型
     */
    public interface GroupKeySpecifier<K, T> {
        /**
         * 指定键的值
         *
         * @param entity 值
         * @return
         */
        K specify(T entity);
    }

    public interface ValuePicker<R, K> {
        R pick(K entity);
    }

    public interface ListMerger<T> {
        T merge(T a, T b);
    }

    public interface StringSpecifier<T> {
        String specify(T entity);
    }

    public interface MapKeySpecifier<K, V> {
        K specify(V entity);
    }

    public interface ElementTransformer<O, R> {
        R transform(O element);
    }

    public interface MapKeyValueSpecifier<K, V, T> {
        K specifyKey(T entity);

        V specifyValue(T entity);
    }

    public interface IteratorCallback<T> {
        void call(T entity, Iterate iterate);
    }

    public static class Iterate {
        private final Iterator iterator;
        private final int index;

        public Iterate(Iterator iterator, int index) {
            this.iterator = iterator;
            this.index = index;
        }

        public boolean hasNext() {
            if (iterator == null) {
                return false;
            }
            return iterator.hasNext();
        }

        public boolean isFirst() {
            return index == 0;
        }
    }

}
