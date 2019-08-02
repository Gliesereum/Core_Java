package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private CommentService commentService;

    @PostMapping
    public WorkerDto createWorker(@RequestBody @Valid WorkerDto worker) {
        return workerService.create(worker);
    }

    @PutMapping
    public WorkerDto updateWorker(@RequestBody @Valid WorkerDto worker) {
        return workerService.update(worker);
    }

    @DeleteMapping("/{id}")
    public MapResponse deleteWorker(@PathVariable("id") UUID id) {
        workerService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/by-corporation")
    public List<WorkerDto> getByCorporationId(@RequestParam("corporationId") UUID corporationId) {
        return workerService.getByCorporationId(corporationId);
    }

    @GetMapping("/by-business")
    public List<WorkerDto> getByBusinessId(@RequestParam("businessId") UUID businessId) {
        return workerService.getByBusinessId(businessId, true);
    }

    @GetMapping("/by-workingSpace")
    public List<WorkerDto> getAllWorker(@RequestParam("workingSpaceId") UUID workingSpaceId) {
        return workerService.getByWorkingSpaceId(workingSpaceId);
    }

    @PostMapping("/create-with-user")
    public WorkerDto createWorkerWithUser(@RequestBody @Valid WorkerDto worker) {
        return workerService.createWorkerWithUser(worker);
    }

    @GetMapping("/exist/byPhone")
    public MapResponse checkWorkerExistByPhone(@RequestParam("phone") String phone) {
        Boolean exist = workerService.checkWorkerExistByPhone(phone);
        return new MapResponse("exist", exist);
    }

    @GetMapping("/{id}/rating")
    public RatingDto getRating(@PathVariable("id") UUID id) {
        return commentService.getRating(id);
    }

    @GetMapping("/{id}/comment")
    public List<CommentFullDto> getCommentByWorker(@PathVariable("id") UUID id) {
        return commentService.findFullByObjectId(id);
    }

    @GetMapping("/{id}/comment/current-user")
    public CommentFullDto getCommentByWorkerForUser(@PathVariable("id") UUID id) {
        return commentService.findByObjectIdForCurrentUser(id);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@PathVariable("id") UUID id,
                                 @RequestBody @Valid CommentDto comment) {
        SecurityUtil.checkUserByBanStatus();
        return workerService.addComment(id, SecurityUtil.getUserId(), comment);
    }

    @PutMapping("/comment")
    public CommentDto updateComment(@RequestBody @Valid CommentDto comment) {
        SecurityUtil.checkUserByBanStatus();
        return workerService.updateComment(SecurityUtil.getUserId(), comment);
    }

    @DeleteMapping("/comment/{commentId}")
    public MapResponse deleteComment(@PathVariable("commentId") UUID commentId) {
        SecurityUtil.checkUserByBanStatus();
        workerService.deleteComment(commentId, SecurityUtil.getUserId());
        return new MapResponse("true");
    }
}
