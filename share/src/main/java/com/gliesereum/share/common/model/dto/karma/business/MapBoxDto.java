package com.gliesereum.share.common.model.dto.karma.business;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class MapBoxDto {

    private String type;

    private List<String> query;

    private List<Map<String, Object>> features;

    private String attribution;
}