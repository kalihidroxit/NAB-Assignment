package org.smartchoice;

import org.smartchoice.entity.AppUser;
import org.smartchoice.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.Executor;

@SpringBootApplication
public class SmartChoiceApplication implements CommandLineRunner {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {

        SpringApplication.run(SmartChoiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        AppUser appUser = new AppUser();
        appUser.setUserName("mvp_user");
        appUser.setPassword(bCryptPasswordEncoder.encode("123456"));
        appUserRepo.save(appUser);
    }
}
