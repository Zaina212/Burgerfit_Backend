package com.burgerfit.productos.repository;

import com.burgerfit.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByLocalId(Long localId);

    List<Producto> findByDisponibleTrue();
}