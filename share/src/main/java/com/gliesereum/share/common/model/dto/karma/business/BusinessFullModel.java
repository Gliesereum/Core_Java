package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
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
public class BusinessFullModel extends DefaultDto {

    private String name;

    private UUID corporationId;

    private UUID businessId;

    private String description;

    private String logoUrl;

    private String address;

    private String phone;

    private String addPhone;

    private Double latitude;

    private Double longitude;

    private Integer timeZone;

    private RatingDto rating;

    private ObjectState objectState;

    private UUID businessCategoryId;

    private BusinessCategoryDto businessCategory;

    private List<WorkTimeDto> workTimes = new ArrayList<>();

    private List<WorkingSpaceDto> spaces = new ArrayList<>();

    private List<BusinessDescriptionDto> descriptions = new ArrayList<>();

    private List<ServicePriceDto> servicePrices = new ArrayList<>();

    private List<PackageDto> packages = new ArrayList<>();

    private List<MediaDto> media = new ArrayList<>();

    private List<CommentFullDto> comments = new ArrayList<>();

    private List<BaseRecordDto> records = new ArrayList<>();
}
