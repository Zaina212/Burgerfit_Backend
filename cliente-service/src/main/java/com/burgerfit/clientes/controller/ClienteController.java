package com.burgerfit.clientes.controller;

import com.burgerfit.clientes.dto.ClienteRequestDTO;
import com.burgerfit.clientes.dto.ClienteResponseDTO;
import com.burgerfit.clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")

public class ClienteController {
    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("Solicitud recibida para crear cliente");

        ClienteResponseDTO clienteCreado = clienteService.crearCliente(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    @GetMapping
    public ResponseEntity<?> listarClientes() {
        log.info("Solicitud recibida para listar clientes");

        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        log.info("Solicitud recibida para buscar cliente con ID: {}", id);

        ClienteResponseDTO cliente = clienteService.buscarClientePorId(id);

        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO
    ) {
        log.info("Solicitud recibida para actualizar cliente con ID: {}", id);

        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, requestDTO);

        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        log.info("Solicitud recibida para eliminar cliente con ID: {}", id);

        clienteService.eliminarCliente(id);

        return ResponseEntity.ok().body("Cliente eliminado correctamente");
    }

}
