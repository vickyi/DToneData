package com.bi.service;

import com.bi.entity.OdDimTypesEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 状态汇总表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-28 14:27:02
 */
public interface OdDimTypesService {
	
	OdDimTypesEntity queryObject(Integer incId);
	
	List<OdDimTypesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(OdDimTypesEntity odDimTypes);
	
	void update(OdDimTypesEntity odDimTypes);
	
	void delete(Integer incId);
	
	void deleteBatch(Integer[] incIds);
	
	List<OdDimTypesEntity> queryListByTypeid(Integer typeId);
	
	HashMap<String, List<Map<String, String>>> formatKeyMap(List<OdDimTypesEntity> odDimList);

	/**
	 * 获取所有的枚举数据
	 * @return
	 */
    HashMap<String, List<Map<String, String>>> getListAll();
	
}
