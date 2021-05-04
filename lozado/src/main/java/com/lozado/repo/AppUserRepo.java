package com.lozado.repo;

import com.lozado.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByApiKey(String xApiKey);
    Optional<AppUser> findByUserName(String userName);
}
