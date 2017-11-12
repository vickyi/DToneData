package com.bi.utils;

/**
 * 常量
 *
 * @author bi
 * @email bi.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;
    
    public static String STRING_0 = "0";

    public static String STRING_1 = "1";

    public static String STRING_2 = "2";

    public static String STRING_3 = "3";

    public static Integer INT_0 = 0; //等待

    public static Integer INT_1 = 1; //运行中

    public static Integer INT_2 = 2; //成功

    public static Integer INT_3 = 3; //失败

    public static Integer INT_4 = 4; //取消

    public static final String COOKIE_DOMAIN = "bi.org";


    /**
     * 菜单类型
     *
     * @author bi
     * @email bi.com
     * @date 2016年11月15日 下午1:24:29
     */

    /**
     * 业务Code
     */

    public static String LABEL_CODE = "LB";

    public static String BOARD_CODE = "BO";

    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author bi
     * @email bi.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
}
