package com.dxvalley.epassbook;

import com.dxvalley.epassbook.enums.ERole;
import com.dxvalley.epassbook.models.Role;
import com.dxvalley.epassbook.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class EpassbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpassbookApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialize(RoleRepository repo) {
		return args -> {
			List<ERole> names = Arrays.asList(ERole.ROLE_MODERATOR,ERole.ROLE_ADMIN,ERole.ROLE_USER);

			Optional<Role> role = repo.findByName(ERole.ROLE_MODERATOR);
			if(role.isEmpty())

				names.forEach(name -> repo.save(new Role(name)));
		};
	}

}
