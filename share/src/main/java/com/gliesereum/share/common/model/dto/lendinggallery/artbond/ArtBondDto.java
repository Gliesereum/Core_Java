package com.gliesereum.share.common.model.dto.lendinggallery.artbond;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.media.MediaDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtBondDto extends DefaultDto {

    private Integer price;

    private String name;

    private String description;

    private String author;

    private String execution;

    private String size;

    private String dated;

    private String location;

    private String state;

    private String origin;

    private String exhibitions;

    private String literature;

    private StatusType statusType;

    private SpecialStatusType specialStatusType;

    private List<MediaDto> images = new ArrayList<>();

    private List<MediaDto> authorInfo = new ArrayList<>();

    private List<MediaDto> artBondInfo = new ArrayList<>();

    private List<MediaDto> documents = new ArrayList<>();

}
