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
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
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

    @GetMapping
    public List<BaseBusinessDto> getAll() {
        return baseBusinessService.getAll();
    }

    @PostMapping("/search")
    public List<BaseBusinessDto> search(@Valid @RequestBody(required = false) BusinessSearchDto businessSearch) {
        return businessEsService.search(businessSearch);
    }

    @PostMapping("/search/document")
    public List<BusinessDocument> searchDocument(@Valid @RequestBody(required = false) BusinessSearchDto businessSearch) {
        return businessEsService.searchDocuments(businessSearch);
    }

    @GetMapping("/{id}")
    public BaseBusinessDto getById(@PathVariable("id") UUID id) {
        return baseBusinessService.getById(id);
    }

    @GetMapping("{id}/full-model")
    public BusinessFullModel getFullModelById(@PathVariable("id") UUID id) {
        return baseBusinessService.getFullModelByIds(Arrays.asList(id)).get(0);
    }

    @GetMapping("/by-user")
    public List<BaseBusinessDto> getByUser() {
        return baseBusinessService.getByCorporationIds(SecurityUtil.getUserCorporationIds());
    }

    @GetMapping("/by-corporation-id")
    public List<BaseBusinessDto> getByCorporationId(@RequestParam("corporationId") UUID corporationId) {
        return baseBusinessService.getByCorporationId(corporationId);
    }

    @PostMapping
    public BaseBusinessDto create(@RequestBody @Valid BaseBusinessDto business) {
        return baseBusinessService.create(business);
    }

    @PutMapping
    public BaseBusinessDto update(@RequestBody @Valid BaseBusinessDto business) {
        return baseBusinessService.update(business);
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

    @GetMapping("/by-current-user")
    public List<BaseBusinessDto> getAllBusinessByUser() {
        return baseBusinessService.getAllBusinessByCurrentUser();
    }

    @GetMapping("/full-model/by-current-user")
    public List<BusinessFullModel> getAllFullBusinessBy() {
        return baseBusinessService.getAllFullBusinessByCurrentUser();
    }

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

    @DeleteMapping("/comment/{commentId}")
    public MapResponse deleteComment(@PathVariable("commentId") UUID commentId) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        baseBusinessService.deleteComment(commentId, userId);
        return new MapResponse("true");
    }

    @PostMapping("/client/search")
    public MapResponse clientSearch(@RequestBody RecordsSearchDto search) {
        List<UUID> clientIds = baseBusinessService.searchClient(search);
        return new MapResponse("clientIds", clientIds);
    }

    @GetMapping("/indexing")
    public MapResponse indexing() {
        businessEsService.indexAll();
        return MapResponse.resultTrue();
    }

    @GetMapping("/customers")
    public Page<ClientDto> getCustomersByBusinessIds(@RequestParam(value = "ids") List<UUID> ids,
                                                     @RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        return clientFacade.getCustomersByBusinessIds(ids, page, size);
    }

    @GetMapping("/customers/by-corporation-id")
    public Page<ClientDto> getAllCustomersByCorporationIds(@RequestParam(value = "id") UUID id,
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                @RequestParam(value = "size", required = false) Integer size,
                                                                @RequestParam(value = "query", required = false) String query) {
        return clientFacade.getAllCustomersByCorporationId(id, page, size, query);
    }

}
