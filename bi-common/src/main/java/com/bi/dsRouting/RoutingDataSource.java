package com.bi.dsRouting;

/**
 * Created by bi on 2017/10/31.
 */
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
public class RoutingDataSource extends AbstractRoutingDataSource  {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceType();
    }
}