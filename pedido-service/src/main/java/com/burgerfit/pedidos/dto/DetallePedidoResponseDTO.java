package com.burgerfit.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoResponseDTO {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Integer precioUnitario;
    private Integer subtotal;
}