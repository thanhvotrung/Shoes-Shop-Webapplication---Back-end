package com.ecom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3Config {
    @Bean
    public OpenAPI myApiDocumentatrion(){
        return new OpenAPI()
                .info(new Info()
                        .title("Test with Den")
                        .description("Swagger 3")
                        .version("@"));
    }
}
