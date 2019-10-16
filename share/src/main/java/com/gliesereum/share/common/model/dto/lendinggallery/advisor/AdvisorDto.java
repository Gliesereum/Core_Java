package com.gliesereum.share.common.model.dto.lendinggallery.advisor;

import com.gliesereum.share.common.model.dto.AuditableDefaultDto;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdvisorDto extends AuditableDefaultDto {
	
	private UUID artBondId;
	
	private UUID userId;
	
	private String position;
	
	private PublicUserDto user;
}
