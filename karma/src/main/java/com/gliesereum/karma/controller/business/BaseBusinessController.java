package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ServiceTypeFacade serviceTypeFacade;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BusinessEsService businessEsService;

    @GetMapping
    public List<BaseBusinessDto> getAll() {
        return baseBusinessService.getAll();
    }

    @PostMapping("/search")
    public List<BaseBusinessDto> search(@Valid @RequestBody(required = false) BusinessSearchDto businessSearch) {
        return businessEsService.search(businessSearch);
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
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(media.getObjectId());
        return mediaService.create(media);
    }

    @PutMapping("/media")
    public MediaDto update(@RequestBody @Valid MediaDto media) {
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(media.getObjectId());
        return mediaService.update(media);
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public MapResponse delete(@PathVariable("id") UUID businessId, @PathVariable("mediaId") UUID mediaId) {
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(businessId);
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
        return commentService.addComment(id, userId, comment);
    }

    @PutMapping("/comment")
    public CommentDto updateComment(@RequestBody @Valid CommentDto comment) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        return commentService.updateComment(userId, comment);
    }

    @DeleteMapping("/comment/{commentId}")
    public MapResponse deleteComment(@PathVariable("commentId") UUID commentId) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        commentService.deleteComment(commentId, userId);
        return new MapResponse("true");
    }

}
