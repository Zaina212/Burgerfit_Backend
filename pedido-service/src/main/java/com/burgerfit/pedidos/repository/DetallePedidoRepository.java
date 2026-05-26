package com.burgerfit.pedidos.repository;

import com.burgerfit.pedidos.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}