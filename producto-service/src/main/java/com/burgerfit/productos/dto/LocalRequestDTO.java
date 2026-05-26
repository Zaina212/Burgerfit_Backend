package com.burgerfit.productos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocalRequestDTO {

    @NotBlank(message = "El nombre del local es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "La dirección del local es obligatoria")
    @Size(max = 150, message = "La dirección no puede superar los 150 caracteres")
    private String direccion;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(max = 80, message = "La comuna no puede superar los 80 caracteres")
    private String comuna;

    @NotBlank(message = "El teléfono del local es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;
}