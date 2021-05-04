package org.smartchoice.repo;

import org.smartchoice.entity.ProductLookup;
import org.smartchoice.utils.ThirdPartyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLookupRepo extends JpaRepository<ProductLookup, Long> {

    Boolean existsByKeyword(String keyword);

    Optional<ProductLookup> findByProductIdAndThirdPartyName(Long id, ThirdPartyType thirdPartyType);
}
