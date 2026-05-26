package com.burgerfit.productos.controller;

import com.burgerfit.productos.dto.ProductoRequestDTO;
import com.burgerfit.productos.dto.ProductoResponseDTO;
import com.burgerfit.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.burgerfit.productos.dto.DescontarStockDTO;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        log.info("Solicitud recibida para crear producto");

        ProductoResponseDTO productoCreado = productoService.crearProducto(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @GetMapping
    public ResponseEntity<?> listarProductos() {
        log.info("Solicitud recibida para listar productos");

        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<?> listarProductosDisponibles() {
        log.info("Solicitud recibida para listar productos disponibles");

        return ResponseEntity.ok(productoService.listarProductosDisponibles());
    }

    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarProductosPorLocal(@PathVariable Long localId) {
        log.info("Solicitud recibida para listar productos del local ID: {}", localId);

        return ResponseEntity.ok(productoService.listarProductosPorLocal(localId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Long id) {
        log.info("Solicitud recibida para buscar producto con ID: {}", id);

        return ResponseEntity.ok(productoService.buscarProductoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO
    ) {
        log.info("Solicitud recibida para actualizar producto con ID: {}", id);

        return ResponseEntity.ok(productoService.actualizarProducto(id, requestDTO));
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam Boolean disponible
    ) {
        log.info("Solicitud recibida para cambiar disponibilidad del producto ID: {}", id);

        return ResponseEntity.ok(productoService.cambiarDisponibilidad(id, disponible));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        log.info("Solicitud recibida para eliminar producto con ID: {}", id);

        productoService.eliminarProducto(id);

        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @PatchMapping("/{id}/stock/descontar")
    public ResponseEntity<?> descontarStock(
            @PathVariable Long id,
            @Valid @RequestBody DescontarStockDTO requestDTO
    ) {
        log.info("Solicitud recibida para descontar stock del producto ID: {}", id);

        return ResponseEntity.ok(productoService.descontarStock(id, requestDTO));
    }
}