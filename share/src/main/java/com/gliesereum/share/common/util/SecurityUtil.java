package com.gliesereum.share.common.util;

import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 18/10/2018
 */
public class SecurityUtil {

    public static UserAuthentication getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (UserAuthentication) authentication;
    }

    public static UUID getUserId() {
        UUID result = null;
        UserAuthentication user = getUser();
        if ((user != null) && (!user.isAnonymous())) {
            result = user.getUser().getId();
        }
        return result;
    }

    public static List<UUID> getUserCorporationIds() {
        List<UUID> result = null;
        UserAuthentication user = getUser();
        if ((user != null) && (user.getUser() != null) && (CollectionUtils.isNotEmpty(user.getUser().getCorporation()))) {
            result = user.getUser().getCorporation().stream().map(CorporationDto::getId).collect(Collectors.toList());
        }
        return result;
    }

    public static boolean userHavePermissionToCorporation(UUID corporationId) {
        boolean result = false;
        List<UUID> userCorporationIds = getUserCorporationIds();
        if (CollectionUtils.isNotEmpty(userCorporationIds) && userCorporationIds.contains(corporationId)) {
            result = true;
        }
        return result;
    }

    public static boolean isAnonymous() {
        UserAuthentication user = getUser();
        return (user == null) || user.isAnonymous();
    }

    public static String getJwtToken() {
        UserAuthentication user = getUser();
        return (user != null) ? user.getJwtToken() : null;
    }
}
