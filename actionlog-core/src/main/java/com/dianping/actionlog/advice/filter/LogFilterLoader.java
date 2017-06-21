package com.dianping.actionlog.advice.filter;

import com.dianping.actionlog.util.ExtensionLoader;

import java.util.List;

/**
 * Created by jourrey on 16/12/13.
 */
public class LogFilterLoader {

    private static List<LogFilter> filters = ExtensionLoader.getList(LogFilter.class);

    public static boolean registerFilter(LogFilter filter) {
        if (filters.contains(filter)) {
            return false;
        }
        return filters.add(filter);
    }

    public static boolean unregisterFilter(LogFilter filter) {
        return filters.remove(filter);
    }

    public static List<LogFilter> getFilters() {
        return filters;
    }

}
