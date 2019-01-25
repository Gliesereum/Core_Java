package com.gliesereum.karma.controller.common;

import com.gliesereum.karma.service.common.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.common.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.common.RecordsSearchDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private BaseRecordService service;

    @GetMapping
    public List<BaseRecordDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BaseRecordDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public BaseRecordDto create(@Valid @RequestBody BaseRecordDto dto) {
        return service.create(dto);
    }

    @PostMapping("/status/record")
    public BaseRecordDto updateStatusRecord(
            @RequestParam("isUser") Boolean isUser,
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("status") StatusRecord status) {
        return service.updateStatusRecord(idRecord, status, isUser);
    }

    @PostMapping("/time/record")
    public BaseRecordDto updateTimeRecord(
            @RequestParam("isUser") Boolean isUser,
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("beginTime") Long beginTime) {
        return service.updateTimeRecord(idRecord, beginTime, isUser);
    }

    @PostMapping("/status/process")
    public BaseRecordDto updateStatusProcess(
            @RequestParam("isUser") Boolean isUser,
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("status") StatusProcess status) {
        return service.updateStatusProgress(idRecord, status, isUser);
    }

    @PostMapping("/working/space")
    public BaseRecordDto updateWorkingSpace(
            @RequestParam("isUser") Boolean isUser,
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("workingSpaceId") UUID workingSpaceId) {
        return service.updateWorkingSpace(idRecord, workingSpaceId, isUser);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/client/all")
    public List<BaseRecordDto> getAllByUser(@RequestParam("serviceType")ServiceType serviceType) {
        return service.getAllByUser(serviceType);
    }

    @PostMapping("/client/params")
    public List<BaseRecordDto> getByParamsForClient(@RequestBody RecordsSearchDto search) {
        return service.getByParamsForClient(search);
    }

    @PostMapping("/corporation/params")
    public List<BaseRecordDto> getByParamsForCorporation(@RequestBody RecordsSearchDto search) {
        return service.getByParamsForCorporation(search);
    }

    @PostMapping("/map/params")
    public List<BaseRecordDto> getByParamsForMap(@RequestBody RecordsSearchDto search) {
        return service.getByParamsForCorporation(search);
    }

    @PostMapping("/free-time")
    public BaseRecordDto getFreeTimeForRecord(@RequestBody BaseRecordDto dto) {
        return service.getFreeTimeForRecord(dto);
    }
}
