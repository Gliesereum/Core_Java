package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.WorkTimeService;
import com.gliesereum.share.common.model.dto.karma.business.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/work-time")
public class WorkTimeController {

    @Autowired
    private WorkTimeService workTimeService;

    @GetMapping("/{businessId}")
    public List<WorkTimeDto> getAll(@PathVariable("businessId") UUID businessId) {
        return workTimeService.getByObjectId(businessId);
    }

    @PostMapping
    public WorkTimeDto create(@RequestBody @Valid WorkTimeDto workTime) {
        return workTimeService.create(workTime);
    }

    @PutMapping
    public WorkTimeDto update(@RequestBody @Valid WorkTimeDto workTime) {
        return workTimeService.update(workTime);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id, @RequestParam("serviceType") ServiceType serviceType) {
        workTimeService.delete(id, serviceType);
        return new MapResponse("true");
    }
}
