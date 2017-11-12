package com.bi.dao;

import com.bi.entity.SysMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 * 
 * @author bi
 * @email bi.com
 * @date 2016年9月18日 上午9:33:01
 */
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);

	List<Map<String,Object>> queryListBoard();

}
