package com.gliesereum.share.common.model.dto.karma.business.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.business.group.enumerated.BusinessGroupBy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessGroupSearchDto extends BusinessSearchDto {
    
    private Long countInGroups;
    
    private List<BusinessGroupBy> groups;
    
    private List<String> tagGroups;
	
	@JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
	@JsonSerialize(using = LocalDateTimeJsonSerializer.class)
	private LocalDateTime dateFromRecordSearch;
}
