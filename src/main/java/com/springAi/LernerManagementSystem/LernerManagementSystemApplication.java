package com.springAi.LernerManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@EnableCaching
public class LernerManagementSystemApplication {

	/**
	 * The main entry point of the Spring Boot application.
	 * <p>
	 * Internally, Java's JVM starts by loading this class and executing the {@code main} method.
	 * Spring Boot's {@code SpringApplication.run()} performs several critical steps:
	 * 1. Starts a Spring ApplicationContext (the IoC container).
	 * 2. Performs Classpath Scanning to find components (like @Service, @RestController).
	 * 3. Configures Auto-Configuration based on the dependencies in the classpath (e.g., setting up Hibernate if JPA is present).
	 * 4. Starts an embedded web server (like Tomcat) to listen for HTTP requests.
	 * </p>
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(LernerManagementSystemApplication.class, args);
	}

}
