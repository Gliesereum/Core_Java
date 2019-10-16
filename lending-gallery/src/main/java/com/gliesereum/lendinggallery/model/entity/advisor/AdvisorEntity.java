package com.gliesereum.lendinggallery.model.entity.advisor;

import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advisor")
public class AdvisorEntity extends AuditableDefaultEntity {
	
	@Column(name = "art_bond_id")
	private UUID artBondId;
	
	@Column(name = "user_id")
	private UUID userId;
	
	@Column(name = "position")
	private String position;
}
