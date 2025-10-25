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

    @Query("""
        
    select * from usuario where senha = :senha and email = :email and ativo = true;
        
    """, nativeQuery = true)
    fun login( @Param("email") email: String,  @Param("senha") senha: String): Usuario?

    fun findAllByTipoUsuarioIdAndAtivoTrue(i: Int): List<Usuario>

    @Query(
        value = """
        SELECT 
            u.id AS id,
            u.nome AS nome,
            u.email AS email,
            u.telefone AS telefone,
            COUNT(a.id) AS totalPendencias
        FROM usuario u
        LEFT JOIN agendamento a 
            ON a.usuario_id = u.id 
            AND a.status_agendamento_id = 4
        WHERE u.tipo_usuario_id = 2
          AND u.ativo = true
        GROUP BY u.id, u.nome, u.email, u.telefone
        """,
        nativeQuery = true
    )
    fun listarClientesComPendencias(): List<Map<String, Any>>

}