package com.duoc.atencionesmedicas.centromedico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.centromedico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.repository.CentroMedicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CentroMedicoService {

    private final CentroMedicoRepository centroMedicoRepository;

    public List<CentroMedico> listarCentros() {
        return centroMedicoRepository.findAll();
    }

    public CentroMedico buscarPorId(Integer id) {
        return centroMedicoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Centro médico no encontrado con id: " + id));
    }

    public List<CentroMedico> buscarPorComuna(String comuna) {
        List<CentroMedico> centros = centroMedicoRepository.findByComuna(comuna);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen centros médicos en la comuna: " + comuna);
        }

        return centros;
    }

    public List<CentroMedico> buscarPorEstado(String estado) {
        List<CentroMedico> centros = centroMedicoRepository.findByEstado(estado);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen centros médicos con estado: " + estado);
        }

        return centros;
    }

    public List<CentroMedico> buscarPorNombre(String nombre) {
        List<CentroMedico> centros = centroMedicoRepository.findByNombreContainingIgnoreCase(nombre);

        if (centros.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen centros médicos con nombre: " + nombre);
        }

        return centros;
    }

    public CentroMedico guardarCentro(CentroMedico centroMedico) {
        try {
            return centroMedicoRepository.save(centroMedico);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el centro médico: " + e.getMessage());
        }
    }

    public CentroMedico actualizarCentro(Integer id, CentroMedico centroActualizado) {
        try {
            CentroMedico centro = buscarPorId(id);

            centro.setNombre(centroActualizado.getNombre());
            centro.setDireccion(centroActualizado.getDireccion());
            centro.setComuna(centroActualizado.getComuna());
            centro.setTelefono(centroActualizado.getTelefono());
            centro.setHorario(centroActualizado.getHorario());
            centro.setEstado(centroActualizado.getEstado());

            return centroMedicoRepository.save(centro);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el centro médico: " + e.getMessage());
        }
    }

    public void eliminarCentro(Integer id) {
        try {
            CentroMedico centro = buscarPorId(id);
            centroMedicoRepository.delete(centro);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el centro médico: " + e.getMessage());
        }
    }
}