package com.gliesereum.mail.controllers;

import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.service.phone.PhoneService;
import com.gliesereum.share.common.model.response.MapResponse;
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
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/phone/single")
    public MapResponse sendSingleMessageToPhone(@RequestParam(value = "to") String phone,
                                                @RequestParam(value = "message") String message) {
        phoneService.sendSingleMessage(phone, message);
        return MapResponse.resultTrue();
    }

    @PostMapping(value = "/phone/check/balance")
    public String checkBalance() {
        return phoneService.checkBalance();
    }

    @PostMapping(value = "/email/single")
    public void sendSingleMessageToEmail(@RequestParam(value = "to") String to,
                                         @RequestParam(value = "subject") String subject,
                                         @RequestParam(value = "message") String message) {
        emailService.sendSimpleMessage(to, subject, message);
    }

    @PostMapping(value = "/email/verification")
    public MapResponse sendSingleVerificationMessage(@RequestParam(value = "to") String to,
                                                     @RequestParam(value = "message") String message) {
        emailService.sendSingleVerificationMessage(to, message);
        return MapResponse.resultTrue();
    }

    @PostMapping(value = "/email/distribution")
    public void sendEmailsMessages(@RequestParam(value = "listTo") List<String> listTo,
                                   @RequestParam(value = "subject") String subject,
                                   @RequestParam(value = "message") String message) {
        emailService.sendEmailsMessages(listTo, subject, message);
    }
}
