package com.gliesereum.karma.controller.feedback;

import com.gliesereum.karma.service.feedback.FeedBackUserService;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackUserDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/feedback")
public class FeedBackUserController {

    @Autowired
    private FeedBackUserService service;

    @PostMapping("/params")
    public List<FeedBackUserDto> getAllBySearch(@RequestBody FeedBackSearchDto search) {
        return service.getAllBySearch(search);
    }

    @PostMapping("/create/record-feedback")
    public MapResponse recordFeedback(@RequestParam("recordId") UUID recordId,
                                      @RequestParam(value = "comment", required = false) String comment,
                                      @RequestParam("mark") Integer mark) {
        service.recordFeedback(recordId, comment, mark);
        return new MapResponse("true");
    }
}    