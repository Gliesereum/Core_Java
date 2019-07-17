package com.gliesereum.karma.service.feedback.impl;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import com.gliesereum.karma.model.repository.jpa.feedback.FeedBackUserRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.karma.service.feedback.FeedBackUserService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackUserDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FeedBackUserServiceImpl extends DefaultServiceImpl<FeedBackUserDto, FeedBackUserEntity> implements FeedBackUserService {

    private static final Class<FeedBackUserDto> DTO_CLASS = FeedBackUserDto.class;
    private static final Class<FeedBackUserEntity> ENTITY_CLASS = FeedBackUserEntity.class;

    private final FeedBackUserRepository feedBackUserRepository;

    @Autowired
    private BaseRecordService recordService;

    @Autowired
    private BaseBusinessService businessService;

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    public FeedBackUserServiceImpl(FeedBackUserRepository feedBackUserRepository, DefaultConverter defaultConverter) {
        super(feedBackUserRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.feedBackUserRepository = feedBackUserRepository;
    }

    @Override
    @Transactional
    public void recordFeedback(UUID recordId, String comment, Integer mark) {
        SecurityUtil.checkUserByBanStatus();
        BaseRecordDto record = recordService.getById(recordId);
        if (record == null) {
            throw new ClientException(RECORD_NOT_FOUND);
        }
        if (!record.getClientId().equals(SecurityUtil.getUserId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_RECORD);
        }
        FeedBackUserDto feedBack = new FeedBackUserDto();
        feedBack.setBusinessId(record.getBusinessId());
        feedBack.setClientId(record.getClientId());
        feedBack.setComment(comment);
        feedBack.setMark(mark);
        feedBack.setObjectId(record.getId());
        feedBack.setWorkerId(record.getWorkerId());
        feedBack.setWorkingSpaceId(record.getWorkingSpaceId());
        feedBack.setDateFeedback(LocalDateTime.now());
        feedBack.setDateCreateObject(record.getBegin());
        super.create(feedBack);
    }

    @Override
    public List<FeedBackUserDto> getAllBySearch(FeedBackSearchDto search) {
        checkPermission(search);
        List<FeedBackUserDto> result = null;
        List<FeedBackUserEntity> entities = feedBackUserRepository.getBySearch(search);
        if (CollectionUtils.isNotEmpty(entities)) {
            result = converter.convert(entities, dtoClass);
           // setFullModel(result);
        }
        return result;
    }

    /*private void setFullModel(List<FeedBackUserDto> result) {
        Map<UUID, PublicUserDto> clients = new HashMap<>();
        Map<UUID, LiteWorkerDto> workers = new HashMap<>();
        Map<UUID, LiteWorkingSpaceDto> workerSpaces = new HashMap<>();
        Map<UUID, LiteBusinessDto> businesses = new HashMap<>();
        result.forEach(f->{
            LiteBusinessDto business = businesses.get(f.getBusinessId());
            if(business == null){
              business =
            }
            f.setBusiness(business);
        });
    }*/

    private void checkPermission(FeedBackSearchDto search) {
        if (CollectionUtils.isEmpty(search.getBusinessIds()) &&
                CollectionUtils.isEmpty(search.getCorporationIds())) {
            throw new ClientException(BUSINESS_IDS_OR_CORPORATION_IDS_SHOUT_BE_FULL);
        }
        Set<UUID> businessIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(search.getBusinessIds())) {
            businessIds.addAll(search.getBusinessIds());
        }
        if (CollectionUtils.isNotEmpty(search.getCorporationIds())) {
            businessIds.addAll(businessService.getIdsByCorporationIds(search.getCorporationIds()));
        }
        search.setBusinessIds(new ArrayList<>(businessIds));
        businessIds.forEach(f -> {
            if (!businessService.currentUserHavePermissionToActionInBusinessLikeOwner(f)) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
            }
        });
    }

}
