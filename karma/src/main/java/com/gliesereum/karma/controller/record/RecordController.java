package com.gliesereum.karma.controller.record;

import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
import com.gliesereum.share.common.model.dto.karma.record.ReportFilterDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        return service.getFullModelById(id);
    }

    @PostMapping
    public BaseRecordDto create(@Valid @RequestBody BaseRecordDto dto) {
        return service.create(dto);
    }

    @PostMapping("/create/from-business")
    public BaseRecordDto createFromBusiness(@Valid @RequestBody BaseRecordDto dto) {
        return service.createFromBusiness(dto);
    }

    @PutMapping("/record/canceled")
    public BaseRecordDto canceledRecord(@RequestParam("idRecord") UUID idRecord) {
        return service.canceledRecord(idRecord);
    }

    @PutMapping("/time/record")
    public BaseRecordDto updateTimeRecord(
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("beginTime") Long beginTime) {
        return service.updateTimeRecord(idRecord, beginTime);
    }

    @PutMapping("/status/process")
    public BaseRecordDto updateStatusProcess(
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("status") StatusProcess status) {
        return service.updateStatusProgress(idRecord, status);
    }

    @PutMapping("/status/pay")
    public BaseRecordDto updateStatusPay(
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("status") StatusPay status) {
        return service.updateStatusPay(idRecord, status);
    }

    @PutMapping("/working/space")
    public BaseRecordDto updateWorkingSpace(
            @RequestParam("idRecord") UUID idRecord,
            @RequestParam("workingSpaceId") UUID workingSpaceId) {
        return service.updateWorkingSpace(idRecord, workingSpaceId);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/client/all")
    public List<BaseRecordDto> getAllByUser(@RequestParam("serviceType") ServiceType serviceType) {
        return service.getAllByUser(serviceType);
    }

    @PostMapping("/client/params")
    public List<BaseRecordDto> getByParamsForClient(@RequestBody RecordsSearchDto search) {
        return service.getByParamsForClient(search);
    }

    @PostMapping("/business/params")
    public List<BaseRecordDto> getByParamsForBusiness(@RequestBody RecordsSearchDto search) {
        return service.getByParamsForBusiness(search);
    }

    @PostMapping("/free-time")
    public BaseRecordDto getFreeTimeForRecord(@RequestBody BaseRecordDto dto) {
        return service.getFreeTimeForRecord(dto);
    }

    @GetMapping("/report")
    public void getReport(@Valid @RequestBody ReportFilterDto filter, HttpServletResponse response) {
        service.getReport(response, filter);
    }
}
