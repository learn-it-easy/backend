package com.system.learn.service;

import com.system.learn.entity.User;
import com.system.learn.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof User) {
            return ((User) principal).getId();
        } else if (principal instanceof String) {
            throw new IllegalStateException("Unable to get user ID from String principal");
        }

        throw new IllegalStateException("Unknown principal type: " + principal.getClass());
    }

}