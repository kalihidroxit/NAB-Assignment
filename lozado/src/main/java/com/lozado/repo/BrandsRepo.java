package com.lozado.repo;

import com.lozado.entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsRepo extends JpaRepository<Brands, Long> {
}
