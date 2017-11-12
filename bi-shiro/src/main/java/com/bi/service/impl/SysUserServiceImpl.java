package com.bi.service.impl;

import com.bi.dao.SysUserDao;
import com.bi.dsRouting.DataSource;
import com.bi.dsRouting.RoutingStrategy;
import com.bi.entity.SysUserEntity;
import com.bi.service.SysUserRoleService;
import com.bi.service.SysUserService;
import com.bi.utils.EncryptUtil;
import com.bi.utils.RRException;
import com.bi.service.SysRoleService;
import com.bi.utils.Constant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 系统用户
 * 
 * @author bi
 * @email liuhang@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

    @Override
    @DataSource(RoutingStrategy.STAR)
    public SysUserEntity queryObject(Long userId) {
        return sysUserDao.queryObject(userId);
    }

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public List<Long> queryAllFrontMenuId() {
		return sysUserDao.queryAllFrontMenuId();
	}


    @Override
    @DataSource(RoutingStrategy.STAR)
    public List<SysUserEntity> queryList(Map<String, Object> map){
        return sysUserDao.queryList(map);
    }


    @Override
    @DataSource(RoutingStrategy.STAR)
    public SysUserEntity queryByUserName(String username) {
        return sysUserDao.queryByUserName(username);
    }

	@DataSource(RoutingStrategy.STAR)
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		user.setModifyTime(new Date());

		user.setPassword(EncryptUtil.encryption(user.getPassword()));

		sysUserDao.save(user);
		//检查角色是否越权
		checkRole(user);

		if(user.getRoleIdList() != null && user.getRoleIdList().size() != 0 ) {
			//保存用户与角色关系
			sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		}
	}

	@Override
	public void updateLoginTime(SysUserEntity user){
		user.setModifyTime(new Date());
		sysUserDao.update(user);
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(EncryptUtil.encryption(user.getPassword()));
		}
		user.setModifyTime(new Date());
		sysUserDao.update(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		if(user.getRoleIdList() != null && user.getRoleIdList().size() != 0 ) {
			//保存用户与角色关系
			sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		}else{
			sysUserRoleService.delete(user.getUserId());
		}
		//sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}
	
	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());
		
		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}
}
