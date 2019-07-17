package com.gliesereum.karma.model.repository.jpa.feedback;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackSearchRepository {

    List<FeedBackUserEntity> getBySearch(FeedBackSearchDto search);
}
