/**
 * Spring Boot entry point for the Users Demo application.
 *
 * Exposes a minimal REST API and static web UI for managing
 * in-memory demo users. See `README.md` for usage and endpoints.
 */
package com.example.usersdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersDemoApplication.class, args);
    }
}
