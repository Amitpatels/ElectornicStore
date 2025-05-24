package com.lcwd.electronic.store.ElectronicStore;

import com.lcwd.electronic.store.ElectronicStore.config.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.repositories.RoleRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role roleAdmin = roleRepository.findByName("ROLE_"+ AppConstants.ROLE_ADMIN).orElse(null);
		if(roleAdmin == null){
			Role role = new Role();
			role.setRoleId(UUID.randomUUID().toString());
			role.setName("ROLE_"+AppConstants.ROLE_ADMIN);
			roleRepository.save(role);
		}

		Role normalUser = roleRepository.findByName("ROLE_"+AppConstants.ROLE_NORMAL).orElse(null);
		if(normalUser == null){
			Role role = new Role();
			role.setRoleId(UUID.randomUUID().toString());
			role.setName("ROLE_"+AppConstants.ROLE_NORMAL);
			roleRepository.save(role);
		}

		//create a admin user:
		User user = userRepository.findByEmail("patelamit812@gmail.com").orElse(null);
		if(user == null){
			user = new User();
			user.setName("Amit");
			user.setEmail("patelamit812@gmail.com");
			user.setPassword(passwordEncoder.encode("patel"));
			user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());

			userRepository.save(user);

		}


	}
}
