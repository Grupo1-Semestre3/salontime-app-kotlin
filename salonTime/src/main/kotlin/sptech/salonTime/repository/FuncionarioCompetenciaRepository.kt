package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.entidade.FuncionarioCompetencia

interface FuncionarioCompetenciaRepository: JpaRepository<FuncionarioCompetencia, Int> {

    // Método para encontrar competências de um serviço por ID do serviço
    @Query(nativeQuery = true, value = "SELECT * FROM funcionario_competencia WHERE servico_id = ?1")
    fun findCompetenciaByServicoId(id: Int): List<FuncionarioCompetencia>?

}