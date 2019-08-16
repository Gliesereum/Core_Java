package com.gliesereum.share.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author vitalij
 * @version 1.0
 */
public class CryptoUtil {

    public static String encryptAes256(String text, String password, String salt) {
        String result = null;
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(salt)) {
            TextEncryptor te = Encryptors.text(password, salt);
            result = te.encrypt(text);
        }
        return result;
    }

    public static String decryptAes256(String text, String password, String salt) {
        String result = null;
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(salt)) {
            TextEncryptor te = Encryptors.text(password, salt);
            result = te.decrypt(text);
        }
        return result;
    }

    public static String encryptStringToHmacMD5(String text, String key) {
        String result = null;
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(key)) {
            try {
                SecretKeySpec secretKey = new SecretKeySpec((key).getBytes(StandardCharsets.UTF_8), "HmacMD5");
                Mac mac = Mac.getInstance("HmacMD5");
                mac.init(secretKey);
                byte[] bytes = mac.doFinal(text.getBytes(StandardCharsets.US_ASCII));
                StringBuilder hash = new StringBuilder();
                for (byte aByte : bytes) {
                    String hex = Integer.toHexString(0xFF & aByte);
                    if (hex.length() == 1) {
                        hash.append('0');
                    }
                    hash.append(hex);
                }
                result = hash.toString();
            } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                e.getStackTrace();
            }
        }
        return result;
    }
}
