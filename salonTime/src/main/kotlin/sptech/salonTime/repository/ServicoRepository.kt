package sptech.salonTime.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.entidade.Servico

interface ServicoRepository: JpaRepository<Servico, Int> {

    /*
Aqui implementamos uma alteração adhock
Sempre que tivermos comandos Update ou Delete,
Temos que ter as anotações @Transactional e @Modifying
NÃO é permitido fazer INSERT dessa forma

A instrução de atualização é feita em JPQL
sempre na anotação @Query
 */

    @Modifying
    @Transactional
    @Query("update Servico s set s.simultaneo = true where s.id = ?1")
    fun ativarSimultaneo(id: Int): Int

    @Modifying
    @Transactional
    @Query("update Servico s set s.simultaneo = false where s.id = ?1")
    fun desativarSimultaneo(id: Int): Int

    @Modifying
    @Transactional
    @Query("update Servico s set s.status = ?2 where s.id = ?1")
    fun mudarStatus(id: Int, status: String): Int

    fun findAllByStatus(status: String): List<Servico>

}