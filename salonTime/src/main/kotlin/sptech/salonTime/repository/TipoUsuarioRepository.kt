package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.TipoUsuario

interface TipoUsuarioRepository : JpaRepository<TipoUsuario, Int> {
}