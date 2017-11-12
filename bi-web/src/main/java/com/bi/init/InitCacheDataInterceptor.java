package com.bi.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Project: oneData
 * Description: 加载事件、属性等信息到redis
 * Created at: 2017/7/10
 * Created by xianggu.
 *
 * @author xianggu
 */
@Component
public class InitCacheDataInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
