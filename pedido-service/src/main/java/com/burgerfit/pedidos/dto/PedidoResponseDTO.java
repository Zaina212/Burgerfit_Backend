package com.burgerfit.pedidos.dto;

import com.burgerfit.pedidos.entity.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private Long localId;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;
    private Integer total;
    private List<DetallePedidoResponseDTO> detalles;
}