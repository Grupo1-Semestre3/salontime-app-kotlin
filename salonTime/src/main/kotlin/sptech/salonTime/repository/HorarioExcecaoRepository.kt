package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.HorarioExcecao

interface HorarioExcecaoRepository: JpaRepository<HorarioExcecao, Int> {
}