package com.bi.dao;

import com.bi.dsRouting.DataSource;
import com.bi.dsRouting.RoutingStrategy;
import com.bi.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author bi
 * @email liuhang@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysUserDao extends BaseDao<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	//查询所有前端菜单 不需要权限的
	List<Long> queryAllFrontMenuId();
	
	/**
	 * 根据用户名，查询系统用户
	 */
	@DataSource(RoutingStrategy.STAR)
	SysUserEntity queryByUserName(String username);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);

}
