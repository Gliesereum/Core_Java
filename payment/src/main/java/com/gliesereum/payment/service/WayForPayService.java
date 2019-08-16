package com.gliesereum.payment.service;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WayForPayService {

    void verifyCard();

    void verifyCardResponse(Map<String, String> response);
}
