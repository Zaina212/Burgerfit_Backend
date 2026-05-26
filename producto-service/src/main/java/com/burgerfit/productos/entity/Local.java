package com.burgerfit.productos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 80)
    private String comuna;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;
}