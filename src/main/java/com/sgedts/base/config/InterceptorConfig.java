package com.sgedts.base.config;

import com.sgedts.base.config.interceptor.LogInterceptor;
import com.sgedts.base.config.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    public final RequestInterceptor requestInterceptor;
    public final LogInterceptor logInterceptor;

    @Autowired
    public InterceptorConfig(RequestInterceptor requestInterceptor, LogInterceptor logInterceptor) {
        this.requestInterceptor = requestInterceptor;
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
        registry.addInterceptor(logInterceptor);
    }
}
