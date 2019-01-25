package com.gliesereum.share.common.logging.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@ToString
@NoArgsConstructor
public class RequestInfo {

    private Long startTime;

    private Long endTime;

    private String requestBody;

    private String responseBody;

    private boolean isError;

    private String errorMessage;

    private String method;

    private String uri;

    private Integer httpStatus;

    private Map<String, String> headers;

    private Map<String, String[]> parameters;
}
