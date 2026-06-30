package com.duoc.atencionesmedicas.centromedico.service;

import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoRequestDTO;
import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoResponseDTO;
import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.repository.CentroMedicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CentroMedicoServiceTest {

    @Mock
    private CentroMedicoRepository centroMedicoRepository;

    @InjectMocks
    private CentroMedicoService centroMedicoService;

    @Test
    void listarCentros_deberiaRetornarListaDeCentros() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Apoquindo");
        centro.setDireccion("Av. Apoquindo 1234");
        centro.setComuna("Las Condes");
        centro.setTelefono("222222222");
        centro.setHorario("08:00 - 18:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findAll()).thenReturn(List.of(centro));

        List<CentroMedicoResponseDTO> resultado = centroMedicoService.listarCentros();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCentro());
        assertEquals("Centro Médico Apoquindo", resultado.get(0).getNombre());
        assertEquals("Av. Apoquindo 1234", resultado.get(0).getDireccion());
        assertEquals("Las Condes", resultado.get(0).getComuna());
        assertEquals("222222222", resultado.get(0).getTelefono());
        assertEquals("08:00 - 18:00", resultado.get(0).getHorario());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarCentroMedico() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Central");
        centro.setDireccion("Av. Principal 100");
        centro.setComuna("Santiago");
        centro.setTelefono("233333333");
        centro.setHorario("09:00 - 17:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centro));

        CentroMedicoResponseDTO resultado = centroMedicoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Centro Médico Central", resultado.getNombre());
        assertEquals("Av. Principal 100", resultado.getDireccion());
        assertEquals("Santiago", resultado.getComuna());
        assertEquals("233333333", resultado.getTelefono());
        assertEquals("09:00 - 17:00", resultado.getHorario());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).findById(1);
    }

    @Test
    void guardarCentro_deberiaGuardarYRetornarCentro() {
        CentroMedicoRequestDTO dto = new CentroMedicoRequestDTO();
        dto.setNombre("Centro Médico Norte");
        dto.setDireccion("Calle Norte 123");
        dto.setComuna("Huechuraba");
        dto.setTelefono("244444444");
        dto.setHorario("08:30 - 18:30");
        dto.setEstado("ACTIVO");

        CentroMedico centroGuardado = new CentroMedico();
        centroGuardado.setIdCentro(1);
        centroGuardado.setNombre(dto.getNombre());
        centroGuardado.setDireccion(dto.getDireccion());
        centroGuardado.setComuna(dto.getComuna());
        centroGuardado.setTelefono(dto.getTelefono());
        centroGuardado.setHorario(dto.getHorario());
        centroGuardado.setEstado(dto.getEstado());

        when(centroMedicoRepository.save(any(CentroMedico.class))).thenReturn(centroGuardado);

        CentroMedicoResponseDTO resultado = centroMedicoService.guardarCentro(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Centro Médico Norte", resultado.getNombre());
        assertEquals("Calle Norte 123", resultado.getDireccion());
        assertEquals("Huechuraba", resultado.getComuna());
        assertEquals("244444444", resultado.getTelefono());
        assertEquals("08:30 - 18:30", resultado.getHorario());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).save(any(CentroMedico.class));
    }

    @Test
    void actualizarCentro_deberiaActualizarYRetornarCentro() {
        CentroMedico centroExistente = new CentroMedico();
        centroExistente.setIdCentro(1);
        centroExistente.setNombre("Centro Antiguo");
        centroExistente.setDireccion("Dirección Antigua");
        centroExistente.setComuna("Santiago");
        centroExistente.setTelefono("111111111");
        centroExistente.setHorario("08:00 - 17:00");
        centroExistente.setEstado("ACTIVO");

        CentroMedicoRequestDTO dto = new CentroMedicoRequestDTO();
        dto.setNombre("Centro Actualizado");
        dto.setDireccion("Dirección Nueva");
        dto.setComuna("Providencia");
        dto.setTelefono("222222222");
        dto.setHorario("09:00 - 18:00");
        dto.setEstado("ACTIVO");

        CentroMedico centroActualizado = new CentroMedico();
        centroActualizado.setIdCentro(1);
        centroActualizado.setNombre(dto.getNombre());
        centroActualizado.setDireccion(dto.getDireccion());
        centroActualizado.setComuna(dto.getComuna());
        centroActualizado.setTelefono(dto.getTelefono());
        centroActualizado.setHorario(dto.getHorario());
        centroActualizado.setEstado(dto.getEstado());

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centroExistente));
        when(centroMedicoRepository.save(any(CentroMedico.class))).thenReturn(centroActualizado);

        CentroMedicoResponseDTO resultado = centroMedicoService.actualizarCentro(1, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Centro Actualizado", resultado.getNombre());
        assertEquals("Dirección Nueva", resultado.getDireccion());
        assertEquals("Providencia", resultado.getComuna());
        assertEquals("222222222", resultado.getTelefono());
        assertEquals("09:00 - 18:00", resultado.getHorario());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).findById(1);
        verify(centroMedicoRepository, times(1)).save(any(CentroMedico.class));
    }

    @Test
    void buscarPorComuna_deberiaRetornarCentrosFiltrados() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Oriente");
        centro.setDireccion("Av. Oriente 456");
        centro.setComuna("Providencia");
        centro.setTelefono("255555555");
        centro.setHorario("08:00 - 18:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findByComuna("Providencia")).thenReturn(List.of(centro));

        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorComuna("Providencia");

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCentro());
        assertEquals("Centro Médico Oriente", resultado.get(0).getNombre());
        assertEquals("Providencia", resultado.get(0).getComuna());

        verify(centroMedicoRepository, times(1)).findByComuna("Providencia");
    }

    @Test
    void buscarPorEstado_deberiaRetornarCentrosFiltrados() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Sur");
        centro.setDireccion("Av. Sur 789");
        centro.setComuna("La Florida");
        centro.setTelefono("266666666");
        centro.setHorario("09:00 - 19:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findByEstado("ACTIVO")).thenReturn(List.of(centro));

        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorEstado("ACTIVO");

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCentro());
        assertEquals("Centro Médico Sur", resultado.get(0).getNombre());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1)).findByEstado("ACTIVO");
    }

    @Test
    void buscarPorNombre_deberiaRetornarCentrosFiltrados() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Apoquindo");
        centro.setDireccion("Av. Apoquindo 1234");
        centro.setComuna("Las Condes");
        centro.setTelefono("222222222");
        centro.setHorario("08:00 - 18:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findByNombreContainingIgnoreCase("Apoquindo"))
                .thenReturn(List.of(centro));

        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorNombre("Apoquindo");

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCentro());
        assertEquals("Centro Médico Apoquindo", resultado.get(0).getNombre());
        assertEquals("Las Condes", resultado.get(0).getComuna());

        verify(centroMedicoRepository, times(1))
                .findByNombreContainingIgnoreCase("Apoquindo");
    }

    @Test
    void eliminarCentro_deberiaEliminarCentroCuandoExiste() {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(1);
        centro.setNombre("Centro Médico Central");
        centro.setDireccion("Av. Principal 100");
        centro.setComuna("Santiago");
        centro.setTelefono("233333333");
        centro.setHorario("09:00 - 17:00");
        centro.setEstado("ACTIVO");

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centro));

        centroMedicoService.eliminarCentro(1);

        verify(centroMedicoRepository, times(1)).findById(1);
        verify(centroMedicoRepository, times(1)).delete(centro);
    }
}