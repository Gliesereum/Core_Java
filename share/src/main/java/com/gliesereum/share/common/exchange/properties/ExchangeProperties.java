package com.gliesereum.share.common.exchange.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "exchange.endpoint")
public class ExchangeProperties {

    private AccountProperty account;

    private PermissionProperty permission;

    private MailProperty mail;

    private KarmaProperties karma;
}
