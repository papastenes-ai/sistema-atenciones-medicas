package com.duoc.atencionesmedicas.centromedico.service;

import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoRequestDTO;
import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoResponseDTO;
import com.duoc.atencionesmedicas.centromedico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.repository.CentroMedicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentroMedicoService {

    private final CentroMedicoRepository centroMedicoRepository;

    private CentroMedicoResponseDTO mapToResponseDTO(CentroMedico centro) {
        return new CentroMedicoResponseDTO(
                centro.getIdCentro(),
                centro.getNombre(),
                centro.getDireccion(),
                centro.getComuna(),
                centro.getTelefono(),
                centro.getHorario(),
                centro.getEstado()
        );
    }

    private CentroMedico buscarEntidadPorId(Integer id) {
        return centroMedicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Centro médico no encontrado con id: " + id));
    }

    public List<CentroMedicoResponseDTO> listarCentros() {
        return centroMedicoRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CentroMedicoResponseDTO buscarPorId(Integer id) {
        CentroMedico centro = buscarEntidadPorId(id);
        return mapToResponseDTO(centro);
    }

    public List<CentroMedicoResponseDTO> buscarPorComuna(String comuna) {
        List<CentroMedico> centros = centroMedicoRepository.findByComuna(comuna);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen centros médicos en la comuna: " + comuna);
        }

        return centros.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CentroMedicoResponseDTO> buscarPorEstado(String estado) {
        List<CentroMedico> centros = centroMedicoRepository.findByEstado(estado);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen centros médicos con estado: " + estado);
        }

        return centros.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CentroMedicoResponseDTO> buscarPorNombre(String nombre) {
        List<CentroMedico> centros =
                centroMedicoRepository.findByNombreContainingIgnoreCase(nombre);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen centros médicos con nombre: " + nombre);
        }

        return centros.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CentroMedicoResponseDTO guardarCentro(CentroMedicoRequestDTO dto) {
        CentroMedico centro = new CentroMedico(
                null,
                dto.getNombre(),
                dto.getDireccion(),
                dto.getComuna(),
                dto.getTelefono(),
                dto.getHorario(),
                dto.getEstado()
        );

        CentroMedico centroGuardado = centroMedicoRepository.save(centro);

        log.info("Centro médico guardado correctamente con id: {}",
                centroGuardado.getIdCentro());

        return mapToResponseDTO(centroGuardado);
    }

    public CentroMedicoResponseDTO actualizarCentro(Integer id, CentroMedicoRequestDTO dto) {
        CentroMedico centro = buscarEntidadPorId(id);

        centro.setNombre(dto.getNombre());
        centro.setDireccion(dto.getDireccion());
        centro.setComuna(dto.getComuna());
        centro.setTelefono(dto.getTelefono());
        centro.setHorario(dto.getHorario());
        centro.setEstado(dto.getEstado());

        CentroMedico centroActualizado = centroMedicoRepository.save(centro);

        log.info("Centro médico id {} actualizado correctamente.", id);

        return mapToResponseDTO(centroActualizado);
    }

    public void eliminarCentro(Integer id) {
        CentroMedico centro = buscarEntidadPorId(id);
        centroMedicoRepository.delete(centro);

        log.info("Centro médico id {} eliminado correctamente.", id);
    }
}