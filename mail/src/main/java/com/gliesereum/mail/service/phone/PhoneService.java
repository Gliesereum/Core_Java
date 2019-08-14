package com.gliesereum.mail.service.phone;

/**
 * @author vitalij
 */
public interface PhoneService {

    void sendSingleMessage(String phone, String text, boolean devMode);

    String checkBalance();
}
