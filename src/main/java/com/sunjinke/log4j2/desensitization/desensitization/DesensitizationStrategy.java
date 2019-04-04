package com.sunjinke.log4j2.desensitization.desensitization;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间 2019年四月04日 星期四 10:43
 * 作者: zhangjunping
 * 描述：脱敏器策略工厂
 */
public interface DesensitizationStrategy {
//    类似于 策略工厂的 context
    Map<String,DesensitizationStrategy> DEVICE_METHOD_MAP = new HashMap<>();

    /***
     * <p>Description: 脱敏规则</p>
     * @param plaintext 明文
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 20:43
     */
    String produceCipherText (String plaintext);

    void addDeviceToMap();
}
