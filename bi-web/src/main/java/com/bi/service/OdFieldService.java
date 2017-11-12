package com.bi.service;

import com.bi.entity.OdFieldEntity;

import java.util.List;
import java.util.Map;

/**
 * 表对应的字段表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public interface OdFieldService {
	
	OdFieldEntity queryObject(Integer id);
	
	List<OdFieldEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(OdFieldEntity odField);
	
	void update(OdFieldEntity odField);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
