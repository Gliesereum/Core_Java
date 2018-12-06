package com.gliesereum.permission.controller.group;

import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.permission.service.group.GroupUserService;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */

@RestController
@RequestMapping("/group-user")
public class GroupUserController {

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private GroupService groupService;

    @PostMapping
    public GroupUserDto addUserToGroup(@Valid @RequestBody GroupUserDto groupUser) {
        return groupUserService.addToGroup(groupUser);
    }

    @DeleteMapping
    public MapResponse removeFromGroup(@RequestParam("userId") UUID userId) {
        groupUserService.removeFromGroup(userId);
        return new MapResponse("success");
    }

    @GetMapping("/my-group")
    public GroupDto getGroupForCurrentUser() {
        GroupDto result = null;
        UserAuthentication authentication = SecurityUtil.getUser();
        if (authentication.isAnonymous()) {
            result = groupService.getForAnonymous();
        } else {
            groupUserService.getGroupByUser(authentication.getUser());
        }
        return result;
    }
}
