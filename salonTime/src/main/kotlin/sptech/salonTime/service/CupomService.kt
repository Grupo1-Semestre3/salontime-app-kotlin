package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.PointsDto
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.exception.CupomDuplicadoException
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.CupomConfiguracaoRepository
import sptech.salonTime.repository.CupomRepository
import sptech.salonTime.repository.UsuarioRepository

@Service
class CupomService(val repository: CupomRepository, val usuarioRepository: UsuarioRepository, val cupomConfiguracao: CupomConfiguracaoRepository) {

    fun criar(cupom: Cupom): Cupom {

        val existente = repository.findByCodigo(cupom.codigo)

        if (existente != null) {
            throw CupomDuplicadoException("Cupom com código '${cupom.codigo}' já existe.")
        }
        return repository.save(cupom)
    }


    fun listarAtivos(): List<Cupom> {
        return repository.listarAtivos()
    }


    fun atualizar(id: Int, cupomAtualizado: Cupom): Cupom? {
        val cupom = repository.findById(id).orElse(null)
        return if (cupom != null) {
            val atualizado = cupomAtualizado.copy(id = id)
            repository.save(atualizado)
        } else {
            null
        }
    }

    fun deletar(id: Int): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
    }

   /* fun validarVigencia(codigo: String): Cupom? {

    }*/

    fun desativar(id: Int): Boolean {
        val cupom = repository.findById(id)
        return if (cupom.isPresent) {
            val cupomAtualizado = cupom.get().apply { ativo = false }
            repository.save(cupomAtualizado)
            true
        } else {
            false
        }
    }

    fun calcularPoints(idUsuario: Int): PointsDto? {
        usuarioRepository.findById(idUsuario)
            .orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado para cálculo de pontos.") }

        val points = repository.calcularPoints(idUsuario)


        if (points == null) {

            val cupomConfig = cupomConfiguracao.findById(1)
                .orElseThrow { CupomNaoEncontradoException("Configuração de cupom não encontrada.") }

            val total = cupomConfig.intervaloAtendimento ?: 0
            val desconto = cupomConfig.porcentagemDesconto ?: 0

            return PointsDto(
                pointsParcial = 0,
                pointsTotal = cupomConfig.intervaloAtendimento!!.toLong(),
                porcentagemCupom = cupomConfig.porcentagemDesconto!!.toInt()
            )
        }

        return points
    }

}