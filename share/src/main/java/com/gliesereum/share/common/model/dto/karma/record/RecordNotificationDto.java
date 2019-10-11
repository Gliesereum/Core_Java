package com.gliesereum.share.common.model.dto.karma.record;

import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RecordNotificationDto {
	
	private BaseRecordDto record;
	
	private BaseBusinessDto business;
	
	private List<UUID> userIds;
}
