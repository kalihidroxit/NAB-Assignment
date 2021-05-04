package org.smartchoice.repo;

import org.smartchoice.entity.TokenStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenStoreRepo extends JpaRepository<TokenStore, String> {
    Optional<TokenStore> findByTokenAndIsExpFalse(String token);

    Optional<TokenStore> findByUser(String user);
}
