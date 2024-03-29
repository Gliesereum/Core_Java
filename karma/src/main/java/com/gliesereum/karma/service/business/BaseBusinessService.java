package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.EmptyBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultService;
import com.gliesereum.share.common.service.auditable.AuditableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseBusinessService extends AuditableService<BaseBusinessDto, BaseBusinessEntity> {

    List<BaseBusinessDto> getAllIgnoreState();

    List<LiteBusinessDto> getAllLite();

    Page<LiteBusinessDto> getAllLite(ObjectState objectState, Pageable pageable);

    LiteBusinessDto getLiteById(UUID businessId);

    BaseBusinessDto getByIdAndLock(UUID businessId);

    boolean existByIdAndCorporationIds(UUID id, List<UUID> corporationIds);

    List<BaseBusinessDto> getByCorporationIds(List<UUID> corporationIds);

    List<BusinessFullModel> getFullModelByIds(List<UUID> id);

    List<BaseBusinessDto> getByCorporationId(UUID corporationId);

    BaseBusinessDto getByIdIgnoreState(UUID id);

    List<BaseBusinessDto> getAllBusinessByCurrentUser();

    List<UUID> getAllBusinessIdsByCurrentUser();

    List<BusinessFullModel> getAllFullBusinessByCurrentUser();

    void deleteByCorporationIdCheckPermission(UUID id);

    void deleteByCorporationId(UUID id);

    List<UUID> getIdsByCorporationIds(List<UUID> corporationIds);

    Map<UUID, LiteBusinessDto> getLiteBusinessMapByIds(Collection<UUID> collect);

    List<LiteBusinessDto> getLiteBusinessByIds(Collection<UUID> ids);

    CommentDto addComment(UUID objectId, UUID userId, CommentDto comment);

    CommentDto updateComment(UUID userId, CommentDto comment);

    void deleteComment(UUID commentId, UUID userId);

    BaseBusinessDto createEmptyBusiness(EmptyBusinessDto business);

    BaseBusinessDto moveGeoPoint(UUID businessId, String address, Double latitude, Double longitude, Integer timeZone);

    List<TagDto> addTag(UUID tagId, UUID businessId);

    List<TagDto> removeTag(UUID tagId, UUID businessId);
    
    List<TagDto> getTags(UUID businessId);
    
    List<TagDto> saveTags(List<UUID> tagId, UUID businessId);
}

