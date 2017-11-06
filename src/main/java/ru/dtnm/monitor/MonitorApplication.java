package ru.dtnm.monitor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Стартап и конфигурация приложения
 */
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {
        "ru.dtnm.monitor.rest.api",
        "ru.dtnm.monitor.history"
})
public class MonitorApplication {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}
}
