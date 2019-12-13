package com.areano.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Log
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Component
    @EnableScheduling
    @RequiredArgsConstructor
    class Scheduler {

        final DefaultService defaultService;

        @Scheduled(fixedDelay = 1000)
        void debugProperty() {
            defaultService.showEnvironment();
        }

    }

    @Service
    @RefreshScope
    class DefaultService {

        @Value("${info.app.environment}")
        String environment = "dev";

        void showEnvironment() {
            log.info(environment);
        }

    }
}
