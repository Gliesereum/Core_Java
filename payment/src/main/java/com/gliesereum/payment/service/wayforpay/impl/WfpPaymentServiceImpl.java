package com.gliesereum.payment.service.wayforpay.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.wayforpay.*;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.*;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpPayTransactionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionalStatus;
import com.gliesereum.share.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WfpPaymentServiceImpl implements WfpPaymentService {

    @Value("${way-for-pay.merchant.key}")
    private String couplerKey;

    @Value("${way-for-pay.merchant.account}")
    private String couplerAccount;

    @Value("${way-for-pay.merchant.domain}")
    private String couplerDomainName;

    @Value("${way-for-pay.url.api}")
    private String apiUrl;

    @Value("${way-for-pay.url.purchase-response}")
    private String purchaseResponse;

    @Value("${way-for-pay.exchange.order-update-status-payment-info.exchange-name}")
    private String orderUpdateStatusPaymentExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WfpCardService cardService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private WfpOrderService orderService;

    @Autowired
    private WfpResponseCodeService responseCodeService;

    @Autowired
    private WfpOrderTransactionService transactionService;

    @Override
    public WfpPurchaseResponseModel purchase(WfpPurchaseRequestModel model) throws RuntimeException {
        WfpPurchaseResponseModel result = null;
        switch (model.getTransactionType()) {
            case CHARGE:
                result = purchaseCharge(model);
                break;
            case SETTLE:
            case REFUND:
                result = purchaseNewTransaction(model);
                break;
        }
        return result;
    }

    //======================= CHARGE ===========================================================

    private WfpPurchaseResponseModel purchaseCharge(WfpPurchaseRequestModel model) throws RuntimeException {
        WfpPurchaseResponseModel result = new WfpPurchaseResponseModel();
        if (model != null) {
            List<PublicUserDto> users = userExchangeService.findPublicUserByIds(Arrays.asList(model.getClientId()));
            if (CollectionUtils.isEmpty(users)) {
                throw new ClientException(USER_NOT_FOUND);
            }
            List<WfpCardDto> cards = cardService.getCardsByOwnerId(model.getClientId());
            if (CollectionUtils.isEmpty(cards)) {
                throw new ClientException(CARDS_NOT_FOUND_IN_USER);
            }
            cards = cards.stream().filter(WfpCardDto::isVerify).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(cards)) {
                throw new ClientException(VERIFY_CARDS_NOT_FOUND_IN_USER);
            }
            WfpCardDto card = cards.stream().filter(WfpCardDto::isFavorite).findFirst().orElse(cards.get(0));
            PublicUserDto user = users.get(0);
            WfpPaymentRequestDto params = getParamsForPurchaseCharge(model, user, card);

            ResponseEntity<String> payResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(params), String.class);
            if (payResponse.getStatusCode().is2xxSuccessful() && StringUtils.isNotBlank(payResponse.getBody())) {

                WfpPaymentResponseDto response = jsonToWayForPayPurchaseResponse(payResponse.getBody());

                List<String> md5Params = List.of(
                        response.getMerchantAccount(),
                        response.getOrderReference().toString(),
                        response.getAmount().toString(),
                        response.getCurrency(),
                        response.getAuthCode(),
                        response.getCardPan(),
                        response.getTransactionStatus().toString(),
                        response.getReasonCode().toString());

                String signature = generateHmacMD5(md5Params, couplerKey);

                if (signature != null && signature.equals(response.getMerchantSignature())) {

                    LocalDateTime operationDate = LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    long time = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();

                    sendStatusPaymentCharge(response.getOrderReference().toString(), signature, time);

                    WfpOrderDto order = modelMapper.map(response, WfpOrderDto.class);
                    WfpOrderTransactionDto transaction = modelMapper.map(response, WfpOrderTransactionDto.class);

                    if (order != null) {
                        order.setClientId(model.getClientId());
                        order = orderService.create(order);
                        if (order != null && order.getId() != null && transaction != null) {
                            transaction.setOrderId(order.getId());
                            transaction.setTransactionDate(operationDate);
                            transaction.setTransactionType(WfpTransactionType.CHARGE);
                            if (transaction.getPayTransactionType() == null) {
                                transaction.setPayTransactionType(model.getPayTransactionType());
                            }
                            transactionService.create(transaction);
                        }
                    }

                    result.setCode(responseCodeService.getByCode(response.getReasonCode()));
                    result.setTransactionStatus(response.getTransactionStatus());
                    result.setOrderId(response.getOrderReference());
                }
            }
        }
        return result;
    }


    private WfpPaymentRequestDto getParamsForPurchaseCharge(WfpPurchaseRequestModel model, PublicUserDto user, WfpCardDto card) {
        WfpPaymentRequestDto result = new WfpPaymentRequestDto();
        List<String> md5Params = List.of(couplerAccount, couplerDomainName,
                model.getOrderId().toString(), model.getAmount().toString(), "UAH",
                String.join(";", model.getProductName()),
                String.join(";", model.getProductCount().toString()),
                String.join(";", model.getProductPrice().toString()));
        String signature = generateHmacMD5(md5Params, couplerKey);
        if (model.getTransactionType() == null) {
            result.setTransactionType(WfpTransactionType.CHARGE.toString());
        } else {
            result.setTransactionType(model.getTransactionType().toString());
        }

        result.setMerchantAccount(couplerAccount);
        result.setMerchantDomainName(couplerDomainName);
        if (model.getPayTransactionType() != null) {
            result.setMerchantTransactionType(model.getPayTransactionType().toString());
        } else {
            result.setMerchantTransactionType(WfpPayTransactionType.AUTH.toString());
        }
        result.setMerchantTransactionSecureType("AUTO");
        result.setMerchantSignature(signature);
        result.setApiVersion(1);
        result.setOrderReference(model.getOrderId().toString());
        result.setOrderDate(model.getOrderDate());
        result.setAmount(model.getAmount());
        result.setCurrency("UAH");
        result.setProductName(model.getProductName());
        result.setProductPrice(model.getProductPrice());
        result.setProductCount(model.getProductCount());
        result.setClientFirstName(user.getFirstName());
        result.setClientLastName(user.getLastName());
        result.setClientEmail(user.getEmail());
        result.setClientPhone(user.getPhone());
        result.setClientCountry("UKR");
        result.setClientAccountId(user.getId().toString());
        result.setRecToken(card.getRecToken());
        return result;
    }

    private void sendStatusPaymentCharge(String orderReference, String signature, long time) {
        Map<String, Object> params =
                Map.of("orderReference", orderReference, "status", "accept", "time", time, "signature", signature);
        restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(params), String.class);
    }


    //==================== NEW TRANSACTION =====================================================================

    private WfpPurchaseResponseModel purchaseNewTransaction(WfpPurchaseRequestModel model) throws RuntimeException {
        WfpPurchaseResponseModel result = new WfpPurchaseResponseModel();

        WfpOrderDto order = orderService.getByOrderId(model.getOrderId());
        if (order == null) {
            throw new ClientException(ORDER_NOT_FOUND_BY_ID);
        }
        if (order.getTransactions() == null) {
            throw new ClientException(ORDER_DOES_NOT_HAVE_ANY_TRANSACTION);
        }
        checkModel(model, order);

        WfpPaymentRequestDto params = getParamsForPurchaseTransaction(model);

        ResponseEntity<String> payResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(params), String.class);

        if (payResponse.getStatusCode().is2xxSuccessful() && StringUtils.isNotBlank(payResponse.getBody())) {
            WfpPaymentResponseDto response = jsonToWayForPayPurchaseResponse(payResponse.getBody());

            List<String> md5Params = List.of(
                    response.getMerchantAccount(),
                    response.getOrderReference().toString(),
                    response.getTransactionStatus().toString(),
                    response.getReasonCode().toString());

            String signature = generateHmacMD5(md5Params, couplerKey);

            if (signature != null && signature.equals(response.getMerchantSignature())) {
                WfpOrderTransactionDto transaction = modelMapper.map(response, WfpOrderTransactionDto.class);
                LocalDateTime operationDate = LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
                if (transaction != null) {
                    transaction.setTransactionDate(operationDate);
                    transaction.setOrderId(order.getId());
                    transaction.setTransactionType(WfpTransactionType.SETTLE);
                    transactionService.create(transaction);
                }
                result.setCode(responseCodeService.getByCode(response.getReasonCode()));
                result.setTransactionStatus(response.getTransactionStatus());
                result.setOrderId(response.getOrderReference());
            }
        }
        return result;
    }

    private WfpPaymentRequestDto getParamsForPurchaseTransaction(WfpPurchaseRequestModel model) {
        WfpPaymentRequestDto result = new WfpPaymentRequestDto();
        List<String> md5Params = List.of(couplerAccount, model.getOrderId().toString(), model.getAmount().toString(), "UAH");
        String signature = generateHmacMD5(md5Params, couplerKey);
        result.setTransactionType(model.getTransactionType().toString());
        result.setMerchantAccount(couplerAccount);
        result.setOrderReference(model.getOrderId().toString());
        result.setAmount(model.getAmount());
        result.setCurrency("UAH");
        result.setMerchantSignature(signature);
        result.setApiVersion(1);
        result.setComment(model.getRefundComment());
        return result;
    }

    private void checkModel(WfpPurchaseRequestModel model, WfpOrderDto order) {
        switch (model.getTransactionType()) {
            case SETTLE:
                checkSettleModel(model, order);
                break;
            case REFUND:
                checkRefundModel(model, order);
                break;
        }
    }

    private void checkSettleModel(WfpPurchaseRequestModel model, WfpOrderDto order) {
        //todo add new check fields
        WfpOrderTransactionDto authTransaction = order.getTransactions()
                .stream().filter(f -> f.getPayTransactionType().equals(WfpPayTransactionType.AUTH) &&
                        f.getTransactionStatus().equals(WfpTransactionalStatus.Approved)).findFirst().orElse(null);
        if (authTransaction == null) {
            throw new ClientException(ORDER_DOES_NOT_HAVE_AUTH_TRANSACTION);
        }
        Double amount = model.getAmount();
        if (amount == null) {
            amount = authTransaction.getAmount();
        }
        if (amount > authTransaction.getAmount()) {
            throw new ClientException(YOU_CAN_NOT_SETTLE_MORE_THEN_IN_AUTH_TRANSACTION);
        }
    }

    private void checkRefundModel(WfpPurchaseRequestModel model, WfpOrderDto order) {
        //todo add new check fields
        WfpOrderTransactionDto chargeTransaction = order.getTransactions()
                .stream().filter(f -> f.getTransactionType().equals(WfpTransactionType.CHARGE) &&
                        f.getTransactionStatus().equals(WfpTransactionalStatus.Approved)).findFirst().orElse(null);

        WfpOrderTransactionDto settleTransaction = order.getTransactions()
                .stream().filter(f -> f.getTransactionType().equals(WfpTransactionType.SETTLE) &&
                        f.getTransactionStatus().equals(WfpTransactionalStatus.Approved)).findFirst().orElse(null);

        if (chargeTransaction == null && settleTransaction == null) {
            throw new ClientException(ORDER_DOES_NOT_HAVE_APPROVED_PAY_TRANSACTION);
        }

        Double payAmount = null;
        if (chargeTransaction != null &&
                chargeTransaction.getTransactionStatus().equals(WfpTransactionalStatus.Approved) &&
                chargeTransaction.getPayTransactionType().equals(WfpPayTransactionType.SALE)) {
            payAmount = chargeTransaction.getAmount();
        }
        if (settleTransaction != null &&
                settleTransaction.getTransactionStatus().equals(WfpTransactionalStatus.Approved)) {
            payAmount = settleTransaction.getAmount();
        }

        if (model.getAmount() > payAmount) {
            throw new ClientException(YOU_CAN_NOT_REFUND_MORE_THEN_IN_AUTH_TRANSACTION);
        }
    }

    //=================== COMMON =============================================================================

    private String generateHmacMD5(List<String> params, String key) {
        return CryptoUtil.encryptStringToHmacMD5(String.join(";", params), key);
    }

    private WfpPaymentResponseDto jsonToWayForPayPurchaseResponse(String json) {
        WfpPaymentResponseDto result = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                result = objectMapper.readValue(json, WfpPaymentResponseDto.class);
            } catch (IOException e) {
                log.error("Error while convert json to WayForPayResponse", e);
                throw new ClientException(SERVER_ERROR);
            }
        }
        return result;
    }

}
