package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.TPUDescricaoDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.repository.TipoUsuarioRepository
import java.util.Optional
import kotlin.test.assertEquals

class TipoUsuarioServiceTest {
    private val repository = mock(TipoUsuarioRepository::class.java)
    private val service = TipoUsuarioService(repository)

    @Test
    fun `listar deve retornar todos tipos de usuário`() {
        val tipos = listOf(TipoUsuario(1, "ADMIN"),
            TipoUsuario(2, "CLIENTE"),
            TipoUsuario(3, "FUNCIONARIO"))
        `when`(repository.findAll()).thenReturn(tipos)

        val result = service.listar()

        assertEquals(tipos, result)
        verify(repository).findAll()
    }

    @Test
    fun `salvar deve salvar e mostrar o tipo do usuário`() {
        val tipoUsuario = TipoUsuario(1, "ADMIN")
        `when`(repository.save(tipoUsuario)).thenReturn(tipoUsuario)

        val result = service.salvar(tipoUsuario)

        assertEquals(tipoUsuario, result)
        verify(repository).save(tipoUsuario)
    }

    @Test
    fun `listarPorId deve retornar o tipo de usuário por id`() {
        val tipoUsuario = TipoUsuario(1, "ADMIN")
        `when`(repository.findById(1)).thenReturn(Optional.of(tipoUsuario))

        val result = service.listarPorId(1)

        assertEquals(tipoUsuario, result)
        verify(repository).findById(1)
    }

    @Test
    fun `excluir deve deletar tipo de usuário por id`() {
        `when`(repository.existsById(1)).thenReturn(true)

        service.excluir(1)

        verify(repository).deleteById(1)
    }

    @Test
    fun `atualizar deve atualizar e retornar o tipo de usuário`() {
        val tipoUsuario = TipoUsuario(1, "ADMIN")
        val descricaoDto = TPUDescricaoDto("CLIENTE")
        `when`(repository.findById(1)).thenReturn(Optional.of(tipoUsuario))
        `when`(repository.save(tipoUsuario)).thenReturn(tipoUsuario)

        val result = service.atualizar(1, descricaoDto)

        assertEquals("CLIENTE", result.descricao)
        verify(repository).save(tipoUsuario)
    }
}