package com.gliesereum.account.service.verification;

import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;

/**
 * @author vitalij
 * @since 10/12/18
 */
public interface VerificationService {

    boolean checkVerification(String value, String code);

    void sendVerificationCode(String value, VerificationType type);
}
