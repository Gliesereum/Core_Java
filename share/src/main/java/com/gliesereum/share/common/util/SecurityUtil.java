package com.gliesereum.share.common.util;

import com.gliesereum.share.common.security.model.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

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
}
