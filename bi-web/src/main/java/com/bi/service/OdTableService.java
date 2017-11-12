package com.bi.service;

import com.bi.entity.OdTableEntity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * table表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public interface OdTableService {
	
    /**
     * 通过table_id 获取数据
     * @param id
     * @return
     */
	OdTableEntity queryObject(Integer id);
	
	/**
	 * 通过 id 获取数据
	 * @param id
	 * @return
	 */
	OdTableEntity queryObjectById(Integer id);
	
	List<OdTableEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(OdTableEntity odTable);
	
	void update(OdTableEntity odTable);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	/**
	 * 检测，设置表的版本数据
	 * 
	 * @param odTableEntity
	 * @param isversion
	 * @return
	 */
    OdTableEntity checkVersion(OdTableEntity odTableEntity, Integer isversion);

    /**
     * 按条件查询获取所有的历史版本记录
     * 
     * @param params
     * @return
     */
    List<OdTableEntity> queryVersionList(Map<String, Object> params);

    /**
     * 表管理列表导出
     * 
     * @param odIndexList
     * @param response
     */
    void exportOdtable(List<OdTableEntity> odTableList, HttpServletResponse response);


}
