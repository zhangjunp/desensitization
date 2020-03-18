package com.zhangjp.log4j2.desensitization.encry;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * 创建时间 2019年七月18日 星期四 10:05
 * 描述：我家阳光818活动对接，Aes加密工具类
 */
public class AESUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AESUtil.class);
    private static final String aesKey = "78dsfs54fs42afnlsale";
    private static final String defaultCharset = "UTF-8";



    /**
     * 使用aes加密
     * 功能和mysql的hex(AES_ENCRYPT(content,'key'))一样，使用utf-8编码
     * @param content
     * @param key
     * @return
     */
    public static String AESEncrypt(String content, String key) {
        try {
            LOG.debug("加密前 <{}> ", content);
            if (StringUtils.isNotBlank(content)) {
                final Cipher encryptCipher = Cipher.getInstance("AES");
                SecretKeySpec secretKeySpec = generateMySQLAESKey(key, defaultCharset);
                encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte valBytes[] = encryptCipher.doFinal(content.getBytes(defaultCharset));
                String r = new String(Hex.encodeHex(valBytes));
                LOG.debug("加密后 <{}> ", r);
                return r;
            }
        } catch (Exception e) {
           LOG.error("AESEncrypt加密失败：{}",e);
        }
        return content;
    }

    /**
     * 使用aes解密，
     * 功能和mysql的AES_DECRYPT(unhex(content),'key')一样，使用utf-8编码
     * @param content
     * @param key
     * @return
     */
    public static String AESDecrypt(String content, String key) {
        if (StringUtils.isNotBlank(content)) {
            if (StringUtils.isNumeric(content.substring(0, content.length() - 1))) {
                return content;
            } else {
                try {
                    LOG.debug("解密前 <{}> ", content);
                    Cipher decryptCipher = Cipher.getInstance("AES");
                    SecretKeySpec secretKeySpec = generateMySQLAESKey(key, "UTF-8");
                    decryptCipher.init(2, secretKeySpec);
                    byte[] valBytes = decryptCipher.doFinal(Hex.decodeHex(content.toCharArray()));
                    String r = new String(valBytes);
                    LOG.debug("解密后 <{}> ", r);
                    return r;
                } catch (Exception e) {
                    LOG.debug("解密失败后 <{}> ", content);
                    return content;
                }
            }
        } else {
            return content;
        }
    }

    /**
     * 使用aes加密，使用默认key
     * 功能和mysql的hex(AES_ENCRYPT(content,'key'))一样，使用utf-8编码
     * @param content
     * @return
     */
    public static String AESEncrypt(String content) {
        return AESEncrypt(content, aesKey);
    }

    /**
     * 使用aes解密，使用默认key。
     * 功能和mysql的AES_DECRYPT(unhex(content),'key')一样，使用utf-8编码
     * @param content
     * @return
     * @throws Exception
     */
    public static String AESDecrypt(String content) {
        return AESDecrypt(content, aesKey);
    }

    /**
     * @param key
     * @param encoding
     * @return
     */
    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for (byte b : key.getBytes(encoding)) {
                finalKey[i++ % 16] ^= b;
            }
            return new SecretKeySpec(finalKey, "AES");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        String name = "";
        String phone = "13513210010";
        String certificate = "";
        String aesname = AESEncrypt(name);
        String aesphone = AESEncrypt(phone);
        String aescertificate = AESEncrypt(certificate);
        System.out.println("-----------------AES解密------------------------");
        AESDecrypt(name);
    }
}
