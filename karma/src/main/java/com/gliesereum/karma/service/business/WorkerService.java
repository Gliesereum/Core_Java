package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.service.DefaultService;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerService extends DefaultService<WorkerDto, WorkerEntity> {

    List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId);

    List<WorkerDto> getByWorkingSpaceIdWithLock(UUID workingSpaceId);

    WorkerDto findByUserIdAndBusinessId(UUID userId, UUID businessId);

    List<WorkerDto> findByUserId(UUID userId);

    boolean checkWorkerExistByPhone(String phone);

    WorkerDto getById(UUID id, boolean setUsers);

    List<WorkerDto> getByBusinessId(UUID businessId, boolean setUsers);

    Page<WorkerDto> getByBusinessId(UUID businessId, boolean setUsers, Integer page, Integer size);

    List<LiteWorkerDto> getLiteWorkerByBusinessId(UUID id);

    List<LiteWorkerDto> getLiteWorkerByIds(List<UUID> ids);

    List<WorkerDto> findByUserIdAndCorporationId(UUID userId, UUID corporationId);

    List<WorkerDto> getByCorporationId(UUID corporationId);

    Page<WorkerDto> getByCorporationId(UUID corporationId, Integer page, Integer size);

    void setUsers(List<WorkerDto> result);

    boolean existByUserIdAndCorporationId(UUID userId, UUID corporationId);

    boolean existByUserIdAndBusinessId(UUID userId, UUID businessId);

    LiteWorkerDto getLiteWorkerById(UUID workerId);

    Map<UUID, LiteWorkerDto> getLiteWorkerMapByIds(Collection<UUID> collect);

    Map<UUID, List<WorkerDto>> getWorkerMapByBusinessIds(List<UUID> businessIds);

    WorkerDto createWorkerWithUser(WorkerDto worker);

    CommentDto addComment(UUID objectId, UUID userId, CommentDto comment);

    CommentDto updateComment(UUID userId, CommentDto comment);

    void deleteComment(UUID commentId, UUID userId);
}
