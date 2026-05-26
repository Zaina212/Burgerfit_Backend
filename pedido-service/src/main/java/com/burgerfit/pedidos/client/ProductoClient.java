package com.burgerfit.pedidos.client;

import com.burgerfit.pedidos.dto.ProductoDTO;
import com.burgerfit.pedidos.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.burgerfit.pedidos.dto.DescontarStockDTO;

import java.time.Duration;

@Component
public class ProductoClient {

    private static final Logger log = LoggerFactory.getLogger(ProductoClient.class);

    private final WebClient webClient;

    public ProductoClient(
            WebClient.Builder webClientBuilder,
            @Value("${producto.service.url}") String productoServiceUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(productoServiceUrl)
                .build();
    }

    public ProductoDTO obtenerProductoPorId(Long productoId) {
        log.info("Consultando producto-service para producto ID: {}", productoId);

        try {
            return webClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception ex) {
            log.error("Error al consultar producto-service. Producto ID: {}. Error: {}", productoId, ex.getMessage());
            throw new ResourceNotFoundException("No se pudo obtener el producto con ID: " + productoId);
        }
    }

    public ProductoDTO descontarStock(Long productoId, Integer cantidad) {
        log.info("Solicitando descuento de stock al producto-service. Producto ID: {}, cantidad: {}", productoId, cantidad);
        try {
            DescontarStockDTO requestDTO = new DescontarStockDTO(cantidad);
            return webClient.patch()
                    .uri("/api/productos/{id}/stock/descontar", productoId)
                    .bodyValue(requestDTO)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception ex) {
            log.error(
                    "Error al descontar stock en producto-service. Producto ID: {}. Error: {}",
                    productoId,
                    ex.getMessage()
            );
            throw new RuntimeException("No se pudo descontar stock del producto con ID: " + productoId);
        }
    }
}