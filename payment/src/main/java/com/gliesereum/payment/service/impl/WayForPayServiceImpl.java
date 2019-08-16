package com.gliesereum.payment.service.impl;

import com.gliesereum.payment.service.WayForPayService;
import com.gliesereum.share.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WayForPayServiceImpl implements WayForPayService {

    @Value("${way-for-pay.merchant.key}")
    private String couplerKey;

    @Value("${way-for-pay.merchant.account}")
    private String couplerAccount;

    @Value("${way-for-pay.merchant.domain}")
    private String couplerDomainName;

    @Value("${way-for-pay.url.verify}")
    private String verifyUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void verifyCard() {
        int amount = 1;
        String currency = "UAH";
        UUID orderReference = UUID.randomUUID();
        List<String> md5Params = List.of(couplerAccount, couplerDomainName, orderReference.toString(), String.valueOf(amount), currency);
        String signature = generateHmacMD5(md5Params, couplerKey);
        if (StringUtils.isNotBlank(signature)) {
            Map<String, Object> params = new HashMap<>();
            params.put("transactionType", "VERIFY");
            params.put("merchantAccount", couplerAccount);
            params.put("merchantDomainName", couplerDomainName);
            params.put("merchantSignature", signature);
            params.put("apiVersion", 1);
            params.put("orderReference", orderReference);
            params.put("amount", amount);
            params.put("currency", currency);
            params.put("returnUrl", "https://82ddbd7b.ngrok.io/way-for-pay/verify-card-response");
            params.put("serviceUrl", "https://82ddbd7b.ngrok.io/way-for-pay/verify-card-response");
            /*params.put("card", "4731185600315136");
            params.put("expMonth", "11");
            params.put("expYear", "2023");
            params.put("cardCvv", "841");
            params.put("cardHolder", "VITALII GORBUNOV");*/


            ResponseEntity<String> response = restTemplate.exchange(
                    verifyUrl,
                    //"https://api.wayforpay.com/api",
                    HttpMethod.POST,
                    new HttpEntity<>(params),
                    new ParameterizedTypeReference<String>() {
                    });
            if (response != null) {
                response.getBody();
            }
        }
    }

    @Override
    public void verifyCardResponse(Map<String, String> response) {
        if (response != null) {
            response.toString();
        }
    }


    private String generateHmacMD5(List<String> params, String key) {
        String stringParams = String.join(";", params);
        String result = CryptoUtil.encryptStringToHmacMD5(stringParams, key);
        return result;
    }
}
