package com.gliesereum.account.service.verification.impl;

import com.gliesereum.account.model.domain.VerificationDomain;
import com.gliesereum.account.model.repository.redis.VerificationRepository;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.exchange.service.mail.MailExchangeService;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationRepository repository;

    @Autowired
    private MailExchangeService mailExchangeService;

    private final Random random;

/*    @Autowired
    private Environment environment;

    @Autowired
    private RabbitTemplate template;

    private final String MAIL_QUEUE = "spring.rabbitmq.queue-mail";*/

    public VerificationServiceImpl() {
        random = new Random();
    }

    @Override
    public boolean checkVerification(@NotNull String value, @NotNull String code) {
        boolean result = false;
        String id = value + code;
        Optional<VerificationDomain> domain = repository.findById(id);
        if (domain.isPresent() && domain.get().getCreateDate().isAfter(LocalDateTime.now().minusMinutes(3L))) {
            repository.deleteById(id);
            result = true;
        }
        return result;
    }

    @Override
    public void sendVerificationCode(@NotNull String value, VerificationType type) {
        String code = String.valueOf(random.ints(100000, (999998 + 1)).limit(1).findFirst().getAsInt());
        VerificationDomain domain = new VerificationDomain();
        domain.setId(value + code);
        domain.setCreateDate(LocalDateTime.now());
        repository.save(domain);
        sendCode(value, code, type);
    }

    private void sendCode(String value, String code, VerificationType type) {
        if (type.equals(VerificationType.PHONE)) {
            mailExchangeService.sendPhoneVerification(value, code);
        } else if (type.equals(VerificationType.EMAIL)) {
            mailExchangeService.sendEmailVerification(value, code);
        }
    }

  /*  private void sendCode(String value, String code, VerificationType type) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put("code", code);
            model.put("type", type.toString());
            model.put("value", value);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(model);
            template.convertAndSend(environment.getRequiredProperty(MAIL_QUEUE), json);
            log.info(model.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }*/

    @Scheduled(fixedDelay = 600000)
    private void deleteByTime() {
        List<VerificationDomain> list = (List<VerificationDomain>) repository.findAll();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(f -> {
                if (f.getCreateDate().isBefore(LocalDateTime.now().minusMinutes(3L))) {
                    repository.delete(f);
                }
            });
        }
    }
}
