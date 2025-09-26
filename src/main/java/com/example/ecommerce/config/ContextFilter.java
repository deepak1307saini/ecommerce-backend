package com.example.ecommerce.config;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.ecommerce.util.Constants.ROLE_NAMES;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextFilter extends OncePerRequestFilter {
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Get JWT from SecurityContext
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof Jwt jwt) {
                String keycloakId = jwt.getSubject();
                List<String> roles = jwt.getClaimAsStringList("roles");

                String userRole = isNull(roles) ? null :
                        Objects.requireNonNull(ROLE_NAMES.stream()
                                .filter(roleEnum -> roles.contains(roleEnum.name()))
                                .findFirst()
                                .orElse(null)).toString();

                // Map keycloakId to database userId
                User user = userService.getUserIdByKeycloakId(keycloakId);
                Long userId = user.getId();
                String userName = user.getUsername();
                Long tenantId = user.getTenantId();

                if (userId != null && userRole != null) {
                    Context.setContext(userId, userName, keycloakId, userRole, tenantId);
                    log.info("Context set: userId={}, keycloakId={}, userRole={}", userId, keycloakId, userRole);
                } else {
                    log.warn("Failed to set context: userId={}, keycloakId={}, userRole={}", userId, keycloakId, userRole);
                }
            }
        } catch (Exception e) {
            log.error("Error setting context", e);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear context after request to prevent memory leaks
            Context.clear();
        }
    }
}