package com.lozado.repo;

import com.lozado.entity.ProductComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCommentsRepo extends JpaRepository<ProductComments, Long> {
}
