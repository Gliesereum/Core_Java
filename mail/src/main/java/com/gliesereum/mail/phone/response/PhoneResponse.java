package com.gliesereum.mail.phone.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitalij
 */
@Getter
@Setter
public class PhoneResponse {

    private String status;

    private String currency;

    private String balance;

    private String message_status;

    private PhoneResponseError error_info;

    public String getBalance() {
        return balance + " " + currency;
    }

    public String getError() {
        return "Error: " + error_info.getError_message();
    }
}
