package com.gliesereum.payment.service.wayforpay.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.wayforpay.WayForPayPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WayForPayPaymentServiceImpl implements WayForPayPaymentService {

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

    @Value("${way-for-pay.url.verify-card-response}")
    private String verifyCartResponse;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;



}
