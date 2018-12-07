package com.gliesereum.share.common.model.dto.permission.uri;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUriDto {

    private String modulePath;

    private String endpointPath;
}
