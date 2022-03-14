package com.sgedts.base.config;

import com.sgedts.base.security.KeyCloakJwtAuthenticationConverter;
import com.sgedts.base.security.SecurityErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration for security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final SecurityErrorHandler securityErrorHandler;
    private final KeyCloakJwtAuthenticationConverter keyCloakJwtAuthenticationConverter;

    @Value("${cors.allowed-origins}")
    private String CORS_ALLOWED_ORIGINS;
    @Value("${cors.allowed-methods}")
    private String CORS_ALLOWED_METHODS;
    @Value("${cors.allowed-headers}")
    private String CORS_ALLOWED_HEADERS;
    @Value("${cors.exposed-headers}")
    private String CORS_EXPOSED_HEADERS;
    @Value("${cors.allow-credentials}")
    private boolean CORS_ALLOWED_CREDENTIALS;
    @Value("${cors.max-age}")
    private Long CORS_MAX_AGE;

    @Autowired
    public SecurityConfig(SecurityErrorHandler securityErrorHandler,
                          KeyCloakJwtAuthenticationConverter keyCloakJwtAuthenticationConverter) {
        this.securityErrorHandler = securityErrorHandler;
        this.keyCloakJwtAuthenticationConverter = keyCloakJwtAuthenticationConverter;
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    /**
     * Configuration for CORS security
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean processCorsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(CORS_ALLOWED_ORIGINS);
        config.addAllowedMethod(CORS_ALLOWED_METHODS);
        config.addAllowedHeader(CORS_ALLOWED_HEADERS);
        config.addExposedHeader(CORS_EXPOSED_HEADERS);
        config.setAllowCredentials(CORS_ALLOWED_CREDENTIALS);
        config.setMaxAge(CORS_MAX_AGE);
        source.registerCorsConfiguration("/**", config);

        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * Custom configuration for security
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer()
                .authenticationEntryPoint(securityErrorHandler)
                .accessDeniedHandler(securityErrorHandler)
                .jwt()
                .jwtAuthenticationConverter(keyCloakJwtAuthenticationConverter)
                .and().and()
                .csrf().disable()
                .cors()
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/open/**").permitAll()
                .antMatchers("/api/internal/**").permitAll()
                .antMatchers("/api/debug/**").permitAll()
                .antMatchers("/api/**").authenticated();
    }
}
