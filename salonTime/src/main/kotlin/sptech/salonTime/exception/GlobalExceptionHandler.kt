package sptech.salonTime.exception

import com.mysql.cj.exceptions.CJException
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
}