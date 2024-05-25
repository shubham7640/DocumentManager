package com.springReact.DocumentManager;

import com.springReact.DocumentManager.domain.RequestContext;
import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.enumeration.Authority;
import com.springReact.DocumentManager.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing // For Audit Listener to work
@EnableAsync // For email Service to work asynchronously
public class DocumentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository)
	{
		return args -> {
			//Please create a system user in Users table for this insertion to work properly
			RequestContext.setUserId(0L);
			var userRole = new RoleEntity();
			userRole.setRoleName(Authority.USER.name());
			userRole.setAuthorites(Authority.USER);
			roleRepository.save(userRole);

			var adminRole = new RoleEntity();
			adminRole.setRoleName(Authority.ADMIN.name());
			adminRole.setAuthorites(Authority.ADMIN);
			roleRepository.save(adminRole);

			RequestContext.start();

		};
	}
}
