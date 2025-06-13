package sptech.salonTime.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.dto.ServicoDto
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

    @Query("""
    SELECT new sptech.salonTime.dto.ServicoDto(
        s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao,
        COALESCE(AVG(a.notaServico), 0.0)
    )
    FROM Servico s
    LEFT JOIN Agendamento ag ON s.id = ag.servico.id
    LEFT JOIN Avaliacao a ON ag.id = a.agendamento.id
    WHERE s.status = 'ATIVO'
    GROUP BY s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao
""")
    fun listarTodosAtivosComMedia(): List<ServicoDto>


    @Query("""
    SELECT new sptech.salonTime.dto.ServicoDto(
        s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao,
        COALESCE(AVG(a.notaServico), 0.0)
    )
    FROM Servico s
    LEFT JOIN Agendamento ag ON s.id = ag.servico.id
    LEFT JOIN Avaliacao a ON ag.id = a.agendamento.id
    WHERE s.status = 'INATIVO'
    GROUP BY s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao
""")
    fun listarDesativadosComMedia(): List<ServicoDto>

    @Query("""
        
       SELECT new sptech.salonTime.dto.ServicoDto(
        s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao,
        COALESCE(AVG(a.notaServico), 0.0)
    )
    FROM Servico s
    LEFT JOIN Agendamento ag ON s.id = ag.servico.id
    LEFT JOIN Avaliacao a ON ag.id = a.agendamento.id
    WHERE s.id = ?1
    GROUP BY s.id, s.nome, s.preco, s.tempo, s.status, s.simultaneo, s.descricao
    """)
    fun listarPorIdComMedia(id: Int): ServicoDto




}