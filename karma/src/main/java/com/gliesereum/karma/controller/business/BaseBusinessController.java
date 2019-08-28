package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.document.BusinessDocument;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.business.EmptyBusinessDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.ANONYMOUS_CANT_COMMENT;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.BUSINESS_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/business")
public class BaseBusinessController {

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BusinessEsService businessEsService;

    @Autowired
    private ClientFacade clientFacade;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    //++++++++++++++ Base +++++++++++++++++++++++

    @GetMapping("/{id}")
    public BaseBusinessDto getById(@PathVariable("id") UUID id) {
        return baseBusinessService.getById(id);
    }

    @GetMapping
    public List<BaseBusinessDto> getAll() {
        return baseBusinessService.getAll();
    }

    @PostMapping
    public BaseBusinessDto create(@RequestBody @Valid BaseBusinessDto business) {
        return baseBusinessService.create(business);
    }

    @PutMapping
    public BaseBusinessDto update(@RequestBody @Valid BaseBusinessDto business) {
        return baseBusinessService.update(business);
    }

    @PutMapping("/move-geo-point")
    public BaseBusinessDto moveGroPoint(@RequestParam("businessId") UUID businessId,
                                        @RequestParam("address") String address,
                                        @RequestParam("latitude") Double latitude,
                                        @RequestParam("longitude") Double longitude,
                                        @RequestParam(value = "timeZone", required = false) Integer timeZone) {
        return baseBusinessService.moveGeoPoint(businessId, address, latitude, longitude, timeZone);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        baseBusinessService.delete(id);
        return new MapResponse("true");
    }

    @DeleteMapping("/by-corporation-id/{id}")
    public MapResponse deleteByCorporationId(@PathVariable("id") UUID id) {
        baseBusinessService.deleteByCorporationIdCheckPermission(id);
        return new MapResponse("true");
    }

    @GetMapping("/is-owner")
    public MapResponse checkCurrentUserOwner(@RequestParam("businessId") UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        return new MapResponse(businessPermissionFacade.isHavePermissionByBusiness(businessId, BusinessPermission.BUSINESS_ADMINISTRATION));
    }

    //++++++++++++++ Empty Business +++++++++++++++++++++++

    @PostMapping("/create-empty")
    public BaseBusinessDto createEmptyBusiness(@RequestBody @Valid EmptyBusinessDto business) {
        return baseBusinessService.createEmptyBusiness(business);
    }

    //++++++++++++++ Media +++++++++++++++++++++++

    @GetMapping("/{id}/media")
    public List<MediaDto> getMediaByBusiness(@PathVariable("id") UUID id) {
        return mediaService.getByObjectId(id);
    }

    @PostMapping("/media")
    public MediaDto create(@RequestBody @Valid MediaDto media) {
        businessPermissionFacade.checkPermissionByBusiness(media.getObjectId(), BusinessPermission.BUSINESS_ADMINISTRATION);
        return mediaService.create(media);
    }

    @PutMapping("/media")
    public MediaDto update(@RequestBody @Valid MediaDto media) {
        businessPermissionFacade.checkPermissionByBusiness(media.getObjectId(), BusinessPermission.BUSINESS_ADMINISTRATION);
        return mediaService.update(media);
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public MapResponse delete(@PathVariable("id") UUID businessId, @PathVariable("mediaId") UUID mediaId) {
        businessPermissionFacade.checkPermissionByBusiness(businessId, BusinessPermission.BUSINESS_ADMINISTRATION);
        mediaService.delete(mediaId, businessId);
        return new MapResponse("true");
    }

    //++++++++++++++ comment +++++++++++++++++++++++

    @GetMapping("/{id}/rating")
    public RatingDto getRating(@PathVariable("id") UUID id) {
        return commentService.getRating(id);
    }

    @GetMapping("/{id}/comment")
    public List<CommentFullDto> getCommentByBusiness(@PathVariable("id") UUID id) {
        return commentService.findFullByObjectId(id);
    }

    @GetMapping("/{id}/comment/current-user")
    public CommentFullDto getCommentByBusinessForUser(@PathVariable("id") UUID id) {
        return commentService.findByObjectIdForCurrentUser(id);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@PathVariable("id") UUID id,
                                 @RequestBody @Valid CommentDto comment) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        if (!baseBusinessService.isExist(id)) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        return baseBusinessService.addComment(id, userId, comment);
    }

    @PutMapping("/comment")
    public CommentDto updateComment(@RequestBody @Valid CommentDto comment) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        return baseBusinessService.updateComment(userId, comment);
    }

    @DeleteMapping("/comment/{id}")
    public MapResponse deleteComment(@PathVariable("id") UUID id) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        baseBusinessService.deleteComment(id, userId);
        return new MapResponse("true");
    }

    //++++++++++++++ Elastic index +++++++++++++++++++++++

    @GetMapping("/indexing")
    public MapResponse indexing() {
        businessEsService.indexAll();
        return MapResponse.resultTrue();
    }

    //++++++++++++++ Search By +++++++++++++++++++++++

    @PostMapping("/search/document")
    public List<BusinessDocument> searchDocument(@Valid @RequestBody(required = false) BusinessSearchDto businessSearch) {
        return businessEsService.searchDocuments(businessSearch);
    }

    @GetMapping("full-model-by-id")
    public BusinessFullModel getFullModelById(@RequestParam("id") UUID id) {
        return baseBusinessService.getFullModelByIds(Arrays.asList(id)).get(0);
    }

    @GetMapping("/by-current-user/like-worker/full-model")
    public List<BusinessFullModel> getAllFullBusinessBy() {
        return baseBusinessService.getAllFullBusinessByCurrentUser();
    }

    @GetMapping("/by-current-user/like-worker")
    public List<BaseBusinessDto> getAllBusinessByUser() {
        return baseBusinessService.getAllBusinessByCurrentUser();
    }

    @GetMapping("/by-current-user/like-owner")
    public List<BaseBusinessDto> getByUserLikeOwner() {
        return baseBusinessService.getByCorporationIds(SecurityUtil.getUserCorporationIds());
    }

    @GetMapping("/by-corporation-id")
    public List<BaseBusinessDto> getByCorporationId(@RequestParam("id") UUID id) {
        return baseBusinessService.getByCorporationId(id);
    }

    //+++++++++++++ Customer ++++++++++++++++++++++++++++++++++++++

    @GetMapping("/customers")
    public Page<ClientDto> getCustomersByBusinessIds(@RequestParam(value = "corporationId", required = false) UUID corporationId,
                                                     @RequestParam(value = "businessIds", required = false) List<UUID> businessIds,
                                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "100") Integer size,
                                                     @RequestParam(value = "query", required = false) String query) {
        return clientFacade.getCustomersByBusinessIdsOrCorporationId(businessIds, corporationId, page, size, query);
    }
}
