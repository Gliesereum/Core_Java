/*
 * Vendored from https://github.com/liqpay/sdk-java, Apache License 2.0.
 * See NOTICE.md in this package for why, and for the differences from upstream.
 */
package com.liqpay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class LiqPayUtil {

    public static byte[] sha1(String param) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            sha.update(param.getBytes(StandardCharsets.UTF_8));
            return sha.digest();
        } catch (Exception e) {
            throw new RuntimeException("Can't calc SHA-1 hash", e);
        }
    }

    public static String base64_encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Upstream encodes with the platform default charset here. LiqPay's API is
     * specified as UTF-8, and the service containers default to an ASCII
     * locale, so Cyrillic descriptions were being signed as '?'.
     */
    public static String base64_encode(String data) {
        return base64_encode(data.getBytes(StandardCharsets.UTF_8));
    }
}
