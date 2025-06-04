package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.repository.HorarioExcecaoRepository
@Service
class HorarioExcecaoService (var repository: HorarioExcecaoRepository) {

    fun listar(): List<HorarioExcecao> {
        return repository.findAll()
    }

    fun editarAberto(id: Int, aberto: Boolean): HorarioExcecao {
        val horarioExcecao = repository.findById(id).orElseThrow { Exception("Horario excecao nao encontrado") }
        horarioExcecao.aberto = aberto
        return repository.save(horarioExcecao)
    }

    fun editar(id: Int, horarioExcecao: HorarioExcecao): HorarioExcecao {
        val horarioExcecaoExistente = repository.findById(id).orElseThrow { Exception("Horario excecao nao encontrado") }

        horarioExcecao.id = id
        horarioExcecaoExistente.dataInicio = horarioExcecao.dataInicio
        horarioExcecaoExistente.dataFim = horarioExcecao.dataFim
        horarioExcecaoExistente.inicio = horarioExcecao.inicio
        horarioExcecaoExistente.fim = horarioExcecao.fim
        horarioExcecaoExistente.capacidade = horarioExcecao.capacidade
        horarioExcecaoExistente.funcionario = horarioExcecao.funcionario

        return repository.save(horarioExcecaoExistente)
    }

    fun salvar(horarioExcecao: HorarioExcecao): HorarioExcecao {
        return repository.save(horarioExcecao)
    }
    

}