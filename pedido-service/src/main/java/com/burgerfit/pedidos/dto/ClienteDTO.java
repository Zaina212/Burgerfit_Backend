package com.burgerfit.pedidos.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean activo;
}