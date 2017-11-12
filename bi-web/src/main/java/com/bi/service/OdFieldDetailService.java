package com.bi.service;

import com.bi.entity.OdFieldDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 表字段的详情（可更改）
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public interface OdFieldDetailService {
	
	OdFieldDetailEntity queryObject(Integer id);
	
	List<OdFieldDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(OdFieldDetailEntity odFieldDetail);
	
	void update(OdFieldDetailEntity odFieldDetail);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
