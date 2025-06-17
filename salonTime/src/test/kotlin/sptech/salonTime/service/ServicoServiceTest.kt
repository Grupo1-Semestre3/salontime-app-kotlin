package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.repository.ServicoRepository
import java.time.LocalTime
import java.util.*

class ServicoServiceTest {
    private val repository = mock(ServicoRepository::class.java)
    private val service = ServicoService(repository)

    @Test
    @DisplayName("Deve retornar todos os serviços ativos com média de avaliação")
    fun `listarAtivos should return all active services`() {
        val servicosAtivos = listOf(
            ServicoDto(1, "Luzes", 50.0, LocalTime.of(1, 0), "ATIVO", true, "Descrição", 4.5),
            ServicoDto(2, "Corte", 30.0, LocalTime.of(1, 0), "ATIVO", false, "Descrição", 4.5)
        )
        `when`(repository.listarTodosAtivosComMedia()).thenReturn(servicosAtivos)

        val result = service.listarAtivos()

        assertEquals(servicosAtivos, result)
        verify(repository).listarTodosAtivosComMedia()
    }

    @Test
    @DisplayName("Deve retornar todos os serviços inativos com média de avaliação")
    fun `listarDesativados should return all inactive services`() {
        val servicosDesativados = listOf(
            ServicoDto(1, "Corte", 50.0, LocalTime.of(1, 0), "INATIVO", true, "Descrição", 4.5)
        )
        `when`(repository.listarDesativadosComMedia()).thenReturn(servicosDesativados)

        val result = service.listarDesativados()

        assertEquals(servicosDesativados, result)
        verify(repository).listarDesativadosComMedia()
    }

    @Test
    @DisplayName("Deve retornar um serviço pelo ID com média de avaliação")
    fun `listarPorId should return service by id`() {
        val servicoDto = ServicoDto(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true, "Descrição", 4.5)
        `when`(repository.listarPorIdComMedia(1)).thenReturn(servicoDto)

        val result = service.listarPorId(1)

        assertEquals(servicoDto, result)
        verify(repository).listarPorIdComMedia(1)
    }

    @Test
    @DisplayName("Deve desativar um serviço e alterar status para INATIVO")
    fun `desativar should deactivate a service`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.desativar(1)

        assertTrue(result)
        assertEquals("INATIVO", servico.status)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve ativar um serviço e alterar status para ATIVO")
    fun `ativar should activate a service`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "INATIVO", true)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.ativar(1)

        assertTrue(result)
        assertEquals("ATIVO", servico.status)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve criar um novo serviço e retorná-lo")
    fun `criar should save and return the service`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)
        `when`(repository.save(servico)).thenReturn(servico)

        val result = service.criar(servico)

        assertEquals(servico, result)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve atualizar um serviço existente e retorná-lo")
    fun `atualizar should update and return the service`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)
        val servicoAtualizado = Servico(1, "Corte Atualizado", 60.0, LocalTime.of(1, 30), "ATIVO", true)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))
        `when`(repository.save(servicoAtualizado)).thenReturn(servicoAtualizado)

        val result = service.atualizar(1, servicoAtualizado)

        assertEquals(servicoAtualizado, result)
        verify(repository).save(servicoAtualizado)
    }

    @Test
    @DisplayName("Deve ativar a opção simultâneo de um serviço")
    fun `ativarSimultaneo should set simultaneo to true`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", false)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.ativarSimultaneo(1)

        assertTrue(result)
        assertTrue(servico.simultaneo!!)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve desativar a opção simultâneo de um serviço")
    fun `desativarSimultaneo should set simultaneo to false`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.desativarSimultaneo(1)

        assertTrue(result)
        assertFalse(servico.simultaneo!!)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve atualizar a foto do serviço e retorná-la")
    fun `atualizarFoto should update and return the service's photo`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)
        val foto = byteArrayOf(1, 2, 3)
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.atualizarFoto(1, foto)

        assertArrayEquals(foto, result)
        verify(repository).save(servico)
    }

    @Test
    @DisplayName("Deve retornar a foto do serviço pelo ID")
    fun `getFoto should return the service's photo`() {
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true, foto = byteArrayOf(1, 2, 3))
        `when`(repository.findById(1)).thenReturn(Optional.of(servico))

        val result = service.getFoto(1)

        assertArrayEquals(servico.foto, result)
        verify(repository).findById(1)
    }
}
