package com.gliesereum.proxy.service.keeper.impl;

import com.gliesereum.proxy.service.exchange.permission.GroupService;
import com.gliesereum.proxy.service.exchange.permission.GroupUserService;
import com.gliesereum.proxy.service.keeper.EndpointKeeperService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.model.dto.permission.uri.RequestUriDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.share.common.util.RegexUtil;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.GROUP_NOT_ACTIVE;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.GROUP_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class EndpointKeeperServiceImpl implements EndpointKeeperService {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private GroupService groupService;

    /**
     * Everything the gateway serves is published under this prefix, and it has
     * to come off before the rest of the path can be read as module + endpoint.
     *
     * Under Zuul this read `zuul.prefix`, which was never configured, so it was
     * always the empty string while the prefix actually came from
     * `server.servlet.context-path: /api`. Nothing noticed because
     * endpointKeeperEnable is false; had it been switched on, every path would
     * have been split one segment early and rejected.
     */
    @Value("${gateway.prefix:/api}")
    private String gatewayPrefix;

    @Override
    public void checkAccess(UserAuthentication authentication, String currentJwt, String uri, String method) {
        if (Boolean.TRUE.equals(securityProperties.getEndpointKeeperEnable())
                && StringUtils.isNotEmpty(currentJwt)
                && StringUtils.isNotEmpty(uri)) {
            Map<String, PermissionMapValue> permissionMap = getPermissionMap(authentication, currentJwt);
            RequestUriDto parsedUri = parse(uri);
            PermissionMapValue module = permissionMap.get(parsedUri.getModulePath());
            if (module == null) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_MODULE);
            }
            if (!module.getModule().getIsActive()) {
                if (StringUtils.isNotEmpty(module.getModule().getInactiveMessage())) {
                    throw new ClientException(module.getModule().getInactiveMessage(), MODULE_NOT_ACTIVE.getErrorCode(), MODULE_NOT_ACTIVE.getHttpCode());
                }
                throw new ClientException(MODULE_NOT_ACTIVE);
            }
            Optional<EndpointDto> endpointOptional = module.getEndpoints().stream()
                    .filter(endpoint ->
                            endpoint.getUrl().equals(parsedUri.getEndpointPath())
                                    && endpoint.getMethod().name().equals(method))
                    .findFirst();
            EndpointDto endpoint = endpointOptional
                    .orElseThrow(() -> new ClientException(DONT_HAVE_PERMISSION_TO_ENDPOINT));
            if (!endpoint.isActive()) {
                if (StringUtils.isNotEmpty(endpoint.getInactiveMessage())) {
                    throw new ClientException(endpoint.getInactiveMessage(), ENDPOINT_NOT_ACTIVE.getErrorCode(), ENDPOINT_NOT_ACTIVE.getHttpCode());
                }
                throw new ClientException(ENDPOINT_NOT_ACTIVE);
            }
        }
    }

    private RequestUriDto parse(String uri) {
        if (StringUtils.isNotEmpty(gatewayPrefix) && uri.startsWith(gatewayPrefix)) {
            uri = uri.substring(gatewayPrefix.length());
        }
        if (!uri.matches(RegexUtil.REQUEST_URI_REGEX)) {
            throw new ClientException(NOT_VALID_URI);
        }
        int endpointIndex = StringUtils.ordinalIndexOf(uri, "/", 3);
        String modulePath = uri.substring(0, endpointIndex);
        String endpointPath = uri.substring(endpointIndex);
        return new RequestUriDto(modulePath, RegexUtil.removeUUIDToStar(endpointPath));
    }

    private Map<String, PermissionMapValue> getPermissionMap(UserAuthentication authentication, String currentJwt) {
        UUID userId = userIdOf(authentication);
        UUID userIdToCache = userId != null ? userId : SecurityUtil.getAnonymousId();
        List<GroupDto> userGroup = groupUserService.getUserGroups(userIdToCache, applicationIdOf(authentication), currentJwt);
        if (CollectionUtils.isEmpty(userGroup)) {
            throw new ClientException(GROUP_NOT_FOUND);
        }
        userGroup = userGroup.stream().filter(GroupDto::isActive).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userGroup)) {
            throw new ClientException(GROUP_NOT_ACTIVE);
        }
        List<UUID> userGroupIds = userGroup.stream().map(GroupDto::getId).collect(Collectors.toList());
        Map<String, PermissionMapValue> permissionMap = groupService.getPermissionMap(userGroupIds);
        if (MapUtils.isEmpty(permissionMap)) {
            throw new ClientException(DONT_HAVE_ANY_PERMISSION);
        }
        return permissionMap;
    }

    private UUID userIdOf(UserAuthentication authentication) {
        if (authentication == null || authentication.isAnonymous() || authentication.getUser() == null) {
            return null;
        }
        return authentication.getUser().getId();
    }

    private UUID applicationIdOf(UserAuthentication authentication) {
        if (authentication == null || authentication.getApplication() == null) {
            return null;
        }
        return authentication.getApplication().getId();
    }
}
