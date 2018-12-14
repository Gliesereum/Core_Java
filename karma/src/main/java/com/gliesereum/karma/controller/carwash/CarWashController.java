package com.gliesereum.karma.controller.carwash;

import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.ANONYMOUS_CANT_COMMENT;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.CARWASH_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@RestController
@RequestMapping("/carwash")
public class CarWashController {

    @Autowired
    private CarWashService carWashService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<CarWashDto> getAll() {
        return carWashService.getAll();
    }

    @GetMapping("/{id}")
    public CarWashDto getById(@PathVariable("id") UUID id) {
        return carWashService.getById(id);
    }

    @GetMapping("/byUser")
    public List<CarWashDto> getByUser() {
        return carWashService.getByUserBusinessId(SecurityUtil.getUserBusinessId());
    }

    @PostMapping
    public CarWashDto create(@RequestBody @Valid CarWashDto carWash) {
        return carWashService.create(carWash);
    }

    @PutMapping
    public CarWashDto update(@RequestBody @Valid CarWashDto carWash) {
        return carWashService.update(carWash);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        carWashService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/{id}/media")
    public List<MediaDto> getMediaByCarWash(@PathVariable("id") UUID id) {
        return mediaService.getByObjectId(id);
    }

    @PostMapping("/media")
    public MediaDto create(@RequestBody @Valid MediaDto media) {
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, media.getObjectId());
        return mediaService.create(media);
    }

    @PutMapping("/media")
    public MediaDto update(@RequestBody @Valid MediaDto media) {
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, media.getObjectId());
        return mediaService.update(media);
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public MapResponse delete(@PathVariable("id") UUID carWashId, @PathVariable("mediaId") UUID mediaId) {
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, carWashId);
        mediaService.delete(mediaId, carWashId);
        return new MapResponse("true");
    }

    @GetMapping("/{id}/rating")
    public RatingDto getRating(@PathVariable("id") UUID id) {
        return commentService.getRating(id);
    }

    @GetMapping("/{id}/comment")
    public List<CommentDto> getCommentByCarWash(@PathVariable("id") UUID id) {
        return commentService.findByObjectId(id);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@PathVariable("id") UUID id,
                                 @RequestBody @Valid CommentDto comment) {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(ANONYMOUS_CANT_COMMENT);
        }
        if (!carWashService.isExist(id)) {
            throw new ClientException(CARWASH_NOT_FOUND);
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
