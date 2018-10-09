package com.gliesereum.mail.email;

import java.util.List;

/**
 * @author vitalij
 * @since 10/4/18
 */
public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendSingleVerificationMessage(String to, String subject, String verificationLink, String code);

    void sendEmailsMessages(List<String> listTo, String subject, String text);

}
