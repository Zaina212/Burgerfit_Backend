package com.burgerfit.pedidos.client;

import com.burgerfit.pedidos.dto.ClienteDTO;
import com.burgerfit.pedidos.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ClienteClient {

    private static final Logger log = LoggerFactory.getLogger(ClienteClient.class);

    private final WebClient webClient;

    public ClienteClient(
            WebClient.Builder webClientBuilder,
            @Value("${cliente.service.url}") String clienteServiceUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(clienteServiceUrl)
                .build();
    }

    public ClienteDTO obtenerClientePorId(Long clienteId) {
        log.info("Consultando cliente-service para cliente ID: {}", clienteId);

        try {
            return webClient.get()
                    .uri("/api/clientes/{id}", clienteId)
                    .retrieve()
                    .bodyToMono(ClienteDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception ex) {
            log.error("Error al consultar cliente-service. Cliente ID: {}. Error: {}", clienteId, ex.getMessage());
            throw new ResourceNotFoundException("No se pudo obtener el cliente con ID: " + clienteId);
        }
    }
}