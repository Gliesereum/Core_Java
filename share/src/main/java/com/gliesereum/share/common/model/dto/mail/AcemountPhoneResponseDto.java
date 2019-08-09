package com.gliesereum.share.common.model.dto.mail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author vitalij
 */
@Data
@NoArgsConstructor
public class AcemountPhoneResponseDto {

    private Map<String, Object> success_request;

    private Map<String, Object> successful_request;

}
