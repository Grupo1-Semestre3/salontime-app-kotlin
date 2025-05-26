package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.FuncionarioCompetenciaDto
import sptech.salonTime.dto.TipoUsuarioDto
import sptech.salonTime.dto.UsuarioDto
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
    fun `listar deve retornar todas as competencias`() {
        val competencias = listOf(FuncionarioCompetencia(1, null, null))
        `when`(repository.findAll()).thenReturn(competencias)

        val result = service.listar()

        assertEquals(competencias, result)
        verify(repository).findAll()
    }

    @Test
    fun `salvar deve salvar e retornar a competencia`() {
        val servico = Servico().apply { id = 1 }
        val funcionario = Usuario().apply { id = 1 }
        val competencia = FuncionarioCompetencia(1, funcionario)

        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(funcionario))
        `when`(repository.save(competencia)).thenReturn(competencia)

        val result = service.salvar(competencia)

        assertEquals(competencia, result)
        verify(repository).save(competencia)
    }

    @Test
    fun `editar deve atualizar e retornar a competencia`() {
        val servico = Servico().apply { id = 1 }
        val funcionario = Usuario().apply { id = 1 }
        val competenciaExistente = FuncionarioCompetencia(1, funcionario)
        val competenciaAtualizada = FuncionarioCompetencia(1, funcionario)

        `when`(repository.findById(1)).thenReturn(Optional.of(competenciaExistente))
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(funcionario))
        `when`(repository.save(competenciaAtualizada)).thenReturn(competenciaAtualizada)

        val result = service.editar(1, competenciaAtualizada)

        assertEquals(competenciaAtualizada, result)
        verify(repository).save(competenciaAtualizada)
    }

    @Test
    fun `editar deve lançar excecao quando competencia nao encontrada`() {
        val competenciaAtualizada = FuncionarioCompetencia(1, null, null)
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.editar(1, competenciaAtualizada)
        }

        assertEquals("Competencia funcionario nao existe", exception.message)
    }

    @Test
    fun `deletar deve remover a competencia`() {
        val competencia = FuncionarioCompetencia(1, null, null)
        `when`(repository.findById(1)).thenReturn(Optional.of(competencia))

        service.deletar(1)

        verify(repository).delete(competencia)
    }

    @Test
    fun `deletar deve lançar excecao quando competencia nao encontrada`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.deletar(1)
        }

        assertEquals("Competencia funcionario nao existe", exception.message)
    }

    @Test
    fun `listarPorServico deve retornar competencias por servico`() {
        val servico = Servico().apply { id = 1 }
        val funcionario = Usuario().apply { id = 1 }
        val competencia = FuncionarioCompetencia(1, funcionario)
        val competencias = listOf(competencia)

        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(repository.findCompetenciaByServicoId(1)).thenReturn(competencias)

        val result = service.listarPorServico(1)

        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals(competencia.id, result?.get(0)?.id)
        assertEquals(funcionario.id, result?.get(0)?.funcionario?.id)
        verify(servicoRepository).findById(1)
        verify(repository).findCompetenciaByServicoId(1)
    }

    @Test
    fun `listarPorServico deve lançar excecao quando servico nao encontrado`() {
        `when`(servicoRepository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(CompetenciaNaoEcontradaException::class.java) {
            service.listarPorServico(1)
        }

        assertEquals("Competencia funcionario nao existe", exception.message)
    }
}