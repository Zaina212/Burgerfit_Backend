package com.burgerfit.productos.controller;

import com.burgerfit.productos.dto.LocalRequestDTO;
import com.burgerfit.productos.dto.LocalResponseDTO;
import com.burgerfit.productos.service.LocalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locales")
public class LocalController {

    private static final Logger log = LoggerFactory.getLogger(LocalController.class);

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @PostMapping
    public ResponseEntity<?> crearLocal(@Valid @RequestBody LocalRequestDTO requestDTO) {
        log.info("Solicitud recibida para crear local");

        LocalResponseDTO localCreado = localService.crearLocal(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(localCreado);
    }

    @GetMapping
    public ResponseEntity<?> listarLocales() {
        log.info("Solicitud recibida para listar locales");

        return ResponseEntity.ok(localService.listarLocales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarLocalPorId(@PathVariable Long id) {
        log.info("Solicitud recibida para buscar local con ID: {}", id);

        return ResponseEntity.ok(localService.buscarLocalPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLocal(
            @PathVariable Long id,
            @Valid @RequestBody LocalRequestDTO requestDTO
    ) {
        log.info("Solicitud recibida para actualizar local con ID: {}", id);

        return ResponseEntity.ok(localService.actualizarLocal(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLocal(@PathVariable Long id) {
        log.info("Solicitud recibida para eliminar local con ID: {}", id);

        localService.eliminarLocal(id);

        return ResponseEntity.ok("Local eliminado correctamente");
    }
}