package com.bi.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bi.dsRouting.DataSource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.bi.utils.Constant;
import com.bi.utils.EncryptUtil;
import com.bi.utils.SSLClient;
import com.bi.utils.SpringContextUtils;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/14
 * Created by xianggu.
 *
 * @author xianggu
 */
public class SsoFilter implements Filter {

    private final static Logger LOGGER = LoggerFactory.getLogger(SsoFilter.class);

    private static final String COOKIE_HASH_CODE_NAME = "manage_sdk_code";
    private static final String COOKIE_UID_NAME = "manage_bi_uid";
    private static final String MANAGE_HASHCODE_REDIS_KEY = "org.bi.manage.admin.hash:";

    /*
    * 是否是admin
    */
    private boolean isAdmin = false;

    private PatternMatcher pathMatcher = new AntPathMatcher();

    private Set<String> prefixIignores = new HashSet<String>();

    private RedisTemplate ssoRedisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoRedisTemplate = (RedisTemplate) SpringContextUtils.getBean("ssoRedisTemplate");
        String contextPath = filterConfig.getServletContext().getContextPath();
        String ignoresParam = filterConfig.getInitParameter("ignoreUrl");
        String[] ignoreArray = ignoresParam.split(",");
        for (String s : ignoreArray) {
            prefixIignores.add(contextPath + s);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();

        if (canIgnore(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        String hashCode = "";
        String uid = "";
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (COOKIE_HASH_CODE_NAME.equals(cookie.getName())) {
                    hashCode = cookie.getValue();
                }
                if (COOKIE_UID_NAME.equals(cookie.getName())) {
                    uid = cookie.getValue();
                }
            }
        }

        String contextPath = req.getContextPath();
        if (pathMatcher.matches(contextPath + "/", url) || pathMatcher.matches(contextPath + "/login.html", url)) {
            if (StringUtils.isNotEmpty(hashCode) && StringUtils.isNotEmpty(uid)) {
                String code = (String) ssoRedisTemplate.opsForValue().get(MANAGE_HASHCODE_REDIS_KEY + uid);
                LOGGER.info("cookie data exist, get code to sso redis,code:[{}]", code);

                String str = String.format("%s0%s%s", uid, code, "EJIDdhs&&&8790");
                String newHashCode = EncryptUtil.encryption(str);
                if (!hashCode.equals(newHashCode)) {
                    filterChain.doFilter(req, res);
                    return;
                }
                req.setAttribute("uid", uid);
                req.getRequestDispatcher("sso.jsp").forward(req, res);
                return;
            } else {
                filterChain.doFilter(req, res);
                return;
            }
        } else if (pathMatcher.matches("/logout", url)) {
            cleanCookie(req, res);
        } else if (pathMatcher.matches("/sys/login", url)) {
            String username = request.getParameter("username").trim();
            String password = request.getParameter("password").trim();
            password = URLDecoder.decode(password);
            if ("admin".equals(username)) {
                isAdmin = true;
                request.setAttribute("isUserExist", true);
                filterChain.doFilter(req, res);
                return;
            }
            isAdmin = false;
            String newUid = getUid(username, password, request);
            if (StringUtils.isEmpty(newUid)) {
                request.setAttribute("isUserExist", false);
                filterChain.doFilter(req, res);
                return;
            }
            request.setAttribute("isUserExist", true);
            String redisKey = MANAGE_HASHCODE_REDIS_KEY + newUid;
            String code = (String) ssoRedisTemplate.opsForValue().get(redisKey);
            LOGGER.info("check new uid code exist to redis, code:[{}]", code);
            if (StringUtils.isEmpty(code)) {
                int num = (int) (Math.random() * (9000)) + 1000;
                code = String.format("%d.%d", num, System.currentTimeMillis());
                ssoRedisTemplate.opsForValue().set(redisKey, code, 1L, TimeUnit.DAYS);
            }
            String str = String.format("%s0%s%s", newUid, code, "EJIDdhs&&&8790");
            String newHashCode = EncryptUtil.encryption(str);
            res.addCookie(getCookie(req.getCookies(), COOKIE_HASH_CODE_NAME, newHashCode));
            res.addCookie(getCookie(req.getCookies(), COOKIE_UID_NAME, newUid));
        } else {
            if (!isAdmin) {
                if (StringUtils.isEmpty(hashCode) || StringUtils.isEmpty(uid)) {
                    //如果cookie中不存在登录信息，则清空session,重新登录
                    req.getSession().invalidate();
                    res.sendRedirect("/login.html");
                    return;
                    //req.getRequestDispatcher("login.html").forward(req, res);
                }
            }
        }
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

    private boolean canIgnore(HttpServletRequest request) {
        String url = request.getRequestURI();
        for (String ignore : prefixIignores) {
            if (pathMatcher.matches(ignore, url)) {
                return true;
            }
        }
        return false;
    }

    private String getUid(String username, String password, ServletRequest req) {
        try {
            SSLClient httpClient = new SSLClient();
            URI uri = new URI(String.format("http://%s/publicApi/login", System.getProperty("ssoHost")));
            LOGGER.info("Get user info from OA accounts: userName is: [{}], OA api is: [{}]", username, uri);
            HttpPost httpPost = new HttpPost(uri);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new BasicNameValuePair("username", username));
            nameValuePairList.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            httpPost.setEntity(entity);
            String userAgent = System.getProperty("userAgent");
            if(!StringUtils.isEmpty(userAgent))
            {
                LOGGER.info("Get user info from OA accounts: userAgent is: [{}], OA api is: [{}]", userAgent, uri);
                httpPost.setHeader("User-Agent", userAgent);
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String stringResult = new String(EntityUtils.toString(resEntity, "utf-8"));
                Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(stringResult);
                String status = (String) resultMap.get("status");
                if ("1000".equals(status)) {
                    Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                    return (String) dataMap.get("uid");
                } else {
                    String msg = (String) resultMap.get("info");
                    req.setAttribute("msg", msg);
                    LOGGER.error("获取uid失败. ex: " + msg);
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取uid异常", e);
        }
        return "";
    }

    private Cookie getCookie(Cookie[] cookies, String key, String value) {
        if (null == cookies) {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            cookie.setDomain(Constant.COOKIE_DOMAIN);
            return cookie;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookie.setValue(value);
                //cookie.setMaxAge(-1);
                cookie.setPath("/");
                cookie.setDomain(Constant.COOKIE_DOMAIN);
                return cookie;
            }
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        cookie.setDomain(Constant.COOKIE_DOMAIN);
        return cookie;
    }

    private void cleanCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = new Cookie(cookies[i].getName(), null);
            cookie.setMaxAge(0);
            cookie.setDomain(Constant.COOKIE_DOMAIN);
            cookie.setPath("/");//根据你创建cookie的路径进行填写    
            response.addCookie(cookie);
        }
    }

    public RedisTemplate getSsoRedisTemplate() {
        return ssoRedisTemplate;
    }

    public void setSsoRedisTemplate(RedisTemplate ssoRedisTemplate) {
        this.ssoRedisTemplate = ssoRedisTemplate;
    }
}
