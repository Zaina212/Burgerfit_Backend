package com.burgerfit.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String comuna;
    private String telefono;
    private Boolean activo;
}