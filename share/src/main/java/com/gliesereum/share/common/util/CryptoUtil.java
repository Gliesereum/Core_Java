package com.gliesereum.share.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

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
}
