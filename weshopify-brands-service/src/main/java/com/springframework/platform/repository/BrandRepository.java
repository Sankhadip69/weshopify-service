package com.springframework.platform.repository;

import com.springframework.platform.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {


}
