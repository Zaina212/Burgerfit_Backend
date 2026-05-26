package com.burgerfit.productos.service;

import com.burgerfit.productos.dto.LocalRequestDTO;
import com.burgerfit.productos.dto.LocalResponseDTO;
import com.burgerfit.productos.entity.Local;
import com.burgerfit.productos.exception.ResourceNotFoundException;
import com.burgerfit.productos.repository.LocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    private static final Logger log = LoggerFactory.getLogger(LocalService.class);

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public LocalResponseDTO crearLocal(LocalRequestDTO requestDTO) {
        log.info("Iniciando creación de local: {}", requestDTO.getNombre());

        Local local = new Local();
        local.setNombre(requestDTO.getNombre());
        local.setDireccion(requestDTO.getDireccion());
        local.setComuna(requestDTO.getComuna());
        local.setTelefono(requestDTO.getTelefono());
        local.setActivo(true);

        Local localGuardado = localRepository.save(local);

        log.info("Local creado correctamente con ID: {}", localGuardado.getId());

        return convertirAResponseDTO(localGuardado);
    }

    public List<LocalResponseDTO> listarLocales() {
        log.info("Listando todos los locales");

        return localRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public LocalResponseDTO buscarLocalPorId(Long id) {
        log.info("Buscando local con ID: {}", id);

        Local local = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Local no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Local no encontrado con ID: " + id);
                });

        return convertirAResponseDTO(local);
    }

    public Local buscarEntidadLocalPorId(Long id) {
        log.info("Buscando entidad Local con ID: {}", id);

        return localRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Local no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Local no encontrado con ID: " + id);
                });
    }

    public LocalResponseDTO actualizarLocal(Long id, LocalRequestDTO requestDTO) {
        log.info("Actualizando local con ID: {}", id);

        Local local = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Local no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Local no encontrado con ID: " + id);
                });

        local.setNombre(requestDTO.getNombre());
        local.setDireccion(requestDTO.getDireccion());
        local.setComuna(requestDTO.getComuna());
        local.setTelefono(requestDTO.getTelefono());

        Local localActualizado = localRepository.save(local);

        log.info("Local actualizado correctamente con ID: {}", localActualizado.getId());

        return convertirAResponseDTO(localActualizado);
    }

    public void eliminarLocal(Long id) {
        log.info("Eliminando local con ID: {}", id);

        Local local = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Local no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Local no encontrado con ID: " + id);
                });

        localRepository.delete(local);

        log.info("Local eliminado correctamente con ID: {}", id);
    }

    private LocalResponseDTO convertirAResponseDTO(Local local) {
        return new LocalResponseDTO(
                local.getId(),
                local.getNombre(),
                local.getDireccion(),
                local.getComuna(),
                local.getTelefono(),
                local.getActivo()
        );
    }
}