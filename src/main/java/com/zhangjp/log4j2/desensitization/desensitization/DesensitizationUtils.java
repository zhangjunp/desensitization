package com.zhangjp.log4j2.desensitization.desensitization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * 创建时间 2019年四月02日 星期二 17:00
 * 作者: zhangjunping
 * 描述：脱敏工具类
 */
public class DesensitizationUtils extends StringUtils {

    /***
     * <p>Description: md5 方式</p>
     * @param plaintext 明文
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 17:10
     */
    public static String md5(String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes());
    }

    /***
     * <p>Description: 手机号码前三后四脱敏</p>
     * @param mobile 手机号
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 17:11
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /***
     * <p>Description: 身份证前三后四脱敏</p>
     *
     * @param id 身份证号
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 17:12
     */
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{4})\\w(?=\\w{4})", "*");
    }

    /***
     * <p>Description: 卡号三后四脱敏</p>
     * @param card 卡号
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 17:12
     */
    public static String cardEncrypt(String card) {
        if (StringUtils.isEmpty(card) || (card.length() < 8)) {
            return card;
        }
        return card.replaceAll("(?<=\\w{4})\\w(?=\\w{4})", "*");
    }

    /***
     * <p>Description: 姓名截取第一个字脱敏</p>
     * @param name    名字
     * @return java.lang.String
     * @author zhangjunping
     * @date 2019/4/2 17:12
     */
    public static String nameEncrypt(String name) {
        if (StringUtils.isEmpty(name) || (name.length() < 2)) {
            return name;
        }
        return "*" + name.substring(1);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email 邮箱
     * @return 脱敏后邮箱
     */
    public static String emailEncrypt(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 3)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 3), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    /**
     * 判断subStr是否在str中
     * 12,13,a 是否包含数字1 false
     * 1,12,13,a 是否包含数字1 true
     *
     * @param str       字符串
     * @param subStr    需要判断的字符串
     * @param splitChar 分隔符
     */
    public static boolean characterInString(String str, String subStr, char splitChar) {
        boolean result = false;
        int i = 0;
        while (i < str.length()) {
            // 分隔符直接跳过
            if (splitChar == str.charAt(i)) {
                i++;
                continue;
            }
            // 截取到下一分隔符直接的字符串
            int j = i + 1;
            StringBuilder sb = new StringBuilder().append(str.charAt(i));
            while (j < str.length() && splitChar != str.charAt(j)) {
                sb.append(str.charAt(j));
                j++;
            }

            // System.out.println(str);
            if (subStr.equals(sb.toString())) {
                result = true;
                break;
            }

            // 分隔符之后继续遍历
            i = j + 1;
        }
        return result;
    }

}
