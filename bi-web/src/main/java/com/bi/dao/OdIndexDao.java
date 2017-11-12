package com.bi.dao;

import com.bi.entity.OdIndexEntity;

import java.util.List;
import java.util.Map;

/**
 * 指标表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 14:26:52
 */
public interface OdIndexDao extends BaseDao<OdIndexEntity> {

    List<OdIndexEntity> queryListGroupByIndexName(Map<String, Object> map);

    int queryTotalGroupByIndexName(Map<String, Object> map);

    //查询index_name 是否重复
    int queryTotalByIndexName(Map<String, Object> map);

}
