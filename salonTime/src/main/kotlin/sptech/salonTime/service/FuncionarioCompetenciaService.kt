package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.FuncionarioCompetencia
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

    //Testar o apagar e editar no mysql

}