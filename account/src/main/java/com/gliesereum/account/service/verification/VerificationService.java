package com.gliesereum.account.service.verification;

import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;

/**
 * @author vitalij
 */
public interface VerificationService {

    boolean checkVerification(String value, String code);

    void sendVerificationCode(String value, VerificationType type);
}
