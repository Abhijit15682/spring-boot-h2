package org.rest.secure_api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ClientTestRunner {

    private static final Logger log = LoggerFactory.getLogger(ClientTestRunner.class);
    private final SecureApiClient secureApiClient;

    public ClientTestRunner(SecureApiClient secureApiClient) {
        this.secureApiClient = secureApiClient;
    }

    @Bean
    @Order(2)
    public CommandLineRunner runClientVerification() {
        return args -> {
            try {
                log.info(">> REST Client: Initiating integrated user call lifecycle...");
                
                // Pass username, password, and endpoint path directly
                String data = secureApiClient.executeAuthenticatedCall(
                        "admin", 
                        "admin123", 
                        "/api/data/summary"
                );

                log.info(">> Protected Resource Data Received: {}", data);

            } catch (Exception e) {
                log.error(">> Integrated Client Execution Error: {}", e.getMessage());
            }
        };
    }
}
