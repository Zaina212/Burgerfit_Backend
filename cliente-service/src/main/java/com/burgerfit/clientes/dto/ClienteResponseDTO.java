package com.burgerfit.clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean activo;
}
