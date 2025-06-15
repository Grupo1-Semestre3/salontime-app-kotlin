package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.CupomDestinadoRepository
import sptech.salonTime.repository.CupomRepository
import sptech.salonTime.repository.UsuarioRepository
import java.util.*

class CupomDestinadoServiceTest {

    private val repository = mock(CupomDestinadoRepository::class.java)
    private val cupomRepository = mock(CupomRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val service = CupomDestinadoService(repository, cupomRepository, usuarioRepository)



    @Test
    fun `salvar deve salvar um novo cupom destinado`() {
        val tipoUsuario = TipoUsuario(3, "Funcionario") // Create a TipoUsuario object
        val usuario = Usuario(1, tipoUsuario)
        val cupom = Cupom(1, "Desconto")
        val cupomDestinado = CupomDestinado(1, cupom, usuario, false)

        `when`(cupomRepository.findById(1)).thenReturn(Optional.of(cupom))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(repository.save(cupomDestinado)).thenReturn(cupomDestinado)

        val result = service.salvar(cupomDestinado)

        assertEquals(cupomDestinado.cupom, result.cupom)
        verify(repository).save(cupomDestinado)
    }

    @Test
    fun `editar deve atualizar um cupom destinado existente`() {
        val cupomUsado = Cupom(1, "Desconto Usado")
        val usuarioCupom = Usuario(1, TipoUsuario(3, "Funcionario"))
        val cupomDestinadoExistente = CupomDestinado(1, cupomUsado, usuarioCupom, false)

        `when`(repository.findById(1)).thenReturn(Optional.of(cupomDestinadoExistente))
        `when`(repository.save(any(CupomDestinado::class.java))).thenAnswer { it.arguments[0] }

        val result = service.editar(1, CupomDestinado(1, cupomUsado, usuarioCupom, true))

        assertEquals(true, result.usado)
    }


    @Test
    fun `atualizarUsado deve marcar um cupom como usado`() {
        val cupomDestinado = CupomDestinado(1, null, null, false)

        `when`(repository.findById(1)).thenReturn(Optional.of(cupomDestinado))
        `when`(repository.save(cupomDestinado)).thenReturn(cupomDestinado.apply { usado = true })

        val result = service.atualizarUsado(1, true)

        assertTrue(result.usado ?: false)
    }

    @Test
    fun `deletar deve remover um cupom destinado existente`() {
        val cupomDestinado = CupomDestinado(1, null, null, false)

        `when`(repository.findById(1)).thenReturn(Optional.of(cupomDestinado))

        service.deletar(1)

        verify(repository).delete(cupomDestinado)
    }

    @Test
    fun `listar deve retornar todos os cupons destinados`() {
        val cuponsDestinados = listOf(
            CupomDestinado(1, null, null, false),
            CupomDestinado(2, null, null, true)
        )

        `when`(repository.findAll()).thenReturn(cuponsDestinados)

        val result = service.listar()

        assertEquals(cuponsDestinados.size , result.size)
        verify(repository).findAll()
    }
}