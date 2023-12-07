package edu.ktu.GenomeLab;

import edu.ktu.GenomeLab.authentication.AuthenticationService;
import edu.ktu.GenomeLab.authentication.RegisterRequest;
import edu.ktu.GenomeLab.models.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GenomeLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenomeLabApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service) {
		return args -> {
			var admin = RegisterRequest.builder()
					.name("admin")
					.email("admin@gmail.com")
					.password("admin")
					.role(Role.ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getToken());

			var researcher = RegisterRequest.builder()
					.name("researcher")
					.email("researcher@gmail.com")
					.password("researcher")
					.role(Role.RESEARCHER)
					.build();
			System.out.println("Researcher token: " + service.register(researcher).getToken());
		};

	}
}
