package com.gliesereum.payment.service.liqpay.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.liqpay.LiqPayCheckoutService;
import com.gliesereum.payment.service.liqpay.LiqPayTransactionService;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayActionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayPayType;
import com.gliesereum.share.common.model.dto.payment.liqpay.CheckoutRequestDto;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayResponseDto;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayTransactionDto;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

@Service
public class LiqPayCheckoutServiceImpl implements LiqPayCheckoutService {

    @Value("${liq-pay.public-key}")
    private String publicKey;

    @Value("${liq-pay.private-key}")
    private String privateKey;

    @Value("${liq-pay.checkout.callback-server-url}")
    private String serverUrl;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private LiqPayTransactionService transactionService;

    @Override
    public String createCheckoutButton(CheckoutRequestDto request) {
        String result = "";
        if (request != null) {
            HashMap<String, String> params = new HashMap<>();
            LiqPayActionType actionType = request.getActionType();
            if (actionType == null) {
                actionType = LiqPayActionType.pay;
            }
            if (StringUtils.isNotEmpty(request.getResultUrl())) {
                params.put("result_url", request.getResultUrl());
            }
            LiqPayPayType payPayType = request.getPayType();
            if (request.getPayType() == null) {
                payPayType = LiqPayPayType.card;
            }
            params.put("paytypes", payPayType.toString());
            params.put("description", request.getDescription());
            params.put("server_url", serverUrl);
            params.put("action", actionType.toString());
            params.put("amount", request.getAmount().toString());
            params.put("order_id", request.getOrderId().toString());
            params.put("currency", "UAH");
            params.put("language", "uk");
            params.put("version", "3");
            LiqPay liqpay = new LiqPay(publicKey, privateKey);
            result = liqpay.cnb_form(params);
        }
        return result;
    }

    @Override
    public void callBack(LiqPayResponseDto response) {
        if (response != null && ObjectUtils.allNotNull(response.getData(), response.getSignature())) {
            String decodedData = decodeBase64(response.getData());
            if (StringUtils.isNotEmpty(decodedData)) {
                if (checkSignature(response.getSignature(), response.getData())) {
                    LiqPayTransactionDto dto = jsonToTransaction(decodedData);
                    if (dto != null && dto.getOrder_id() != null) {
                        LiqPayTransactionDto exist = transactionService.getByOrderId(dto.getOrder_id());
                        if (exist != null) {
                            dto.setId(exist.getId());
                            transactionService.update(dto);
                        }
                        transactionService.create(dto);
                    }
                }
            }
        }
    }

    @Override
    public String createCheckoutQrCode(CheckoutRequestDto request) {
        String result = "";
        if (request != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("version", "3");
            params.put("currency", "UAH");
            params.put("language", "uk");
            params.put("server_url", serverUrl);
            params.put("action", LiqPayActionType.payqr.toString());
            params.put("amount", request.getAmount().toString());
            params.put("description", request.getDescription());
            params.put("order_id", request.getOrderId().toString());
            LiqPay liqpay = new LiqPay(publicKey, privateKey);
            try {
                HashMap<String, Object> res = (HashMap<String, Object>) liqpay.api("request", params);
                if(MapUtils.isNotEmpty(res)){
                    result = (String) res.get("qr_code");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private LiqPayTransactionDto jsonToTransaction(String json) {
        LiqPayTransactionDto result = null;
        try {
            result = mapper.readValue(json, LiqPayTransactionDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean checkSignature(String signature, String data) {
        String sign = LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey.concat(data).concat(privateKey)));
        return sign.equals(signature);
    }

    private String decodeBase64(String encode) {
        byte[] decodedBytes = Base64.getDecoder().decode(encode);
        return new String(decodedBytes);
    }
}
