package com.gliesereum.account.service.verification.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.account.model.domain.VerificationDomain;
import com.gliesereum.account.model.repository.redis.VerificationRepository;
import com.gliesereum.account.mq.MessagePublisher;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author vitalij
 * @since 10/12/18
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    private final static Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Autowired
    private VerificationRepository repository;

    @Autowired
    private MessagePublisher publisher;

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
        Random random = new Random();
        String code = String.valueOf(random.ints(100000, (999998 + 1)).limit(1).findFirst().getAsInt());
        VerificationDomain domain = new VerificationDomain();
        domain.setId(value + code);
        domain.setCreateDate(LocalDateTime.now());
        repository.save(domain);
        sendCode(value, code, type);
    }

    private void sendCode(String value, String code, VerificationType type) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put("code", code);
            model.put("type", type.toString());
            model.put("value", value);
            ObjectMapper mapper = new ObjectMapper();
            publisher.publish(mapper.writeValueAsString(model));
            logger.info(model.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

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
