package com.gliesereum.payment.service.wayforpay.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.wayforpay.WfpCardService;
import com.gliesereum.payment.service.wayforpay.WfpVerifyCardService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.payment.payment.RequestCardInfoDto;
import com.gliesereum.share.common.model.dto.payment.payment.UserCardDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCallBackResponseDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCardDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpVerifyCardRequestDto;
import com.gliesereum.share.common.util.CryptoUtil;
import com.gliesereum.share.common.util.SecurityUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WfpVerifyCardServiceImpl implements WfpVerifyCardService {

    @Value("${way-for-pay.merchant.key}")
    private String couplerKey;

    @Value("${way-for-pay.merchant.account}")
    private String couplerAccount;

    @Value("${way-for-pay.merchant.domain}")
    private String couplerDomainName;

    @Value("${way-for-pay.url.verify}")
    private String verifyUrl;

    @Value("${way-for-pay.url.api}")
    private String apiUrl;

    @Value("${way-for-pay.url.verify-response}")
    private String verifyCartResponse;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WfpCardService cardService;

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void verifyCardResponse(Map<String, String> map) {
        if (MapUtils.isNotEmpty(map)) {
            String json = (String) map.keySet().toArray()[0];
            WfpCallBackResponseDto response = jsonToWayForPayResponse(json);
            List<String> md5ParamsCheckSignature = List.of(couplerAccount, response.getOrderReference(), response.getAmount().toString(), response.getCurrency());
            if (checkSignatureVerifyCard(response.getMerchantSignature(), md5ParamsCheckSignature)) {
                saveCard(response);
                long time = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
                List<String> md5ParamsStatusVerify = List.of(response.getOrderReference(), "accept", String.valueOf(time));
                String signature = generateHmacMD5(md5ParamsStatusVerify, couplerKey);
                sendStatusVerify(response.getOrderReference(), signature, time);
            }
        }
    }

    @Override
    public String getFormVerifyCard() {
        SecurityUtil.checkUserByBanStatus();
        WfpVerifyCardRequestDto params = getParamsForVerifyCard();
        return renderHtmlForm(params);
    }

    @Override
    public UserCardDto addNewCard(RequestCardInfoDto card) {
        UserCardDto result = null;
        SecurityUtil.checkUserByBanStatus();
        WfpVerifyCardRequestDto params = getParamsForVerifyCard();
        if (ObjectUtils.allNotNull(params, card)) {
            /*String userId = SecurityUtil.getUserId().toString(); //todo decrypt date of card
            params.setCard(decryptDate(card.getCard(), userId));
            params.setCardCvv(decryptDate(card.getCardCvv(), userId));
            params.setCardHolder(decryptDate(card.getCardHolder(), userId));
            params.setExpMonth(decryptDate(card.getExpMonth(), userId));
            params.setExpYear(decryptDate(card.getExpYear(), userId));*/
            params.setCard(card.getCard());
            params.setCardCvv(card.getCardCvv());
            params.setCardHolder(card.getCardHolder());
            params.setExpMonth(card.getExpMonth());
            params.setExpYear(card.getExpYear());
        }
        ResponseEntity<String> response =
                restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(params), String.class);
        if (response.getStatusCode().is2xxSuccessful() && StringUtils.isNotBlank(response.getBody())) {
            WfpCallBackResponseDto verifyResponse = jsonToWayForPayResponse(response.getBody());

            List<String> md5ParamsCheckSignature = List.of(couplerAccount,
                    verifyResponse.getOrderReference(),
                    verifyResponse.getAmount().toString(),
                    verifyResponse.getCurrency(),
                    verifyResponse.getAuthCode(),
                    verifyResponse.getCardPan(),
                    verifyResponse.getTransactionStatus(),
                    verifyResponse.getReasonCode().toString());

            if (checkSignatureVerifyCard(verifyResponse.getMerchantSignature(), md5ParamsCheckSignature)) {
                WfpCardDto wayForPayCard = saveCard(verifyResponse);
                if (wayForPayCard != null) {
                    result = modelMapper.map(wayForPayCard, UserCardDto.class);
                }
                long time = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
                List<String> md5ParamsStatusVerify = List.of(verifyResponse.getOrderReference(), "accept", couplerKey);
                String signature = generateHmacMD5(md5ParamsStatusVerify, couplerKey);
                sendStatusVerify(verifyResponse.getOrderReference(), signature, time);
            }
        }
        return result;
    }

    private WfpVerifyCardRequestDto getParamsForVerifyCard() {
        WfpVerifyCardRequestDto result = new WfpVerifyCardRequestDto();
        WfpCardDto newCard = cardService.create(new WfpCardDto(SecurityUtil.getUserId(), false));
        UUID orderReference = newCard.getId();
        List<String> md5Params = List.of(couplerAccount, couplerDomainName, orderReference.toString(), String.valueOf(1), "UAH");
        String signature = generateHmacMD5(md5Params, couplerKey);
        if (StringUtils.isNotBlank(signature)) {
            result.setAmount(1.0);
            result.setCurrency("UAH");
            result.setApiVersion(1);
            result.setTransactionType("VERIFY");
            result.setServiceUrl(verifyCartResponse);
            result.setMerchantSignature(signature);
            result.setMerchantAccount(couplerAccount);
            result.setMerchantDomainName(couplerDomainName);
            result.setMerchantAuthType("SimpleSignature");
            result.setOrderReference(orderReference.toString());
        }
        return result;
    }

    @Override
    public WfpCardDto saveCard(WfpCallBackResponseDto response) {
        WfpCardDto result = null;
        if (response != null) {
            UUID idOrder = UUID.fromString(response.getOrderReference());
            WfpCardDto saveCard = cardService.getById(idOrder);
            if (saveCard != null) {
                WfpCardDto card = modelMapper.map(response, WfpCardDto.class);
                card.setId(saveCard.getId());
                card.setFavorite(saveCard.isFavorite());
                card.setOwnerId(saveCard.getOwnerId());
                if (response.getReasonCode() == 1100) {
                    card.setVerify(true);
                }
                result = cardService.update(card);
            }
        }
        return result;
    }

    private boolean checkSignatureVerifyCard(String merchantSignature, List<String> md5Params) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(md5Params)) {
            String signature = generateHmacMD5(md5Params, couplerKey);
            result = signature.equals(merchantSignature);
        }
        return result;
    }

    private void sendStatusVerify(String orderReference, String signature, long time) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderReference", orderReference);
        params.put("status", "accept");
        params.put("time", time);
        params.put("signature", signature);
        restTemplate.exchange(verifyUrl, HttpMethod.POST, new HttpEntity<>(params), String.class);
    }


    private String generateHmacMD5(List<String> params, String key) {
        return CryptoUtil.encryptStringToHmacMD5(String.join(";", params), key);
    }

    private String renderHtmlForm(WfpVerifyCardRequestDto request) {
        String result = null;
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
            Template template = freemarkerConfig.getTemplate("verify-form.ftl");
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, request);
        } catch (Exception e) {
            log.error("Error while create html form", e);
            throw new ClientException(SERVER_ERROR);
        }
        return result;
    }

    private WfpCallBackResponseDto jsonToWayForPayResponse(String json) {
        WfpCallBackResponseDto result = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                result = objectMapper.readValue(json, WfpCallBackResponseDto.class);
            } catch (IOException e) {
                log.error("Error while convert json to WayForPayResponse", e);
                throw new ClientException(SERVER_ERROR);
            }
        }
        return result;
    }

    private String decryptDate(String date, String key) {
        String result = null;
        if (StringUtils.isNotBlank(date) && StringUtils.isNotBlank(key)) {
            result = CryptoUtil.decryptAes256(date, key, key);
        }
        return result;
    }

}
