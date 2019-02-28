package com.gliesereum.proxy.service.keeper.impl;

import com.gliesereum.proxy.config.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.permission.GroupService;
import com.gliesereum.proxy.service.exchange.permission.GroupUserService;
import com.gliesereum.proxy.service.keeper.EndpointKeeperService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.model.dto.permission.uri.RequestUriDto;
import com.gliesereum.share.common.util.RegexUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.GroupExceptionMessage.GROUP_NOT_ACTIVE;
import static com.gliesereum.share.common.exception.messages.GroupExceptionMessage.GROUP_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class EndpointKeeperServiceImpl implements EndpointKeeperService {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private GroupService groupService;

    @Override
    public void checkAccess(String currentJwt, String uri, String method) {
        if (securityProperties.getEndpointKeeperEnable()) {
            if (StringUtils.isNotEmpty(currentJwt) && StringUtils.isNotEmpty(uri)) {
                Map<String, PermissionMapValue> permissionMap = getPermissionMap(currentJwt);
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
    }

    private RequestUriDto parse(String uri) {
        String apiPrefix = zuulProperties.getPrefix();
        if (uri.startsWith(apiPrefix)) {
            uri = uri.replaceFirst(apiPrefix, "");
        }
        if (!uri.matches(RegexUtil.REQUEST_URI_REGEX)) {
            throw new ClientException(NOT_VALID_URI);
        }
        int endpointIndex = StringUtils.ordinalIndexOf(uri, "/", 3);
        String modulePath = uri.substring(0, endpointIndex);
        String endpointPath = uri.substring(endpointIndex);
        return new RequestUriDto(modulePath, RegexUtil.removeUUIDToStar(endpointPath));
    }


    private Map<String, PermissionMapValue> getPermissionMap(String currentJwt) {
        GroupDto userGroup = groupUserService.getUserGroup(currentJwt);
        if (userGroup == null) {
            throw new ClientException(GROUP_NOT_FOUND);
        }
        if (!userGroup.isActive()) {
            throw new ClientException(GROUP_NOT_ACTIVE);
        }
        Map<String, PermissionMapValue> permissionMap = groupService.getPermissionMap(userGroup.getId());
        if (MapUtils.isEmpty(permissionMap)) {
            throw new ClientException(DONT_HAVE_ANY_PERMISSION);
        }
        return permissionMap;
    }
}
