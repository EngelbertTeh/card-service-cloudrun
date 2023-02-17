package vn.cloud.cardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.cloud.cardservice.config.SecretsConfig;

@EnableScheduling
@EnableConfigurationProperties(SecretsConfig.class)
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
public class FoodShareBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodShareBackendApplication.class, args);
	}

}




