package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import lombok.Data;
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
public class BusinessFullModel {

    private String name;

    private UUID businessId;

    private String description;

    private String logoUrl;

    private String address;

    private String phone;

    private String addPhone;

    private Double latitude;

    private Double longitude;

    private RatingDto rating;

    private List<WorkTimeDto> workTimes = new ArrayList<>();

    private List<WorkingSpaceDto> spaces = new ArrayList<>();

    private List<ServicePriceDto> servicePrices = new ArrayList<>();

    private List<PackageDto> packages = new ArrayList<>();

    private List<MediaDto> media = new ArrayList<>();

    private List<CommentDto> comments = new ArrayList<>();

    private List<BaseRecordDto> records = new ArrayList<>();
}
