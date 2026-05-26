package com.burgerfit.pedidos.dto;

import com.burgerfit.pedidos.entity.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoPedidoDTO {

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoPedido estado;
}