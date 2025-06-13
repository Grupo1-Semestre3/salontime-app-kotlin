package sptech.salonTime.repository

import jakarta.persistence.criteria.CriteriaBuilder.In
import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Usuario

interface UsuarioRepository: JpaRepository<Usuario, Int> {
    fun findByEmail(email: String): Usuario?
    fun findAllByAtivoTrue(): List<Usuario>?
}