package com.lozado.repo;

import com.lozado.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

    List<Products> findByNameContainingIgnoreCase(String name);
}
