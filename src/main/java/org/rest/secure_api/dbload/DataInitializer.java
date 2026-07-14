package org.rest.secure_api.dbload;

import org.rest.secure_api.model.UserEntity;
import org.rest.secure_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Tells Spring Boot to execute this FIRST
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring automatically injects required dependencies on startup
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Prevents duplicate insertion if H2 configuration changes to persistent storage
        if (userRepository.count() == 0) {
            
            // Create Admin Account
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            // Encrypts plain text string safely using BCrypt before writing to H2
            admin.setPassword(passwordEncoder.encode("admin123")); 
            userRepository.save(admin);

            // Create Regular User Account
            UserEntity user = new UserEntity();
            user.setUsername("john_doe");
            user.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(user);

            log.info(">> H2 Database users table populated with seed data successfully!");
        } else {
            log.info(">> H2 Database already contains user records. Skipping seeding step.");
        }
    }
}
