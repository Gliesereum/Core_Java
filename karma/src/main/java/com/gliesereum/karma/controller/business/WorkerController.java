package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
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
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

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
}
