package com.bi.dao;

import com.bi.entity.TokenEntity;

/**
 * 用户Token
 * 
 * @author bi
 * @email bi.com
 * @date 2017-03-23 15:22:07
 */
public interface TokenDao extends BaseDao<TokenEntity> {
    
    TokenEntity queryByUserId(Long userId);

    TokenEntity queryByToken(String token);
	
}
