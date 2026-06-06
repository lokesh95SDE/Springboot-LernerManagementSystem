package com.springAi.LernerManagementSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic integration test that starts the full Spring ApplicationContext.
 *
 * {@code @SpringBootTest} boots the application in a test mode: Spring creates the
 * ApplicationContext (IoC container), applies auto-configuration, and wires beans.
 * This test ensures the context can start successfully which catches misconfigurations
 * like missing beans or invalid configuration properties.
 */
@SpringBootTest
class LernerManagementSystemApplicationTests {

    /**
     * Smoke test that verifies the Spring context loads without errors.
     * Internally this triggers the application startup lifecycle managed by Spring Boot.
     */
    @Test
    void contextLoads() {
    }

}
