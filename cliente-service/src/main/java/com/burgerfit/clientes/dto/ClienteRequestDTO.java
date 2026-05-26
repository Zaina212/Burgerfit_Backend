package com.burgerfit.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class ClienteRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 ctrs.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80, message = "El apellido no puede superar los 80 ctrs")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 120, message = "El correo no puede superar los 120 ctrs")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 ctrs")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no puede superar los 150 ctrs")
    private String direccion;
}
