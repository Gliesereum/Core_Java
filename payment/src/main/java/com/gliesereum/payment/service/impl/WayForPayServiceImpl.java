package com.gliesereum.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.PaymentCardService;
import com.gliesereum.payment.service.WayForPayService;
import com.gliesereum.share.common.model.dto.payment.PaymentCardDto;
import com.gliesereum.share.common.model.dto.payment.WayForPayResponseDto;
import com.gliesereum.share.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Value("${way-for-pay.url.verify-card-response}")
    private String verifyCartResponse;

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentCardService cardService;

    @Override
    public String getFormVerifyCard() {
        //SecurityUtil.checkUserByBanStatus(); //todo ++++++++++++++++++
        String result = null;
        int amount = 1;
        String currency = "UAH";
        //PaymentCardDto newCard = cardService.create(new PaymentCardDto(SecurityUtil.getUserId())); //todo ++++++++++++
        PaymentCardDto newCard = cardService.create(new PaymentCardDto(UUID.randomUUID(), false));
        if (newCard != null) {
            UUID orderReference = newCard.getId();
            List<String> md5Params = List.of(couplerAccount, couplerDomainName, orderReference.toString(), String.valueOf(amount), currency);
            String signature = generateHmacMD5(md5Params, couplerKey);
            if (StringUtils.isNotBlank(signature)) {
                Map<String, Object> params = new HashMap<>();
                params.put("apiVersion", 1);
                params.put("amount", amount);
                params.put("currency", currency);
                params.put("transactionType", "VERIFY");
                params.put("merchantSignature", signature);
                params.put("orderReference", orderReference);
                params.put("serviceUrl", verifyCartResponse);
                params.put("merchantAccount", couplerAccount);
                params.put("merchantDomainName", couplerDomainName);
                result = renderHtmlForm(verifyUrl, params);
            }
        }
        return result;
    }

    @Override
    public void verifyCardResponse(Map<String, String> map) {
        if (MapUtils.isNotEmpty(map)) {
            String json = (String) map.keySet().toArray()[0];
            WayForPayResponseDto response = jsonToWayForPayResponse(json);
            if (response != null && checkSignatureVerifyCard(response)) {
                UUID idOrder = UUID.fromString(response.getOrderReference());
                PaymentCardDto saveCard = cardService.getById(idOrder);
                if (saveCard != null) {
                    PaymentCardDto card = modelMapper.map(response, PaymentCardDto.class);
                    card.setId(saveCard.getId());
                    card.setFavorite(saveCard.isFavorite());
                    card.setOwnerId(saveCard.getOwnerId());
                    if (response.getReasonCode() == 1100) {
                        card.setVerify(true);
                    }
                    cardService.update(card);
                }
                sendStatusVerify(response.getOrderReference());
            }
        }
    }

    private boolean checkSignatureVerifyCard(WayForPayResponseDto response) {
        boolean result = false;
        if (ObjectUtils.allNotNull(
                response.getOrderReference(),
                response.getAmount(),
                response.getCurrency(),
                response.getMerchantSignature())) {
            List<String> md5Params = List.of(couplerAccount, response.getOrderReference(), response.getAmount().toString(), response.getCurrency());
            String signature = generateHmacMD5(md5Params, couplerKey);
            result = signature.equals(response.getMerchantSignature());
        }
        //return result; //todo
        return true;
    }

    private void sendStatusVerify(String orderReference) {
        String status = "accept";
        long time = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
        List<String> md5Params = List.of(orderReference, status, String.valueOf(time));
        String signature = generateHmacMD5(md5Params, couplerKey);
        Map<String, Object> params = new HashMap<>();
        params.put("orderReference", orderReference);
        params.put("status", status);
        params.put("time", time);
        params.put("signature", signature);
        ResponseEntity<String> response = restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                new HttpEntity<>(params),
                new ParameterizedTypeReference<String>() {
                });
        response.getBody(); //todo ++++++++++++
    }


    private String generateHmacMD5(List<String> params, String key) {
        return CryptoUtil.encryptStringToHmacMD5(String.join(";", params), key);
    }

    private String renderHtmlForm(String url, Map<String, Object> data) {
        StringBuilder form = new StringBuilder("<!DOCTYPE html>\n<html>\n<body>\n");
        form.append("<form method=\"POST\" action=\"".concat(url).concat("\" accept-charset=\"utf-8\">\n"));
        if (MapUtils.isNotEmpty(data)) {
            data.forEach((key, value) -> form.append("<input type=\"hidden\" name=\"".concat(key)
                    .concat("\" value=\"").concat(data.get(key).toString()).concat("\"/>\n")));
        }
        form.append("<input type=\"submit\" name=\"submit\" value=\"Send\"/>\n");
        form.append("</form>\n</body>\n</html>\n");
        return form.toString();
    }

    private WayForPayResponseDto jsonToWayForPayResponse(String json) {
        WayForPayResponseDto result = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                result = objectMapper.readValue(json, WayForPayResponseDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
