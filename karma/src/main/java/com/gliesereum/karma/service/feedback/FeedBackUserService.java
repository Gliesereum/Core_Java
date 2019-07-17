package com.gliesereum.karma.service.feedback;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackUserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackUserService extends DefaultService<FeedBackUserDto, FeedBackUserEntity> {

    void recordFeedback(UUID recordId, String comment, Integer mark);

    List<FeedBackUserDto> getAllBySearch(FeedBackSearchDto search);
}