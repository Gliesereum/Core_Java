package com.gliesereum.mail.service.phone.impl;

import com.gliesereum.mail.service.email.EmailService;
import com.gliesereum.mail.service.mail.MailStateService;
import com.gliesereum.mail.service.phone.PhoneService;
import com.gliesereum.mail.service.phone.provider.PhoneProvider;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author vitalij
 */
@Service
@Slf4j
public class PhoneServiceImpl implements PhoneService {

    @Value("${spring.mail.log-email}")
    private String logEmail;

    @Value("${phone.sendSms}")
    private boolean sendSmsEnable;

    @Autowired
    @Qualifier("acemountPhoneProvider")
    private PhoneProvider phoneProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailStateService mailStateService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void sendSingleMessage(String phone, String text, boolean devMode) {
        String message = "Message: " + text + "\nSend to phone: " + phone;
        log.info(message);
        if (sendSmsEnable && !devMode) {
            MailStateDto mailStateDto = phoneProvider.sendSingleMessage(phone, text);
            mailStateService.create(mailStateDto);
            log.info("Successful: {}", message);
            sendLogInfoToEmailAsync(message);
        }
    }

    @Override
    public String checkBalance() {
       return phoneProvider.checkBalance();
    }

    @Scheduled(fixedDelay = 60000)
    private void checkStatus() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(3L);
        List<MailStateDto> list = mailStateService.getByMessageStatusAndDateAfter("1", date);
        List<MailStateDto> listToUpdate = phoneProvider.checkStatus(list);
        if (CollectionUtils.isNotEmpty(listToUpdate)) {
            mailStateService.update(listToUpdate);
        }
    }

    private void sendLogInfoToEmailAsync(String message) {
        taskExecutor.execute(() -> {
            String logInfoToEmail = message + "\nBalance: " + checkBalance();
            emailService.sendSimpleMessage(logEmail, "Dispatch service info", logInfoToEmail);

        });
    }

}
