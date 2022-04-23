package org.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Application {
    @Bean
    HttpTraceRepository traceRepository() {
        return new InMemoryHttpTraceRepository();
    }
    public static void main(String[] args) {
        Hooks.onOperatorDebug();

        SpringApplication.run(Application.class, args);
    }
}
