package com.gliesereum.share.common.exchange.service.mail;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface MailExchangeService {

    String sendEmailVerification(String to, String code);

    String sendPhoneVerification(String to, String code);
}
