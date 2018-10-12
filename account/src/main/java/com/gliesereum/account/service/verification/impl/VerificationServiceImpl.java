package com.gliesereum.account.service.verification.impl;

import com.gliesereum.account.model.domain.VerificationDomain;
import com.gliesereum.account.model.repository.redis.VerificationRepository;
import com.gliesereum.account.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author vitalij
 * @since 10/12/18
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationRepository repository;

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
    public void saveVerification(@NotNull String value, @NotNull String code) {
        String id = value + code;
        VerificationDomain domain = new VerificationDomain();
        domain.setId(id);
        domain.setCreateDate(LocalDateTime.now());
        repository.save(domain);
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
