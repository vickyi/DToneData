package com.bi.dao;

import com.bi.entity.UserEntity;

/**
 * 用户
 * 
 * @author bi
 * @email bi.com
 * @date 2017-03-23 15:22:06
 */
public interface UserDao extends BaseDao<UserEntity> {

    UserEntity queryByMobile(String mobile);
}
