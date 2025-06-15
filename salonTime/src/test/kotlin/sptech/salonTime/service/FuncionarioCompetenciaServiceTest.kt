package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.CadastroCompetenciaFuncionario
import sptech.salonTime.entidade.FuncionarioCompetencia
import sptech.salonTime.entidade.Servico
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.CompetenciaNaoEcontradaException
import sptech.salonTime.repository.FuncionarioCompetenciaRepository
import sptech.salonTime.repository.ServicoRepository
import sptech.salonTime.repository.UsuarioRepository
import java.util.*

class FuncionarioCompetenciaServiceTest {

    private val repository = mock(FuncionarioCompetenciaRepository::class.java)
    private val servicoRepository = mock(ServicoRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val service = FuncionarioCompetenciaService(repository, servicoRepository, usuarioRepository)

    @Test
    fun `salvar deve lançar excecao quando servico nao encontrado`() {
        val funcionarioCompetencia = CadastroCompetenciaFuncionario(servico = 999, funcionario = 1)
        `when`(servicoRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.salvar(funcionarioCompetencia)
        }

        assertEquals("Servico não existe", exception.message)
    }

    @Test
    fun `salvar deve lançar excecao quando funcionario nao encontrado`() {
        val funcionarioCompetencia = CadastroCompetenciaFuncionario(servico = 1, funcionario = 999)
        val servico = Servico().apply { id = 1 }
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(usuarioRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.salvar(funcionarioCompetencia)
        }

        assertEquals("Funcionario não existe", exception.message)
    }

    @Test
    fun `editar deve lançar excecao quando competencia nao encontrada`() {
        val funcionarioCompetencia = CadastroCompetenciaFuncionario(servico = 1, funcionario = 1)
        `when`(repository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.editar(999, funcionarioCompetencia)
        }

        assertEquals("Competencia funcionario nao existe", exception.message)
    }

    @Test
    fun `listarPorServico deve retornar lista vazia quando nao ha competencias`() {
        val servico = Servico().apply { id = 1 }
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(repository.findCompetenciaByServicoId(1)).thenReturn(emptyList())

        val result = service.listarPorServico(1)

        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun `listarPorServico deve lançar excecao quando servico nao encontrado`() {
        `when`(servicoRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.listarPorServico(999)
        }

        assertEquals("Competencia funcionario nao existe", exception.message)
    }
}