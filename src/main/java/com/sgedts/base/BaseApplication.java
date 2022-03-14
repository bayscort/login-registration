package com.sgedts.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.Security;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class BaseApplication {

    public static void main(String[] args) {
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
        SpringApplication.run(BaseApplication.class, args);
    }

}
