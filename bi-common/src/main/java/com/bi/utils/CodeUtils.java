package com.bi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 业务数据代码生成
 *
 * @author yunmo
 * @email yunmo@bi.com
 * @date 2017年7月11日 下午12:53:33
 */
public class CodeUtils {
    public final static String DATE_TIME_PATTERN = "yyMMddHHmmss";

    public static String getCode(String code) {
        if(code != null && !("").equals(code)){
            StringBuffer sb = new StringBuffer();
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            sb.append(code).append(df.format(new Date()));
            return sb.toString();
        }else{
            throw new RRException("code不能为空");
        }
    }
}
