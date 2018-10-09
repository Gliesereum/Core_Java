package com.gliesereum.mail.phone;

/**
 * @author vitalij
 * @since 10/4/18
 */
public interface PhoneService {

    void sendSingleMessage(String phone, String text);

    String checkBalance();
}
