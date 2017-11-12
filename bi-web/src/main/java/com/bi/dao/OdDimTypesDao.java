package com.bi.dao;

import java.util.List;

import com.bi.entity.OdDimTypesEntity;

/**
 * 状态汇总表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-28 14:27:02
 */
public interface OdDimTypesDao extends BaseDao<OdDimTypesEntity> {
	
    List<OdDimTypesEntity> queryListByTypeId(Integer typeId);
    
}
