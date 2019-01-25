package com.gliesereum.permission.controller.group;

import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public List<GroupDto> getAll() {
        return groupService.getAll();
    }

    @GetMapping("/{id}")
    public GroupDto getById(@PathVariable("id") UUID id) {
        return groupService.getById(id);
    }

    @PostMapping
    public GroupDto create(@Valid @RequestBody GroupDto endpoint) {
        return groupService.create(endpoint);
    }

    @PutMapping
    public GroupDto update(@Valid @RequestBody GroupDto endpoint) {
        return groupService.update(endpoint);
    }

    @DeleteMapping
    public MapResponse delete(@RequestParam("id") UUID id) {
        groupService.delete(id);
        return new MapResponse("success");
    }

    @GetMapping("/permission-map")
    public Map<String, PermissionMapValue> getPermissionMap(@RequestParam("groupId") UUID groupId) {
        return groupService.getPermissionMap(groupId);
    }
}
