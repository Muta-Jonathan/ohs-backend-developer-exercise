package org.example.openAPI;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	// Define OpenAPI bean with custom info title, version and description
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Digital Health API")
						.version("1.0")
						.description("API for managing Patients, Encounters, and Observations"));
	}
}
