package com.bi.dao;

import java.util.List;
import java.util.Map;

import com.bi.entity.OdTableEntity;

/**
 * table表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public interface OdTableDao extends BaseDao<OdTableEntity> {

    OdTableEntity queryObjectById(Integer id);
    
    List<OdTableEntity> queryVersionList(Map<String, Object> map);
    
}
