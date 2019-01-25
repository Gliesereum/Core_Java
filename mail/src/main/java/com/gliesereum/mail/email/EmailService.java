package com.gliesereum.mail.email;

import java.util.List;

/**
 * @author vitalij
 */
public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendSingleVerificationMessage(String to, String subject, String code);

    void sendEmailsMessages(List<String> listTo, String subject, String text);

}
