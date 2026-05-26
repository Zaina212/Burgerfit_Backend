package com.burgerfit.pedidos.controller;

import com.burgerfit.pedidos.dto.ActualizarEstadoPedidoDTO;
import com.burgerfit.pedidos.dto.PedidoRequestDTO;
import com.burgerfit.pedidos.dto.PedidoResponseDTO;
import com.burgerfit.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        log.info("Solicitud recibida para crear pedido");

        PedidoResponseDTO pedidoCreado = pedidoService.crearPedido(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
    }

    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        log.info("Solicitud recibida para listar pedidos");

        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long id) {
        log.info("Solicitud recibida para buscar pedido con ID: {}", id);

        return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> listarPedidosPorCliente(@PathVariable Long clienteId) {
        log.info("Solicitud recibida para listar pedidos del cliente ID: {}", clienteId);

        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId));
    }

    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarPedidosPorLocal(@PathVariable Long localId) {
        log.info("Solicitud recibida para listar pedidos del local ID: {}", localId);

        return ResponseEntity.ok(pedidoService.listarPedidosPorLocal(localId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPedidoDTO requestDTO
    ) {
        log.info("Solicitud recibida para actualizar estado del pedido ID: {}", id);

        return ResponseEntity.ok(pedidoService.actualizarEstado(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        log.info("Solicitud recibida para eliminar pedido con ID: {}", id);

        pedidoService.eliminarPedido(id);

        return ResponseEntity.ok("Pedido eliminado correctamente");
    }
}