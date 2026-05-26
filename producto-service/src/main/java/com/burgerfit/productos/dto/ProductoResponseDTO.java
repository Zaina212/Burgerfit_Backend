package com.burgerfit.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {

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