package com.burgerfit.pedidos.service;

import com.burgerfit.pedidos.client.ClienteClient;
import com.burgerfit.pedidos.client.ProductoClient;
import com.burgerfit.pedidos.dto.*;
import com.burgerfit.pedidos.entity.DetallePedido;
import com.burgerfit.pedidos.entity.EstadoPedido;
import com.burgerfit.pedidos.entity.Pedido;
import com.burgerfit.pedidos.exception.BadRequestException;
import com.burgerfit.pedidos.exception.ResourceNotFoundException;
import com.burgerfit.pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final ClienteClient clienteClient;
    private final ProductoClient productoClient;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteClient clienteClient,
            ProductoClient productoClient
    ) {
        this.pedidoRepository = pedidoRepository;
        this.clienteClient = clienteClient;
        this.productoClient = productoClient;
    }

    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {
        log.info("Iniciando creación de pedido para cliente ID: {}", requestDTO.getClienteId());

        ClienteDTO cliente = clienteClient.obtenerClientePorId(requestDTO.getClienteId());

        if (cliente == null || cliente.getId() == null) {
            log.warn("Cliente inválido o no encontrado. ID: {}", requestDTO.getClienteId());
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + requestDTO.getClienteId());
        }

        if (cliente.getActivo() != null && !cliente.getActivo()) {
            log.warn("Cliente inactivo. ID: {}", requestDTO.getClienteId());
            throw new BadRequestException("El cliente se encuentra inactivo");
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(requestDTO.getClienteId());
        pedido.setLocalId(requestDTO.getLocalId());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        List<DetallePedido> detalles = new ArrayList<>();
        int total = 0;

        for (DetallePedidoRequestDTO detalleRequest : requestDTO.getProductos()) {
            log.info("Validando producto ID: {}", detalleRequest.getProductoId());

            ProductoDTO producto = productoClient.obtenerProductoPorId(detalleRequest.getProductoId());

            if (producto == null || producto.getId() == null) {
                log.warn("Producto inválido o no encontrado. ID: {}", detalleRequest.getProductoId());
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleRequest.getProductoId());
            }

            if (producto.getDisponible() != null && !producto.getDisponible()) {
                log.warn("Producto no disponible. ID: {}", producto.getId());
                throw new BadRequestException("El producto no está disponible: " + producto.getNombre());
            }

            if (!producto.getLocalId().equals(requestDTO.getLocalId())) {
                log.warn(
                        "Producto ID {} no pertenece al local ID {}",
                        producto.getId(),
                        requestDTO.getLocalId()
                );
                throw new BadRequestException(
                        "El producto " + producto.getNombre() + " no pertenece al local indicado"
                );
            }

            if (producto.getStock() < detalleRequest.getCantidad()) {
                log.warn(
                        "Stock insuficiente para producto ID {}. Stock actual: {}, cantidad solicitada: {}",
                        producto.getId(),
                        producto.getStock(),
                        detalleRequest.getCantidad()
                );
                throw new BadRequestException(
                        "Stock insuficiente para el producto: " + producto.getNombre()
                );
            }

            int subtotal = producto.getPrecio() * detalleRequest.getCantidad();

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProductoId(producto.getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
            total += subtotal;
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        log.info("Pedido creado correctamente con ID: {}", pedidoGuardado.getId());

        for (DetallePedido detalle : pedidoGuardado.getDetalles()) {
            log.info(
                    "Descontando stock del producto ID: {} por cantidad: {}",
                    detalle.getProductoId(),
                    detalle.getCantidad()
            );

            productoClient.descontarStock(detalle.getProductoId(), detalle.getCantidad());
        }

        log.info("Stock descontado correctamente para el pedido ID: {}", pedidoGuardado.getId());

        return convertirAResponseDTO(pedidoGuardado);
    }

    public List<PedidoResponseDTO> listarPedidos() {
        log.info("Listando todos los pedidos");

        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        log.info("Buscando pedido con ID: {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        return convertirAResponseDTO(pedido);
    }

    public List<PedidoResponseDTO> listarPedidosPorCliente(Long clienteId) {
        log.info("Listando pedidos del cliente ID: {}", clienteId);

        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> listarPedidosPorLocal(Long localId) {
        log.info("Listando pedidos del local ID: {}", localId);

        return pedidoRepository.findByLocalId(localId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO actualizarEstado(Long id, ActualizarEstadoPedidoDTO requestDTO) {
        log.info("Actualizando estado del pedido ID: {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar estado. Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        pedido.setEstado(requestDTO.getEstado());

        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        log.info(
                "Estado del pedido ID {} actualizado a {}",
                pedidoActualizado.getId(),
                pedidoActualizado.getEstado()
        );

        return convertirAResponseDTO(pedidoActualizado);
    }

    public void eliminarPedido(Long id) {
        log.info("Eliminando pedido con ID: {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        pedidoRepository.delete(pedido);

        log.info("Pedido eliminado correctamente con ID: {}", id);
    }

    private PedidoResponseDTO convertirAResponseDTO(Pedido pedido) {
        List<DetallePedidoResponseDTO> detallesDTO = pedido.getDetalles()
                .stream()
                .map(detalle -> new DetallePedidoResponseDTO(
                        detalle.getId(),
                        detalle.getProductoId(),
                        detalle.getNombreProducto(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getLocalId(),
                pedido.getFechaPedido(),
                pedido.getEstado(),
                pedido.getTotal(),
                detallesDTO
        );
    }
}