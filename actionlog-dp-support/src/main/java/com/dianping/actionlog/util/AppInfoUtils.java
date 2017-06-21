package com.dianping.actionlog.util;

import com.dianping.lion.Environment;

/**
 * Created by jourrey on 16/11/22.
 */
public class AppInfoUtils {

    /**
     * 获取当前项目名
     *
     * @return
     */
    public static String getAppName() {
        return Environment.getAppName();
    }

    /**
     * 判断当前环境是否是线上
     *
     * @return
     */
    public static boolean isOnLineEnv() {
        return Environment.isProductEnv();
    }
}
