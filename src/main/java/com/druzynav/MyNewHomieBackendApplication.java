package com.druzynav;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
		info = @Info(
				title = "MyNewHomie",
				version = "0.0",
				description = "My xPI",
				license = @License(name = "Apache 2.0", url = "https://foo.bar")
		)
)
@SpringBootApplication
public class MyNewHomieBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNewHomieBackendApplication.class, args);
	}


}
