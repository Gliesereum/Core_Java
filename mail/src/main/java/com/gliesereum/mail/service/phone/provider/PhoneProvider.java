package com.gliesereum.mail.service.phone.provider;

import com.gliesereum.share.common.model.dto.mail.MailStateDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface PhoneProvider {

    MailStateDto sendSingleMessage(String phone, String text);

    String checkBalance();

    List<MailStateDto> checkStatus(List<MailStateDto> list);
}
