package com.gliesereum.share.common.model.dto.karma.business.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class BusinessDocumentServiceDto {
    
    private String name;
    
    private Integer price;
    
    private String serviceId;
    
    private String businessId;
    
    private Integer duration;
    
    private List<String> serviceClassIds;
    
    private List<String> filterIds;
    
    private List<String> filterAttributeIds;
    
}
