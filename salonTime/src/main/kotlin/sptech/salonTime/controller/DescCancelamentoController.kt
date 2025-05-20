package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.service.DescCancelamentoService

@RestController
@RequestMapping("/cancelamentos")
class DescCancelamentoController (val service: DescCancelamentoService) {

    @GetMapping
    fun listar(): ResponseEntity<List<DescCancelamento>> {
        val cancelamentos = service.listar()
        return if (cancelamentos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(cancelamentos)
        }
    }

    @GetMapping("/{id}")
    fun listarPorId(id: Int): ResponseEntity<DescCancelamento> {
        val cancelamento = service.listarPorId(id)
        return if (cancelamento != null) {
            ResponseEntity.status(200).body(cancelamento)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping()
    fun criar(novoCancelamento: DescCancelamento): ResponseEntity<DescCancelamento> {
        return ResponseEntity.status(201).body(service.criar(novoCancelamento))
    }

}