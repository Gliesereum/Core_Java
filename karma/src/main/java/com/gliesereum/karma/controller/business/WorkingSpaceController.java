package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceServicePriceDto;
import com.gliesereum.share.common.model.response.MapResponse;
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
@RequestMapping("/working-space")
public class WorkingSpaceController {

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private WorkerService workerService;

    @GetMapping("/{businessId}")
    public List<WorkingSpaceDto> getAll(@PathVariable("businessId") UUID businessId) {
        return workingSpaceService.getByBusinessId(businessId, true);
    }

    @PostMapping
    public WorkingSpaceDto create(@RequestBody @Valid WorkingSpaceDto space) {
        return workingSpaceService.create(space);
    }

    @PostMapping("/list")
    public List<WorkingSpaceDto> createList(@RequestBody List<WorkingSpaceDto> spaces) {
        return workingSpaceService.create(spaces);
    }

    @PutMapping
    public WorkingSpaceDto update(@RequestBody @Valid WorkingSpaceDto space) {
        return workingSpaceService.update(space);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        workingSpaceService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/worker/{workingSpaceId}")
    public List<WorkerDto> getAllWorker(@PathVariable("workingSpaceId") UUID workingSpaceId) {
        return workerService.getByWorkingSpaceId(workingSpaceId);
    }

    @GetMapping("/workers")
    public List<WorkerDto> getByCorporationId(@RequestParam("corporationId") UUID corporationId) {
        return workerService.getByCorporationId(corporationId);
    }

    @PostMapping("/worker")
    public WorkerDto createWorker(@RequestBody @Valid WorkerDto worker) {
        return workerService.create(worker);
    }

    @PutMapping("/worker")
    public WorkerDto updateWorker(@RequestBody @Valid WorkerDto worker) {
        return workerService.update(worker);
    }

    @DeleteMapping("/worker/{id}")
    public MapResponse deleteWorker(@PathVariable("id") UUID id) {
        workerService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/worker/exist/byPhone")
    public MapResponse checkWorkerExistByPhone(@RequestParam("phone") String phone) {
        Boolean exist = workerService.checkWorkerExistByPhone(phone);
        return new MapResponse("exist", exist);
    }

    @PostMapping("/add-service-price")
    public List<WorkingSpaceServicePriceDto> addServicePrice(@RequestBody List<WorkingSpaceServicePriceDto> dtos) {
        return workingSpaceService.addServicePrice(dtos);
    }
}
