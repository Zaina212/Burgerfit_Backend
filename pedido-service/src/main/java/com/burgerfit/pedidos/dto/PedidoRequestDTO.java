package com.burgerfit.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID del local es obligatorio")
    private Long localId;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<DetallePedidoRequestDTO> productos;
}