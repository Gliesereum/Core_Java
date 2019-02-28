package com.gliesereum.share.common.util;

import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
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
        if ((user != null) && (user.getUser() != null) && (CollectionUtils.isNotEmpty(user.getUser().getCorporationIds()))) {
            result = user.getUser().getCorporationIds();
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

    public static void checkUserByBanStatus() {
        if (isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        UserDto user = SecurityUtil.getUser().getUser();
        if ((user.getBanStatus() != null) && user.getBanStatus().equals(BanStatus.BAN)) {
            throw new ClientException(USER_IN_BAN);
        }
    }
}
