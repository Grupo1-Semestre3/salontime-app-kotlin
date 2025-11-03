package sptech.salonTime.service

import jakarta.validation.constraints.Null
import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.FuncionarioCompetencia
import sptech.salonTime.entidade.Servico
import sptech.salonTime.exception.CompetenciaNaoEcontradaException
import sptech.salonTime.repository.FuncionarioCompetenciaRepository
import sptech.salonTime.repository.ServicoRepository
import sptech.salonTime.repository.UsuarioRepository

@Service
class FuncionarioCompetenciaService(val repository: FuncionarioCompetenciaRepository, val servicoRepository: ServicoRepository, val usuarioRepository: UsuarioRepository) {

    fun listar(): List<FuncionarioCompetencia> {
        return repository.findAll()
    }

    fun salvar(funcionarioCompetencia: CadastroCompetenciaFuncionario): FuncionarioCompetencia {
        val servico = servicoRepository.findById(funcionarioCompetencia.servico).orElseThrow { CompetenciaNaoEcontradaException("Servico não existe") }
        val funcionario = usuarioRepository.findById(funcionarioCompetencia.funcionario).orElseThrow { CompetenciaNaoEcontradaException("Funcionario não existe") }

        val funcionarioCompetencia = FuncionarioCompetencia()
        funcionarioCompetencia.servico = servico
        funcionarioCompetencia.funcionario = funcionario

        return repository.save(funcionarioCompetencia)
    }

    fun editar(id: Int, funcionarioCompetencia: CadastroCompetenciaFuncionario): FuncionarioCompetencia{
        val competenciaEncontrada = repository.findById(id).orElseThrow { CompetenciaNaoEcontradaException("Competencia funcionario nao existe") }
        val servico = servicoRepository.findById(funcionarioCompetencia.servico).orElseThrow { CompetenciaNaoEcontradaException("Servico não existe") }
        val funcionario = usuarioRepository.findById(funcionarioCompetencia.funcionario).orElseThrow { CompetenciaNaoEcontradaException("Funcionario não existe") }

        competenciaEncontrada.id = id
        competenciaEncontrada.servico = servico
        competenciaEncontrada.funcionario = funcionario

        return repository.save(competenciaEncontrada)

    }

    fun deletar(id: Int){
        val funcionarioCompetenciaEncontrado = repository.findById(id).orElseThrow { CompetenciaNaoEcontradaException("Competencia funcionario nao existe") }
        repository.delete(funcionarioCompetenciaEncontrado)
    }

    fun listarPorServico(id: Int): List<FuncionarioCompetenciaDto>? {
        val funcionarioCompetenciaEncontrado = servicoRepository.findById(id)
            .orElseThrow { CompetenciaNaoEcontradaException("Competencia funcionario nao existe") }

        val competencias = repository.findCompetenciaByServicoId(id)

        return competencias?.map { competencia ->
            FuncionarioCompetenciaDto(
                id = competencia.id,
                servico = ServicoDto(
                    id = competencia.servico?.id,
                    nome = competencia.servico?.nome,
                    descricao = competencia.servico?.descricao,
                    tempo = competencia.servico?.tempo,
                    preco = competencia.servico?.preco,
                    status = competencia.servico?.status,
                    simultaneo = competencia.servico?.simultaneo,
                    mediaAvaliacao = null
                ),
                funcionario = UsuarioDto(
                    id = competencia.funcionario?.id,
                    nome = competencia.funcionario?.nome,
                    tipoUsuario = TipoUsuarioDto(
                        id = competencia.funcionario?.tipoUsuario?.id,
                        descricao = competencia.funcionario?.tipoUsuario?.descricao
                    ),
                    telefone = competencia.funcionario?.telefone,
                    email = competencia.funcionario?.email,
                    foto = competencia.funcionario?.foto.toString()
                )
            )
        }
    }

    fun listarPorFuncionario(id: Int): List<FuncionarioCompetenciaDto> {
        // Verifica se o funcionario existe
        val funcionario = usuarioRepository.findById(id)
            .orElseThrow { CompetenciaNaoEcontradaException("Funcionario não existe") }

        // Busca apenas os IDs das competências desse funcionario
        val competenciasIds = repository.findCompetenciaByFuncionarioId(id) ?: emptyList()

        return competenciasIds.mapNotNull { fc ->
            // Para cada id, buscar os objetos completos
            val servico = fc.servico?.id?.let { servicoRepository.findById(it).orElse(null) }
            val funcionarioCompleto = fc.funcionario?.id?.let { usuarioRepository.findById(it).orElse(null) }

            if (servico == null || funcionarioCompleto == null) return@mapNotNull null

            FuncionarioCompetenciaDto(
                id = fc.id,
                servico = ServicoDto(
                    id = servico.id,
                    nome = servico.nome,
                    descricao = servico.descricao,
                    tempo = servico.tempo,
                    preco = servico.preco,
                    status = servico.status,
                    simultaneo = servico.simultaneo,
                    mediaAvaliacao = null
                ),
                funcionario = UsuarioDto(
                    id = funcionarioCompleto.id,
                    nome = funcionarioCompleto.nome,
                    tipoUsuario = funcionarioCompleto.tipoUsuario?.let {
                        TipoUsuarioDto(id = it.id, descricao = it.descricao)
                    },
                    telefone = funcionarioCompleto.telefone,
                    email = funcionarioCompleto.email,
                    foto = funcionarioCompleto.foto.toString() ?: ""
                )
            )
        }
    }
}