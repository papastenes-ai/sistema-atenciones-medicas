package com.duoc.atencionesmedicas.paciente.service;

import com.duoc.atencionesmedicas.paciente.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.paciente.model.Paciente;
import com.duoc.atencionesmedicas.paciente.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id));
    }

    public Paciente buscarPorRut(String rut) {
        return pacienteRepository.findByRut(rut)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con rut: " + rut));
    }

    public Paciente guardarPaciente(Paciente paciente) {
        try {
            return pacienteRepository.save(paciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el paciente: " + e.getMessage());
        }
    }

    public Paciente actualizarPaciente(Integer id, Paciente pacienteActualizado) {
        try {
            Paciente paciente = buscarPorId(id);

            paciente.setRut(pacienteActualizado.getRut());
            paciente.setNombre(pacienteActualizado.getNombre());
            paciente.setApellido(pacienteActualizado.getApellido());
            paciente.setEdad(pacienteActualizado.getEdad());

            return pacienteRepository.save(paciente);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el paciente: " + e.getMessage());
        }
    }

    public void eliminarPaciente(Integer id) {
        try {
            Paciente paciente = buscarPorId(id);
            pacienteRepository.delete(paciente);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el paciente: " + e.getMessage());
        }
    }
}