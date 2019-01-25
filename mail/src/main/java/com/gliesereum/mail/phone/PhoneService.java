package com.gliesereum.mail.phone;

/**
 * @author vitalij
 */
public interface PhoneService {

    void sendSingleMessage(String phone, String text);

    String checkBalance();
}
