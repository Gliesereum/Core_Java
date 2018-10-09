package com.gliesereum.mail.phone.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitalij
 * @since 10/5/18
 */
@Getter
@Setter
public class PhoneResponseError {

    private String error_code;

    private String error_message;

}
