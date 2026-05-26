package com.burgerfit.productos.repository;

import com.burgerfit.productos.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
}