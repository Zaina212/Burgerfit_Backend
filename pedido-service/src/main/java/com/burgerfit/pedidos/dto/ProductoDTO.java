package com.burgerfit.pedidos.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private Integer stock;
    private String categoria;
    private Boolean disponible;
    private Long localId;
    private String nombreLocal;
}