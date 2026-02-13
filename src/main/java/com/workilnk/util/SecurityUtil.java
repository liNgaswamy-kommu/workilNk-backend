package com.workilnk.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getLoggedInUserId() {
        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
