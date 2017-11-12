package com.bi.shiro;

import com.alibaba.fastjson.JSONObject;
import com.bi.entity.SysMenuEntity;
import com.bi.entity.SysUserEntity;
import com.bi.service.impl.SysMenuServiceImpl;
import com.bi.service.impl.SysUserServiceImpl;
import com.bi.utils.EncryptUtil;
import com.bi.utils.SSLClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 认证
 *
 * @author bi
 * @email liuhang@gmail.com
 * @date 2016年11月10日 上午11:55:49
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysMenuServiceImpl sysMenuService;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        List<String> permsList = null;

        //系统管理员，拥有最高权限
        if (userId == 1) {
//            List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<String, Object>());
            List<SysMenuEntity> menuList = sysMenuService.queryList(new HashMap<String, Object>());
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
//            permsList = sysUserDao.queryAllPerms(userId);
            permsList = sysUserService.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<String>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        SysUserEntity user = new SysUserEntity();
        if (!username.equals("admin")) {
            String jsonString = getOaApi(username, password);
            if (StringUtils.isEmpty(jsonString)) {
                throw new UnknownAccountException("登录异常，请联系管理员");
            }
            Integer code = (Integer) JSONObject.parseObject(jsonString).get("code");
            String msg = (String) JSONObject.parseObject(jsonString).get("msg");

            if (code == -1) {
                throw new UnknownAccountException(msg + code);
            } else if (code == -2) {
                throw new UnknownAccountException(msg + code);
            } else if (code == -4) {
                throw new UnknownAccountException(msg + code);
            }
            Map userMap = (Map) JSONObject.parseObject(jsonString).get("data");

            //查询用户信息
            user = sysUserService.queryByUserName(username);
//            user = sysUserDao.queryByUserName(username);
            if (user == null) {
                SysUserEntity newuser = new SysUserEntity();
                newuser.setUsername((String) userMap.get("cn"));
                newuser.setTruename((String) userMap.get("sn"));
                newuser.setPassword(password);
                newuser.setMobile((String) userMap.get("mobile"));
                newuser.setEmail((String) userMap.get("mail"));
                newuser.setStatus(1);
                newuser.setCreateUserId((long) 1);
                if (userMap.containsKey("displayname")) {
                    newuser.setDisplayname((String) userMap.get("displayname"));
                }
                newuser.setOaId(Integer.parseInt((String) userMap.get("uid")));
                sysUserService.save(newuser);
//                user = sysUserDao.queryByUserName(username);
                user = sysUserService.queryByUserName(username);
            }else{
                SysUserEntity newuser = new SysUserEntity();
                newuser.setUserId(user.getUserId());
                // TODO 用户密码 fix
                sysUserService.updateLoginTime(newuser);
            }
        } else {
//            user = sysUserDao.queryByUserName(username);
            user = sysUserService.queryByUserName(username);
            //账号不存在
            if (user == null) {
                throw new UnknownAccountException("账号或密码不正确");
            }

            //密码错误
            if (!password.equals(user.getPassword())) {
                throw new IncorrectCredentialsException("账号或密码不正确");
            }

            //账号锁定
            if (user.getStatus() == 0) {
                throw new LockedAccountException("账号已被锁定,请联系管理员");
            }
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

    public Date getCurrentDatetime() {
        Date date = null;
        Date now = new Date();
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = myFmt2.parse(myFmt2.format(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 调用OA接口 返回json字符串
     *
     * @param username
     * @param password
     * @return
     */
    public String getOaApi(String username, String password) {
        String url1 = System.getProperty("oaHost") + "index.php?m=Api&c=Auth&a=login";
        System.out.println(url1);
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("username", username);
        userMap.put("password", password);
        String httpOrgCreateTestRtn = doPost(url1, userMap, "utf-8");
        String unicodeChinese = EncryptUtil.decodeUnicode(httpOrgCreateTestRtn);
        return unicodeChinese;
    }

    public static String doPost(String url, Map<String, String> map, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if (map != null) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                    httpPost.setEntity(entity);
                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String stringResult = new String(EntityUtils.toString(resEntity, charset));
                    result = stringResult;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
