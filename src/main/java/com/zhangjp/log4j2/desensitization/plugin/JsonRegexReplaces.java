package com.zhangjp.log4j2.desensitization.plugin;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * 创建时间 2019年四月01日 星期一 9:07
 * 作者: zhangjp
 * 描述：自定义插件（基于正则表达式实现日志脱敏）
 */
@Plugin(name = "jsonReplaces", category = "Core", printObject = true)
public class JsonRegexReplaces {
    private static final Logger LOGGER = StatusLogger.getLogger();

    // replace标签，复用log4j已有plugin， replaces 下可以0，1，多个replace
    private final RegexReplacement[] replaces;

    // 自定义JSON格式 的replace 封装 json字符串的判断，以及脱敏规则
    private final JsonDesensitization[] jsonDesensitization;


    private JsonRegexReplaces(RegexReplacement[] replaces, JsonDesensitization[] jsonDesensitization) {
        this.replaces = replaces;
        this.jsonDesensitization = jsonDesensitization;
    }

    /**
     * 格式化输出日志信息， 此方法会执行多个正则表达式匹配与替换
     */
    public String format(String msg) {
        for (JsonDesensitization json : jsonDesensitization) {
            msg = json.format(msg);
        }
        for (RegexReplacement replace : replaces) {
            msg = replace.format(msg);
        }
        return msg;
    }

    /**
     * 实现pluginFactory， 用于生成pugin
     */
    @PluginFactory
    public static JsonRegexReplaces createRegexReplacement(
            @PluginElement("replace") final RegexReplacement[] replaces,
            @PluginElement("jsonReplace") final JsonDesensitization[] jsonReplace) {
        if (replaces == null) {
            LOGGER.warn("have the replace , but no replace is set");
            return null;
        }
        if (jsonReplace.length == 0) {
            LOGGER.warn("have the jsonReplace , but no jsonReplace is set");
            return null;
        }
        return new JsonRegexReplaces(replaces, jsonReplace);
    }
}
