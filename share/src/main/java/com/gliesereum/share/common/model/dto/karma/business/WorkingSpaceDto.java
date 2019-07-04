package com.gliesereum.share.common.model.dto.karma.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonDeserializer;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.WorkingSpaceDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkingSpaceDto extends DefaultDto {

    private String name;

    private String description;

    private Integer indexNumber;

    private UUID businessId;

    private StatusSpace statusSpace;

    private UUID businessCategoryId;

    private List<WorkerDto> workers = new ArrayList<>();

    @JsonDeserialize(using = DescriptionJsonDeserializer.class)
    @JsonSerialize(using = DescriptionJsonSerializer.class)
    private List<WorkingSpaceDescriptionDto> descriptions = new ArrayList<>();

}
