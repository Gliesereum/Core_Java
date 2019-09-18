package com.gliesereum.mail.service.feedback.impl;

import com.gliesereum.mail.model.entity.FeedBackRequestEntity;
import com.gliesereum.mail.model.repository.jpa.FeedBackRequestRepository;
import com.gliesereum.mail.service.email.EmailService;
import com.gliesereum.mail.service.feedback.FeedBackRequestService;
import com.gliesereum.mail.service.feedback.FeedBackUserService;
import com.gliesereum.mail.service.phone.PhoneService;
import com.gliesereum.mail.service.telegram.FeedBackTelegramBotService;
import com.gliesereum.mail.service.telegram.TelegramBotService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserPhoneExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.model.dto.mail.FeedBackRequestDto;
import com.gliesereum.share.common.model.dto.mail.FeedBackUserDto;
import com.gliesereum.share.common.model.dto.mail.TelegramBotDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.NOT_PHONE_BY_REGEX;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FeedBackRequestServiceImpl extends DefaultServiceImpl<FeedBackRequestDto, FeedBackRequestEntity> implements FeedBackRequestService {

    private static final Class<FeedBackRequestDto> DTO_CLASS = FeedBackRequestDto.class;
    private static final Class<FeedBackRequestEntity> ENTITY_CLASS = FeedBackRequestEntity.class;

    private static final String LOG_EMAIL = "spring.mail.log-email";

    private final FeedBackRequestRepository feedBackRequestRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private FeedBackUserService userService;

    @Autowired
    private UserPhoneExchangeService exchangeService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FeedBackTelegramBotService feedBackTelegramBotService;

    @Autowired
    public FeedBackRequestServiceImpl(FeedBackRequestRepository feedBackRequestRepository, DefaultConverter defaultConverter) {
        super(feedBackRequestRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.feedBackRequestRepository = feedBackRequestRepository;
    }

    @Override
    @Transactional
    public FeedBackRequestDto create(FeedBackRequestDto dto) {
        if (!RegexUtil.phoneIsValid(dto.getPhone())) {
            throw new ClientException(NOT_PHONE_BY_REGEX);
        }
        boolean exist = feedBackRequestRepository.existsByPhone(dto.getPhone());
        dto.setCreateDate(LocalDateTime.now());
        FeedBackRequestDto result = super.create(dto);
        if (result != null) {
            String message = "New feedback request: ".concat(dto.getPhone());
            String logMessage = "Message: " + message + " vas send to: ";
            feedBackTelegramBotService.sendMessageToChatBot(message);
            /*if (!exist) {
                //todo add get User by app id
                List<FeedBackUserDto> feedBackUsers = userService.getAll();
                if (CollectionUtils.isNotEmpty(feedBackUsers)) {
                    List<UserPhoneDto> phones = exchangeService.findUserPhoneByUserIds(feedBackUsers.stream().map(FeedBackUserDto::getUserId).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(phones)) {
                        phones.forEach(userPhone -> {
                            phoneService.sendSingleMessage(userPhone.getPhone(), message, false);
                            log.info(logMessage.concat(userPhone.getPhone()));
                        });
                    }
                }
            } else {
                String email = environment.getProperty(LOG_EMAIL);
                emailService.sendSimpleMessage(email, "Feedback", message);
                log.info(logMessage.concat(email));
            }*/
        }
        return result;
    }


}
