package com.dianping.actionlog.util;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jourrey on 16/12/13.
 */
public class ExtensionLoader {

    private static ConcurrentHashMap<Class<?>, List<?>> extensionListMap = new ConcurrentHashMap<Class<?>, List<?>>();

    private ExtensionLoader() {
    }

    /**
     * 这里是证明Empty和Null有区别的Case,所以请反思,代码里那些Empty和Null混用的行为
     * 不要拿业务特殊性狡辩,二意性和同意性是非常不好的
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(Class<T> clazz) {
        List<T> extensions = (List<T>) extensionListMap.get(clazz);
        if (extensions == null) {
            extensions = loadList(clazz);
            List<T> cache = (List<T>) extensionListMap.putIfAbsent(clazz, extensions);
            if (cache != null) {
                extensions = cache;
            }
        }
        return extensions;
    }

    public static <T> List<T> loadList(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        List<T> extensions = new LinkedList<T>();
        for (T service : serviceLoader) {
            extensions.add(service);
        }
        return extensions;
    }
}
