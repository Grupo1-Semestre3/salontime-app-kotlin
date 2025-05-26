package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.Pagamento
import sptech.salonTime.service.PagamentoService

@RestController
@RequestMapping("/pagamento")
class PagamentoController (var service: PagamentoService) {

    @GetMapping
    fun listar(): ResponseEntity<List<Pagamento>> {
        val pagamentos = service.listar()
        return if (pagamentos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(pagamentos)
        }
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable id: Int): ResponseEntity<Pagamento> {
        val pagamento = service.listarPorId(id)
        return if (pagamento != null) {
            ResponseEntity.status(200).body(pagamento)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping()
    fun criar(@RequestBody novoPagamento: Pagamento): ResponseEntity<Pagamento> {
        return ResponseEntity.status(201).body(service.criar(novoPagamento))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody novoPagamento: Pagamento): ResponseEntity<Pagamento> {
        val pagamentoAtualizado = service.atualizar(id, novoPagamento)
        return if (pagamentoAtualizado != null) {
            ResponseEntity.status(200).body(pagamentoAtualizado)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        val pagamento = service.listarPorId(id)
        return if (pagamento != null) {
            service.deletar(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

}