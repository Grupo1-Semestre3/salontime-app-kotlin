package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.dto.NovaDescricaoCancelamentoDto
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.service.DescCancelamentoService

@RestController
@RequestMapping("/cancelamentos")
class DescCancelamentoController (val service: DescCancelamentoService) {

    @GetMapping
    fun listar(): ResponseEntity<List<DescCancelamentoDto>> {
        val cancelamentos = service.listar()
        return if (cancelamentos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(cancelamentos)
        }
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable id: Int): ResponseEntity<DescCancelamentoDto> {
        val cancelamento = service.listarPorId(id)
        return if (cancelamento != null) {
            ResponseEntity.status(200).body(cancelamento)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping()
    fun criar(@RequestBody novoCancelamento: DescCancelamento): ResponseEntity<DescCancelamento> {
        return ResponseEntity.status(201).body(service.criar(novoCancelamento))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, novoCancelamento: DescCancelamento): ResponseEntity<DescCancelamentoDto> {
        return ResponseEntity.status(200).body(service.atualizar(id, novoCancelamento))
    }

    @PatchMapping("/mudar-descricao/{id}")
    fun atualizarDescricao(@PathVariable id: Int, @RequestBody descricao: NovaDescricaoCancelamentoDto): ResponseEntity<DescCancelamentoDto> {
        return ResponseEntity.status(200).body(service.atualizarDescricao(id, descricao))
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        val cancelamento = service.listarPorId(id)
        return if (cancelamento != null) {
            service.deletar(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}