package sptech.salonTime.service

import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.repository.HorarioExcecaoRepository

class HorarioExcecaoService (var repository: HorarioExcecaoRepository) {

    fun listar(): List<HorarioExcecao> {
        return repository.findAll() ?: emptyList()
    }
    

}