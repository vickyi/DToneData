package com.bi.service;

import com.bi.entity.OdIndexEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 指标表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 14:26:52
 */
public interface OdIndexService {
	
	OdIndexEntity queryObject(Integer id);
	
	List<OdIndexEntity> queryList(Map<String, Object> map);

	List<OdIndexEntity> queryListGroupByIndexName(Map<String, Object> map);

	int queryTotalGroupByIndexName(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	int queryTotalByIndexName(Map<String, Object> map);
	
	void save(OdIndexEntity odIndex);
	
	void update(OdIndexEntity odIndex);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    void exportOneDataIndex(List<OdIndexEntity> odIndexList, HttpServletResponse response);
}
