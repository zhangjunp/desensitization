package com.zhangjp.log4j2.desensitization.encry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 创建时间 2019年九月12日 星期四 15:44
 * 作者: zhangjp
 */
public class JacksonEncry extends JsonSerializer<String>  {
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        System.out.println("value = " + value);
        if (!StringUtils.isEmpty (value)) {
            String s = AESUtil.AESEncrypt(value);
            jsonGenerator.writeString (s);
        }else {
            jsonGenerator.writeString (value);
        }
    }

}
