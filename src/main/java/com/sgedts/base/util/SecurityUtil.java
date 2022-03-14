package com.sgedts.base.util;

import com.sgedts.base.constant.JwtConstant;
import com.sgedts.base.constant.RoleConstant;
import com.sgedts.base.constant.ScopeConstant;
import com.sgedts.base.constant.enums.MessageResourceEnum;
import com.sgedts.base.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collections of security utility functions.
 */
public final class SecurityUtil {

    public static Optional<String> getRoleCategory() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString(JwtConstant.ROLE_CATEGORY));
    }

    /**
     * Check if mobile user.
     *
     * @return is user mobile
     */
    public static boolean isMobile() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString(JwtConstant.SCOPE))
                .stream().anyMatch(s -> s.contains(ScopeConstant.MOBILE));
    }

    /**
     * Get user permissions
     *
     * @return Optional of list permissions
     */
    public static Optional<List<String>> getListPermissions() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsStringList(JwtConstant.PERMISSIONS));
    }

    /**
     * Get user mobile id
     *
     * @return Optional of user mobile id
     */
    public static Optional<String> getUserMobileId() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString(JwtConstant.CUSTOMER_ID));
    }

    /**
     * Get username from current user.
     *
     * @return username
     */
    public static Optional<String> getUsername() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString(JwtConstant.USERNAME));
    }

    /**
     * @return true if jwt token is anonymous role
     */
    public static boolean isAnonymous() {
        return (getRoles().contains(RoleConstant.ANONYMOUS));
    }

    /**
     * Get roles from current user.
     *
     * @return roles
     */
    public static List<String> getRoles() {
        return Stream.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static Optional<String> getSessionId() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString(JwtConstant.SESSION_STATE));
    }

    public static String getToken() {
        return getJwt().orElseThrow().getTokenValue();
    }

    /**
     * @return Optional of JWT token
     */
    private static Optional<Jwt> getJwt() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(JwtAuthenticationToken::getToken);
    }
}
