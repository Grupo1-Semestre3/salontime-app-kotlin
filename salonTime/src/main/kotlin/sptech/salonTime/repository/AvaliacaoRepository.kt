package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sptech.salonTime.entidade.Avaliacao

@Repository
interface AvaliacaoRepository : JpaRepository<Avaliacao, Int>{
}