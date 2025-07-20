package sptech.salonTime.repository

import jakarta.persistence.criteria.CriteriaBuilder.In
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import sptech.salonTime.entidade.Usuario
import java.time.LocalDate
import java.time.LocalTime

interface UsuarioRepository: JpaRepository<Usuario, Int> {
    fun findByEmail(email: String): Usuario?
    fun findAllByAtivoTrue(): List<Usuario>?
    fun findAllByAtivoTrueAndTipoUsuarioId(i: Int): List<Usuario>?
    @Query("""
    SELECT f.id 
    FROM usuario f 
    WHERE f.id IN :ids 
    AND NOT EXISTS (
        SELECT 1 
        FROM agendamento a 
        WHERE a.funcionario_id = f.id 
        AND a.data = :data 
        AND (a.inicio < :fim AND a.fim > :inicio)
    )
""", nativeQuery = true)
    fun buscasFuncionariosDisponiveisPorData(
        @Param("data") data: LocalDate,
        @Param("inicio") inicio: LocalTime,
        @Param("fim") fim: LocalTime,
        @Param("ids") ids: List<Int>
    ): List<Int>

    @Query("""
        
    select id from usuario where tipo_usuario_id = 3 or tipo_usuario_id = 1;
        
    """, nativeQuery = true)
    fun buscarIdsFuncionarios(): List<Int>

}