package com.lozado;

import com.lozado.entity.AppUser;
import com.lozado.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LozadoApplicationRunner implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(LozadoApplicationRunner.class, args);

	}

	@Override
	public void run(String... args) {
	}

}
