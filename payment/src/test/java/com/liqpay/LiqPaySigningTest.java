package com.liqpay;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Pins the vendored SDK's wire format: LiqPay authenticates every request by
 * recomputing base64(sha1(private_key + data + private_key)) on their side, so
 * a drift in JSON key order, charset or Base64 alphabet silently turns into
 * rejected payments rather than a compile error.
 *
 * The expected values are computed independently (see NOTICE.md), not captured
 * from this implementation.
 */
public class LiqPaySigningTest {

    private static final String PUBLIC_KEY = "sandbox_pub";
    private static final String PRIVATE_KEY = "sandbox_private_key";

    private static final String EXPECTED_DATA =
            "eyJhbW91bnQiOiIxLjUiLCJjdXJyZW5jeSI6IlVBSCIsImRlc2NyaXB0aW9uIjoi0KLQtdGB0YIiLCJvcmRlcl9pZCI6IjQyIiwi"
                    + "cHVibGljX2tleSI6InNhbmRib3hfcHViIiwidmVyc2lvbiI6IjMifQ==";
    private static final String EXPECTED_SIGNATURE = "sbsmTOnm1iwDcSn4uJ+lEcGuIkk=";

    private Map<String, String> checkoutParams() {
        Map<String, String> params = new HashMap<>();
        params.put("amount", "1.5");
        params.put("currency", "UAH");
        params.put("description", "Тест");
        params.put("order_id", "42");
        return params;
    }

    /** Cyrillic must be signed as UTF-8, not as the platform default charset. */
    @Test
    public void serialisesCheckoutParamsToTheDocumentedDataPayload() throws Exception {
        Map<String, String> data = new LiqPay(PUBLIC_KEY, PRIVATE_KEY).generateData(checkoutParams());

        assertEquals(EXPECTED_DATA, data.get("data"));
        assertEquals(EXPECTED_SIGNATURE, data.get("signature"));
    }

    @Test
    public void signsWithSha1OverPrivateKeyWrappedData() {
        LiqPay liqPay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);

        assertEquals(EXPECTED_SIGNATURE, liqPay.createSignature(EXPECTED_DATA));
    }

    @Test
    public void base64EncodesStringsAsUtf8() {
        assertEquals("0KLQtdGB0YI=", LiqPayUtil.base64_encode("Тест"));
    }

    @Test
    public void rendersCheckoutFormCarryingDataAndSignature() {
        String form = new LiqPay(PUBLIC_KEY, PRIVATE_KEY).cnb_form(checkoutParams());

        assertTrue(form.contains("action=\"https://www.liqpay.ua/api/3/checkout\""));
        assertTrue(form.contains("name=\"data\" value=\"" + EXPECTED_DATA + "\""));
        assertTrue(form.contains("name=\"signature\" value=\"" + EXPECTED_SIGNATURE + "\""));
    }

    @Test(expected = NullPointerException.class)
    public void rejectsCheckoutWithoutAmount() {
        Map<String, String> params = checkoutParams();
        params.remove("amount");

        new LiqPay(PUBLIC_KEY, PRIVATE_KEY).cnb_form(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsEmptyPublicKey() {
        new LiqPay("", PRIVATE_KEY);
    }
}
