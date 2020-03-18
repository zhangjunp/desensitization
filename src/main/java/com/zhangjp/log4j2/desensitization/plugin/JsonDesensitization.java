package com.zhangjp.log4j2.desensitization.plugin;

import com.zhangjp.log4j2.desensitization.desensitization.DesensitizationStrategy;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 创建时间 2019年四月01日 星期一 9:39
 * 作者: zhangjp
 * 描述：json日志格式脱敏
 */
@Plugin(name = "jsonReplace", category = "Core", printObject = true)
public final class JsonDesensitization {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private String keys;
    private String methodName;

    private JsonDesensitization(final String keys, final String methodName) {
        this.keys = keys;
        this.methodName = methodName;
    }

    public String format(String msg) {
        String[] split = keys.split(",");
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < split.length; i++) {
            sb.append("\"").append(split[i]).append("\"").append("|");
            if (i == split.length - 1) {
                sb.append("\"").append(split[i]).append("\"");
            }
        }
        sb.append(")(:\")([^\"]*)(\")");
        Matcher matcher = Pattern.compile(sb.toString()).matcher(msg);
        if (matcher.find()) {
            String group = matcher.group(3);
            if (null != DesensitizationStrategy.DEVICE_METHOD_MAP.get(methodName)) {
                String cipherText = DesensitizationStrategy.DEVICE_METHOD_MAP.get(methodName).produceCipherText(group);
                msg = matcher.replaceAll("$1".concat("$2").concat(cipherText).concat("$4"));
            } else {
                LOGGER.warn("脱敏方法名:{}，不存在，请检查脱敏方法名是否正确！", methodName);
            }
        }
        return msg;
    }

    @PluginFactory
    public static JsonDesensitization createRegexReplacement(
            @PluginAttribute("keys") final String keys,
            @PluginAttribute("methodName") final String methodName) {
        if (keys == null) {
            LOGGER.error("The json keys  is required");
            return null;
        }
        if (methodName == null) {
            LOGGER.error("A value string is required");
        }
        return new JsonDesensitization(keys, methodName);
    }
}
