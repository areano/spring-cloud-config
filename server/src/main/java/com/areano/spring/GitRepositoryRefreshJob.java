package com.areano.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.monitor.PropertyPathEndpoint;
import org.springframework.cloud.config.server.environment.JGitEnvironmentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
public class GitRepositoryRefreshJob {

    private static final Log log = LogFactory.getLog(GitRepositoryRefreshJob.class);

    @Autowired
    private PropertyPathEndpoint endpoint;

    @Autowired(required = false)
    private JGitEnvironmentRepository jGitEnvironmentRepository;

    private String currentHead;

    @Scheduled(fixedRateString = "${app.config.server.monitor.fixedDelay:5000}")
    public void poll() {
        String head = jGitEnvironmentRepository.refresh(jGitEnvironmentRepository.getDefaultLabel());
        if (!head.equals(this.currentHead)) {
            log.info("Sending push notification for configuration updates");
            this.currentHead = head;
            this.endpoint.notifyByPath(new HttpHeaders(), Collections.singletonMap("path", jGitEnvironmentRepository.getBasedir().getAbsolutePath() + "/application"));
        }
    }

}
