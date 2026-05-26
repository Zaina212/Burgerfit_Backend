package com.burgerfit.productos.service;

import com.burgerfit.productos.dto.ProductoRequestDTO;
import com.burgerfit.productos.dto.ProductoResponseDTO;
import com.burgerfit.productos.entity.Local;
import com.burgerfit.productos.entity.Producto;
import com.burgerfit.productos.exception.ResourceNotFoundException;
import com.burgerfit.productos.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.burgerfit.productos.dto.DescontarStockDTO;
import com.burgerfit.productos.exception.BadRequestException;
import com.burgerfit.productos.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final LocalService localService;

    public ProductoService(ProductoRepository productoRepository, LocalService localService) {
        this.productoRepository = productoRepository;
        this.localService = localService;
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        log.info("Iniciando creación de producto: {}", requestDTO.getNombre());

        Local local = localService.buscarEntidadLocalPorId(requestDTO.getLocalId());

        Producto producto = new Producto();
        producto.setNombre(requestDTO.getNombre());
        producto.setDescripcion(requestDTO.getDescripcion());
        producto.setPrecio(requestDTO.getPrecio());
        producto.setStock(requestDTO.getStock());
        producto.setCategoria(requestDTO.getCategoria());
        producto.setDisponible(true);
        producto.setLocal(local);

        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto creado correctamente con ID: {}", productoGuardado.getId());

        return convertirAResponseDTO(productoGuardado);
    }

    public List<ProductoResponseDTO> listarProductos() {
        log.info("Listando todos los productos");

        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> listarProductosDisponibles() {
        log.info("Listando productos disponibles");

        return productoRepository.findByDisponibleTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> listarProductosPorLocal(Long localId) {
        log.info("Listando productos del local ID: {}", localId);

        return productoRepository.findByLocalId(localId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO buscarProductoPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        return convertirAResponseDTO(producto);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        Local local = localService.buscarEntidadLocalPorId(requestDTO.getLocalId());

        producto.setNombre(requestDTO.getNombre());
        producto.setDescripcion(requestDTO.getDescripcion());
        producto.setPrecio(requestDTO.getPrecio());
        producto.setStock(requestDTO.getStock());
        producto.setCategoria(requestDTO.getCategoria());
        producto.setLocal(local);

        Producto productoActualizado = productoRepository.save(producto);

        log.info("Producto actualizado correctamente con ID: {}", productoActualizado.getId());

        return convertirAResponseDTO(productoActualizado);
    }

    public void eliminarProducto(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        productoRepository.delete(producto);

        log.info("Producto eliminado correctamente con ID: {}", id);
    }

    public ProductoResponseDTO cambiarDisponibilidad(Long id, Boolean disponible) {
        log.info("Cambiando disponibilidad del producto ID: {} a {}", id, disponible);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        producto.setDisponible(disponible);

        Producto productoActualizado = productoRepository.save(producto);

        log.info("Disponibilidad actualizada para producto ID: {}", productoActualizado.getId());

        return convertirAResponseDTO(productoActualizado);
    }

    public ProductoResponseDTO descontarStock(Long id, DescontarStockDTO requestDTO) {
        log.info("Descontando stock del producto ID: {}. Cantidad: {}", id, requestDTO.getCantidad());

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        if (!producto.getDisponible()) {
            log.warn("No se puede descontar stock. Producto no disponible. ID: {}", id);
            throw new BadRequestException("El producto no está disponible");
        }

        if (producto.getStock() < requestDTO.getCantidad()) {
            log.warn(
                    "Stock insuficiente para producto ID {}. Stock actual: {}, cantidad solicitada: {}",
                    id,
                    producto.getStock(),
                    requestDTO.getCantidad()
            );
            throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - requestDTO.getCantidad());

        if (producto.getStock() == 0) {
            producto.setDisponible(false);
            log.info("Producto ID {} quedó sin stock y fue marcado como no disponible", id);
        }

        Producto productoActualizado = productoRepository.save(producto);

        log.info("Stock descontado correctamente. Producto ID: {}, stock actual: {}", id, productoActualizado.getStock());

        return convertirAResponseDTO(productoActualizado);
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getDisponible(),
                producto.getLocal().getId(),
                producto.getLocal().getNombre()
        );
    }
}