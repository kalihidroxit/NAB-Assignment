package org.smartchoice.repo;

import org.smartchoice.entity.ThirdParty;
import org.smartchoice.utils.ThirdPartyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyRepo extends JpaRepository<ThirdParty, Long> {

    List<ThirdParty> findByNameIn(List<ThirdPartyType> names);
}
