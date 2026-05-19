package com.duoc.atencionesmedicas.medico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.medico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.medico.model.Medico;
import com.duoc.atencionesmedicas.medico.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Medico buscarPorId(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Médico no encontrado con id: " + id));
    }

    public Medico buscarPorRut(String rut) {
        return medicoRepository.findByRut(rut)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Médico no encontrado con rut: " + rut));
    }

    public List<Medico> buscarPorApellido(String apellido) {

        List<Medico> medicos = medicoRepository.findByApellido(apellido);

        if (medicos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen médicos con apellido: " + apellido);
        }

        return medicos;
    }

    public Medico guardarMedico(Medico medico) {

        try {
            return medicoRepository.save(medico);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al guardar el médico: " + e.getMessage());
        }
    }

    public Medico actualizarMedico(Integer id, Medico medicoActualizado) {

        try {

            Medico medico = buscarPorId(id);

            medico.setRut(medicoActualizado.getRut());
            medico.setNombre(medicoActualizado.getNombre());
            medico.setApellido(medicoActualizado.getApellido());
            medico.setCorreo(medicoActualizado.getCorreo());
            medico.setTelefono(medicoActualizado.getTelefono());
            medico.setEspecialidad(medicoActualizado.getEspecialidad());

            return medicoRepository.save(medico);

        } catch (RecursoNoEncontradoException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al actualizar el médico: " + e.getMessage());
        }
    }

    public void eliminarMedico(Integer id) {

        try {

            Medico medico = buscarPorId(id);

            medicoRepository.delete(medico);

        } catch (RecursoNoEncontradoException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al eliminar el médico: " + e.getMessage());
        }
    }
}