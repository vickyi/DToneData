package com.bi.dsRouting;

/**
 * Created by bi on 2017/10/31.
 */
public class DataSourceHolder {
    private static final ThreadLocal<RoutingStrategy> contextHolder = new ThreadLocal<RoutingStrategy>();

    /**
     * @param strategy 数据库类型
     * @return void
     * @throws
     * @Description: 设置数据源类型
     */
    public static void setDataSourceType(RoutingStrategy strategy) {
        contextHolder.set(strategy);
    }

    /**
     * @param
     * @return String
     * @throws
     * @Description: 获取数据源类型
     */
    public static RoutingStrategy getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 清除数据源类型
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}