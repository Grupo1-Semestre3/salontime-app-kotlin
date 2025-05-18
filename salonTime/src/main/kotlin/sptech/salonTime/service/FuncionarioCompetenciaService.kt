package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.FuncionarioCompetencia
import sptech.salonTime.repository.FuncionarioCompetenciaRepository

@Service
class FuncionarioCompetenciaService(val repository: FuncionarioCompetenciaRepository) {

    fun listar(): List<FuncionarioCompetencia> {
        return repository.findAll()
    }

    fun salvar(funcionarioCompetencia: FuncionarioCompetencia): FuncionarioCompetencia {
        return repository.save(funcionarioCompetencia)
    }

    //Testar o apagar e editar no mysql

}