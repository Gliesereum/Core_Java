package com.gliesereum.share.common.model.dto.karma.business.document;

import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class BusinessDocumentDto {
    
    private String id;
    
    private String corporationId;
    
    private String name;
    
    private Integer countBox;
    
    private String description;
    
    private String address;
    
    private String logoUrl;
    
    private String coverUrl;
    
    private String siteUrl;
    
    private String videoUrl;
    
    private String phone;
    
    private String addPhone;
    
    private Double latitude;
    
    private Double longitude;
    
    private Integer timeZone;
    
    private Double rating;
    
    private Integer ratingCount;
    
    private Boolean businessVerify;
    
    private Map<String, Double> geoPoint;
    
    private UUID businessCategoryId;
    
    private ObjectState objectState;
    
    private List<BusinessDocumentServiceDto> services;
    
    private List<BusinessDocumentWorkTimeDto> workTimes;
    
    private List<String> serviceNames;
    
    private List<String> tags;
    
    private Float score;
    
}
