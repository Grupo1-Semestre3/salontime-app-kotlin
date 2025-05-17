package sptech.salonTime.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AgendamentoNaoEncontradoException::class)
    fun handleRecursoNaoEncontrado(ex: AgendamentoNaoEncontradoException): ResponseEntity<Any> {
        val erro = mapOf(
            "erro" to ex.message,
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }
}