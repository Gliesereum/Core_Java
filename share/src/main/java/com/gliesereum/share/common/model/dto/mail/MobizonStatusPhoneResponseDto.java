package com.gliesereum.share.common.model.dto.mail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class MobizonStatusPhoneResponseDto {

    private Integer code;

    private List<Map<String, Object>> data;

    private String message;
}
