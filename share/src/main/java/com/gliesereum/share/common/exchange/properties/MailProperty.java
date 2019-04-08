package com.gliesereum.share.common.exchange.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class MailProperty {

    private String emailVerification;

    private String phoneVerification;

    private String sentEmail;

    private String sendPhone;
}
