package com.zhangjp.log4j2.desensitization.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import org.springframework.stereotype.Component;

/**
 * 创建时间 2019年四月03日 星期三 9:07
 * 作者: zhangjp
 * 描述：TODO 基于FastJson的ValueFilter 实现 指定字段脱敏
 */
@Component
public class DesensitizationJsonFilter implements ValueFilter {
    @Override
    public Object process(Object obj, String name, Object value) {
        if (name.toLowerCase().equals("fastjson")) {
//            或者自定义规则
            return "******jsonFilter******";
        }
        if (name.toLowerCase().equals("tpasswd")) {
            return "******jsonFilter******";
        }
        return value;
    }
}
