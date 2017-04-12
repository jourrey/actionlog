package com.dianping.actionlog.util;

import com.dianping.lion.client.ConfigManager;
import com.dianping.lion.client.ConfigManagerFactory;

/**
 * Lion工具类，获取Lion的ConfigManager对象
 *
 * @author jourrey.liu
 */
public final class LionUtils {
    private static volatile ConfigManager instance = null;

    private LionUtils() {
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (LionUtils.class) {
                if (instance == null) {
                    instance = ConfigManagerFactory.getConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * 如果该对象被用于序列化，可以保证对象在序列化前后保持一致
     *
     * @returnType: Object
     */
    public Object readResolve() {
        return getInstance();
    }

}