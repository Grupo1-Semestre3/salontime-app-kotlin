package sptech.salonTime.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AgendamentoNaoEncontradoException::class)
    fun handleAgendametoNaoEncontrado(ex: AgendamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AtributoInvalidoAoAtualizarException::class)
    fun handleConflitoDeAgendamento(ex: AtributoInvalidoAoAtualizarException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AvaliacaoNaoExisteException::class)
    fun handleAvalicaoNaoExiste(ex: AvaliacaoNaoExisteException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConflitoDeAgendamentoException::class)
    fun handleConflitoDeAgendamento(ex: ConflitoDeAgendamentoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.CONFLICT.value()
        )
        return ResponseEntity(erro, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(CompetenciaNaoEcontradaException::class)
    fun handleCompetenciaNaoEncontrada(ex: CompetenciaNaoEcontradaException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UsuarioNaoEncontradoException::class)
    fun handleUsuarioNaoEcontrado(ex: UsuarioNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StatusAgendamentoNaoEncontradoException::class)
    fun handleStatusAgendamento(ex: StatusAgendamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PagamentoNaoEncontradoException::class)
    fun handlePagamentoNaoEcontrado(ex: PagamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ServicoNaoEcontradoException::class)
    fun handleServico(ex: ServicoNaoEcontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(FuncionarioNaoEcontradoException::class)
    fun handleFuncionarioNaoEcontrado(ex: FuncionarioNaoEcontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CupomNaoEncontradoException::class)
    fun handleCupomNaoEcontrado(ex: CupomNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(CupomDestinadoNaoEncontradoException::class)
    fun handleCupomDestinadoNaoEcontrado(ex: CupomDestinadoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(UsuarioEstaDesativadoException::class)
    fun handleUsuarioEstaDesativado(ex: UsuarioEstaDesativadoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.UNAUTHORIZED.value()
        )
        return ResponseEntity(erro, HttpStatus.UNAUTHORIZED)
    }
    @ExceptionHandler(TipoUsuarioNaoEncontradoException::class)
    fun handleTipoUsuarioNaoEncontrado(ex: TipoUsuarioNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(CupomConfiguracaoException::class)
    fun handleCupomConfiguracaoNaoEncontrado(ex: CupomConfiguracaoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(DataErradaException::class)
    fun handleDataErradaException(ex: DataErradaException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DescCancelamentoNaoEncontradoException::class)
    fun handleDescCancelamentoNaoEncontradoException(ex: DescCancelamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(FuncionamentoNaoEncontradoException::class)
    fun handleFuncionarioNaoEcontradoException(ex: FuncionamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AtributoInvalidoPatchException::class)
    fun handleAtributoInvalidoPatchException(ex: AtributoInvalidoPatchException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UsuarioJaCadastradoException::class)
    fun handleUsuarioJaCadastradoExceptionException(ex: UsuarioJaCadastradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }
}