package com.burgerfit.clientes.service;

import com.burgerfit.clientes.exception.BadRequestException;
import com.burgerfit.clientes.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.burgerfit.clientes.dto.ClienteRequestDTO;
import com.burgerfit.clientes.dto.ClienteResponseDTO;
import com.burgerfit.clientes.entity.Cliente;
import com.burgerfit.clientes.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ClienteService {
    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO requestDTO) {
        log.info("Iniciando creación de cliente con correo: {}", requestDTO.getCorreo());

        if (clienteRepository.existsByCorreo(requestDTO.getCorreo())) {
            log.warn("Intento de crear cliente con correo ya registrado: {}", requestDTO.getCorreo());
            throw new BadRequestException("Ya existe un cliente registrado con ese correo");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(requestDTO.getNombre());
        cliente.setApellido(requestDTO.getApellido());
        cliente.setCorreo(requestDTO.getCorreo());
        cliente.setTelefono(requestDTO.getTelefono());
        cliente.setDireccion(requestDTO.getDireccion());
        cliente.setActivo(true);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        log.info("Cliente creado correctamente con ID: {}", clienteGuardado.getId());

        return convertirAResponseDTO(clienteGuardado);
    }

    public List<ClienteResponseDTO> listarClientes() {
        log.info("Listando todos los clientes");

        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarClientePorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
                });

        return convertirAResponseDTO(cliente);
    }

    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO requestDTO) {
        log.info("Actualizando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Cliente no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
                });

        clienteRepository.findByCorreo(requestDTO.getCorreo())
                .ifPresent(clienteExistente -> {
                    if (!clienteExistente.getId().equals(id)) {
                        log.warn("Correo ya usado por otro cliente: {}", requestDTO.getCorreo());
                        throw new BadRequestException("Ya existe otro cliente con ese correo");
                    }
                });

        cliente.setNombre(requestDTO.getNombre());
        cliente.setApellido(requestDTO.getApellido());
        cliente.setCorreo(requestDTO.getCorreo());
        cliente.setTelefono(requestDTO.getTelefono());
        cliente.setDireccion(requestDTO.getDireccion());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        log.info("Cliente actualizado correctamente con ID: {}", clienteActualizado.getId());

        return convertirAResponseDTO(clienteActualizado);
    }

    public void eliminarCliente(Long id) {
        log.info("Eliminando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Cliente no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
                });

        clienteRepository.delete(cliente);

        log.info("Cliente eliminado correctamente con ID: {}", id);
    }

    private ClienteResponseDTO convertirAResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getActivo()
        );
    }

}
