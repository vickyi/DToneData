package com.bi.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.bi.entity.SysUserEntity;
import com.bi.service.SysUserService;
import com.bi.utils.Constant;
import com.bi.utils.EncryptUtil;
import com.bi.utils.R;
import com.bi.utils.SSLClient;
import com.bi.utils.ShiroUtils;

/**
 * 登录相关
 *
 * @author bi
 * @email bi.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SysLoginController.class);

    @Autowired
    private Producer producer;

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha, HttpServletRequest request) throws IOException {
        try {
            boolean validateUser = (boolean) request.getAttribute("isUserExist");
            if (!validateUser) {
                return R.error((String) request.getAttribute("msg"));
            }
            Subject subject = ShiroUtils.getSubject();
            String pwd = URLDecoder.decode(password);
            String newPassword = EncryptUtil.encryption(pwd);
            username = username.trim();
            UsernamePasswordToken token = new UsernamePasswordToken(username, newPassword);
            System.out.println(token);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账户验证失败6");
        }

		/*String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}*/
        Map<String, Object> map = new HashedMap();
       /* if (("admin").equals(username)) {
            map.put("url", "admin.html");
        } else {
            map.put("url", "index.html");
        }*/
        map.put("url", "index.html");
        return R.ok(map);
    }

    /**
     * SSO登录
     */
    @ResponseBody
    @RequestMapping(value = "/sys/ssoLogin")
    public R login(String uid, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            SSLClient httpClient = new SSLClient();
            URI uri = new URI(String.format("http://%s/PublicApi/getAccessByUid", System.getProperty("ssoHost")));
            LOGGER.info("Get user info from OA accounts: uid is: [{}], OA api is: [{}]", uid, uri);
            StringEntity entity = new StringEntity("uid=" + uid);
            entity.setContentType("application/x-www-form-urlencoded");
            HttpPost httpPost = new HttpPost(uri);

            httpPost.setEntity(entity);
            String userAgent = System.getProperty("userAgent");
            if(!StringUtils.isEmpty(userAgent))
            {
                LOGGER.info("Get user info from OA accounts: userAgent is: [{}], OA api is: [{}]", userAgent, uri);
                httpPost.setHeader("User-Agent", userAgent);
            }

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String stringResult = new String(EntityUtils.toString(resEntity, "utf-8"));
                    Map<String, Object> o = (Map<String, Object>) JSON.parse(stringResult);
                    String code = (String) o.get("status");
                    if ("1000".equals(code)) {
                        Map<String, String> data = (Map<String, String>) o.get("data");
                        String username = data.get("username");
                        try {
                            SysUserEntity sysUserEntity = sysUserService.queryByUserName(username);
                            if (null == sysUserEntity) {
                                cleanCookies(req, res);
                                return R.ok();
                            }
                            Subject subject = ShiroUtils.getSubject();
                            UsernamePasswordToken token = new UsernamePasswordToken(username, sysUserEntity.getPassword());
                            subject.login(token);
                        } catch (UnknownAccountException e) {
                            LOGGER.error("sso login error.", e);
                            ShiroUtils.logout();
                            cleanCookies(req, res);
                            return R.error();
                        }
                    } else {
                        String msg = (String) o.get("info");
                        LOGGER.error("sso login error. code:{}, msg:[{}]", code, msg);
                        ShiroUtils.logout();
                        cleanCookies(req, res);
                        return R.error();
                    }
                }
            } else {
                LOGGER.error("sso login error. response is null");
                ShiroUtils.logout();
                cleanCookies(req, res);
                return R.error();
            }
        } catch (Exception e) {
            cleanCookies(req, res);
            LOGGER.error("sso login error.", e);
        }

        return R.ok();

    }

    private void cleanCookies(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = new Cookie(cookies[i].getName(), null);
            cookie.setMaxAge(0);
            cookie.setDomain(Constant.COOKIE_DOMAIN);
            cookie.setPath("/");//根据你创建cookie的路径进行填写    
            res.addCookie(cookie);
        }
        ShiroUtils.logout();
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:login.html";
    }
}
