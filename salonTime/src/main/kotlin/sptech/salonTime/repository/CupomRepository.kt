package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.entidade.Cupom

interface CupomRepository : JpaRepository<Cupom, Int> {
    @Query("SELECT c FROM Cupom c WHERE c.ativo = true AND c.fim > CURRENT_TIME")
    fun listarAtivos(): List<Cupom>
}
