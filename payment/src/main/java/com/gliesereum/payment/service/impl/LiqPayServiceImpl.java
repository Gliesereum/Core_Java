package com.gliesereum.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.payment.service.LiqPayService;
import com.gliesereum.payment.service.PaymentRecipientService;
import com.gliesereum.share.common.model.dto.payment.LiqPayRequestDto;
import com.gliesereum.share.common.model.dto.payment.LiqPayStatusRequestDto;
import com.gliesereum.share.common.model.dto.payment.PaymentRecipientDto;
import com.gliesereum.share.common.util.CryptoUtil;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class LiqPayServiceImpl implements LiqPayService {

    @Value("${liq-pay.crypto.salt}")
    private String salt;

    @Value("${liq-pay.crypto.password}")
    private String password;

   private LiqPay liqpay = new LiqPay("sandbox_i68717225710", "sandbox_MbnwJt32pvmXEFielzRaM2gYB4tauUmpYM4a3qFs");

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PaymentRecipientService recipientService;

    @Autowired
    private FanoutExchange orderUpdateInfoExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void twoStagePayment(LiqPayRequestDto request) {

    }

    @Override
    public void securePayment(LiqPayRequestDto request) {

    }

    @Override
    public void returnPayment(LiqPayRequestDto request) {

    }

    @Override
    public void getStatus(LiqPayRequestDto request) {

    }

    @Override
    public void callBack(LiqPayStatusRequestDto request) {
        if (request != null && ObjectUtils.allNotNull(request.getData(), request.getSignature())) {
            Map<String, Object> data = jsonToMap(request.getData());
            if (MapUtils.isNotEmpty(data) && data.get("public_key") != null) {
                final String publicKey = data.get("public_key").toString();
                PaymentRecipientDto recipient = recipientService.getByPublicKey(publicKey);
                if (recipient != null) {
                    String privateKey = CryptoUtil.decryptAes256(recipient.getPrivateKey(), password, salt);
                    String sign = LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey.concat(request.getData()).concat(privateKey)));
                    if (sign.equals(request.getSignature())) {

                        //todo:  update order, sent status in queue
                        rabbitTemplate.convertAndSend(orderUpdateInfoExchange.getName(), "", request); //todo set object
                    }
                }
            }
        }
    }

    @Override
    public void test() {
        HashMap<String, String> params_1 = new HashMap<String, String>();
        params_1.put("action", "pay");
        params_1.put("amount", "1");
        params_1.put("currency", "UAH");
        params_1.put("description", "description text");
        params_1.put("order_id", "order_id_3");
        params_1.put("version", "3");
        String html = liqpay.cnb_form(params_1);
        System.out.println(html);

       /* HashMap<String, String> params_2 = new HashMap<String, String>();
        params_2.put("action", "auth");
        params_2.put("version", "3");
        params_2.put("phone", "380996438310");
        params_2.put("amount", "1");
        params_2.put("currency", "UAH");
        params_2.put("description", "add cart");
        params_2.put("order_id", "order_id_2");
        params_2.put("card", "4731185615124168");
        params_2.put("card_exp_month", "10");
        params_2.put("card_exp_year", "26");
        params_2.put("card_cvv", "892");
        HashMap<String, Object> res = null;
        try {
            res = (HashMap<String, Object>) liqpay.api("request", params_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String json = JSONObject.toJSONString(res);
        System.out.println(res.get("status"));*/
    }

    private Map<String, Object> jsonToMap(String json) {
        Map<String, Object> result = null;
        try {
            result = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
