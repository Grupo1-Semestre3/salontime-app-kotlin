package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.FuncionarioCompetenciaDto
import sptech.salonTime.dto.TipoUsuarioDto
import sptech.salonTime.dto.UsuarioDto
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

    fun salvar(funcionarioCompetencia: FuncionarioCompetencia): FuncionarioCompetencia {

        val servico = funcionarioCompetencia.servico?.id
            ?.let { servicoRepository.findById(it).orElse(null) }
        val funcionario = funcionarioCompetencia.funcionario?.id
            ?.let { usuarioRepository.findById(it).orElse(null) }

        funcionarioCompetencia.servico = servico
        funcionarioCompetencia.funcionario = funcionario

        return repository.save(funcionarioCompetencia)
    }

    fun editar(id: Int, funcionarioCompetencia: FuncionarioCompetencia): FuncionarioCompetencia{
        val competenciaEncontrada = repository.findById(id).orElseThrow { CompetenciaNaoEcontradaException("Competencia funcionario nao existe") }

        var servico = funcionarioCompetencia.servico?.id
            ?.let { servicoRepository.findById(it).orElse(null) }
        var funcionario = funcionarioCompetencia.funcionario?.id
            ?.let { usuarioRepository.findById(it).orElse(null) }

        funcionarioCompetencia.id = id
        funcionarioCompetencia.servico = servico
        funcionarioCompetencia.funcionario = funcionario

        return repository.save(funcionarioCompetencia)

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

}