package com.ar.apartmentrent;

import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.repository.LesseeRepository;
import com.ar.apartmentrent.repository.LessorRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Apartment Rent System API", version = "1.0",
		description = "Apartment Rent System API"))
@SecurityScheme(name = "arapi", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER, description = "JWT Authorization header using Bearer scheme")
public class ApartmentrentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApartmentrentApplication.class, args);
	}
}
