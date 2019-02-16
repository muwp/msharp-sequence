package com.ruijing.sequence.load;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * 机器运行的环境变量
 *
 * @author mwup
 * @version 1.0
 * @created 2018/08/23 13:51
 **/
public class Environment {

    /**
     * 环境配制文件
     */
    private static final String EVN_CONFIG = "/data/env/app.env";

    /**
     * 应用配制文件
     */
    private static final String APP_EVN_CONFIG = "META-INF/app.properties";

    /**
     * 环境变量值
     */
    private static Properties DEFAULT_PROPERTY = new Properties();

    static {
        loadAppEnv();
    }

    public static String getProperty(final String key) {
        return DEFAULT_PROPERTY.getProperty(key);
    }

    public static String getOrDefault(final String key, String defaultValue) {
        return DEFAULT_PROPERTY.getProperty(key, defaultValue);
    }


    private static Properties loadAppEnv() {
        String loadFile = null;
        Properties props = loadApp();
        if (MapUtils.isNotEmpty(props)) {
            DEFAULT_PROPERTY.putAll(props);
        }
        Properties tmp = props;
        try {
            loadFile = EVN_CONFIG;
            // load from /data/configs/env/appenv
            props = PropertiesUtils.loadFromFileSystem(loadFile);
            if (props == null) {
                props = PropertiesUtils.loadFromClassPath(loadFile);
            }
        } catch (Throwable e) {
            if (tmp == props) {
                props = null;
            }
            System.err.println("failed to load data from " + loadFile);
        }

        if (null != props) {
            DEFAULT_PROPERTY.putAll(props);
        }
        return props;
    }

    private static Properties loadApp() {
        String loadFile = null;
        Properties props = null;
        try {
            // load from META-INF/app.properties
            loadFile = APP_EVN_CONFIG;
            props = PropertiesUtils.loadFromClassPath(loadFile);
        } catch (Throwable e) {
            System.err.println("failed to load data from " + loadFile);
        }
        return props;
    }


}