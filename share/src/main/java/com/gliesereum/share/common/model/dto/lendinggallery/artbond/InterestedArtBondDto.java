package com.gliesereum.share.common.model.dto.lendinggallery.artbond;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InterestedArtBondDto extends DefaultDto {

    private UUID artBondId;

    private UUID customerId;

    public InterestedArtBondDto(UUID artBondId, UUID customerId) {
        this.artBondId = artBondId;
        this.customerId = customerId;
    }
}