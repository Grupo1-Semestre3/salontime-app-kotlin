package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.InfoSalao
import sptech.salonTime.exception.AtributoInvalidoAoAtualizarException
import sptech.salonTime.repository.InfoSalaoRepository
import java.util.*

class InfoSalaoServiceTest {

    private val repository = mock(InfoSalaoRepository::class.java)
    private val service = InfoSalaoService(repository)

    @Test
    fun `listar deve retornar todas as informacoes do salao`() {
        val infoSalaoList = mutableListOf(
            InfoSalao(1, "email@salao.com", "123456789", "Rua A", "123", "Cidade", "Estado", "Complemento")
        )
        `when`(repository.findAll()).thenReturn(infoSalaoList)

        val result = service.listar()

        assertEquals(infoSalaoList, result)
        verify(repository).findAll()
    }

    @Test
    fun `editar deve atualizar o email`() {
        val infoSalao = InfoSalao(1, "email@salao.com", "123456789", "Rua A", "123", "Cidade", "Estado", "Complemento")
        val novoEmail = "novoemail@salao.com"
        `when`(repository.findById(1)).thenReturn(Optional.of(infoSalao))
        `when`(repository.save(any(InfoSalao::class.java))).thenReturn(infoSalao.copy(email = novoEmail))

        val result = service.editar("email", novoEmail)

        assertEquals(novoEmail, result.email)
        verify(repository).save(any(InfoSalao::class.java))
    }

    @Test
    fun `editar deve lançar excecao quando InfoSalao nao encontrado`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.editar("email", "novoemail@salao.com")
        }

        assertEquals("InfoSalao não encontrado", exception.message)
    }
}