package com.sunjinke.log4j2.desensitization.desensitization;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;

/**
 * 创建时间 2019年四月04日 星期四 10:45
 * 作者: zhangjunping
 * 描述：MD5方式脱敏器
 */
@Component
public class MD5Device implements DesensitizationStrategy {
    @Override
    public String produceCipherText(String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes());
    }

    @PostConstruct
    @Override
    public void addDeviceToMap() {
        DEVICE_METHOD_MAP.put("md5",this);
    }
}
