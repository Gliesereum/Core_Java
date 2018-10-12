package com.gliesereum.account.service.verification;

/**
 * @author vitalij
 * @since 10/12/18
 */
public interface VerificationService {

    boolean checkVerification(String value, String code);

    void saveVerification(String value, String code);
}
