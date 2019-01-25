package com.gliesereum.mail.controllers;

import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vitalij
 */
@RestController
@RequestMapping(value = "/")
public class MailController {

    @Autowired
    PhoneService phoneService;

    @Autowired
    EmailService emailService;

    @PostMapping(value = "/phone/single")
    public void sendSingleMessageToPhone(@RequestParam(value = "phone") String phone,
                                         @RequestParam(value = "text") String text) {
        phoneService.sendSingleMessage(phone, text);
    }

    @PostMapping(value = "/phone/check/balance")
    public String checkBalance() {
        return phoneService.checkBalance();
    }

    @PostMapping(value = "/email/single")
    public void sendSingleMessageToEmail(@RequestParam(value = "to") String to,
                                         @RequestParam(value = "subject") String subject,
                                         @RequestParam(value = "text") String text) {
        emailService.sendSimpleMessage(to, subject, text);
    }

    @PostMapping(value = "/email/verification")
    public void sendSingleVerificationMessage(@RequestParam(value = "to") String to,
                                              @RequestParam(value = "subject") String subject,
                                              @RequestParam(value = "code") String code) {
        emailService.sendSingleVerificationMessage(to, subject, code);
    }

    @PostMapping(value = "/email/distribution")
    public void sendEmailsMessages(@RequestParam(value = "listTo") List<String> listTo,
                                   @RequestParam(value = "subject") String subject,
                                   @RequestParam(value = "text") String text) {
        emailService.sendEmailsMessages(listTo, subject, text);
    }
}
