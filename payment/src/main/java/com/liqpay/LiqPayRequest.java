/*
 * Vendored from https://github.com/liqpay/sdk-java, Apache License 2.0.
 * See NOTICE.md in this package for why, and for the differences from upstream.
 */
package com.liqpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.liqpay.LiqPayUtil.base64_encode;

public class LiqPayRequest {

    /** Upstream sets no timeouts at all, so a stalled endpoint pins the thread. */
    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 30_000;

    public static String post(String url, Map<String, String> list, String proxyLogin, String proxyPassword, Proxy proxy) throws Exception {
        StringBuilder urlParameters = new StringBuilder();
        for (Map.Entry<String, String> entry : list.entrySet()) {
            urlParameters.append(entry.getKey())
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                    .append('&');
        }

        URL obj = new URL(url);
        HttpURLConnection con;
        if (proxy == null) {
            con = (HttpURLConnection) obj.openConnection();
        } else {
            con = (HttpURLConnection) obj.openConnection(proxy);
            if (proxyLogin != null) {
                con.setRequestProperty("Proxy-Authorization", "Basic " + getProxyUser(proxyLogin, proxyPassword));
            }
        }
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setConnectTimeout(CONNECT_TIMEOUT_MS);
        con.setReadTimeout(READ_TIMEOUT_MS);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParameters.toString());
            wr.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    public static String getProxyUser(String proxyLogin, String proxyPassword) {
        return base64_encode(proxyLogin + ":" + proxyPassword);
    }
}
