package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.service.AiService

@RestController
@RequestMapping("/api/ai")
class AiController(
    private val aiService: AiService
) {
    data class PerguntaDto(val pergunta: String)

    @PostMapping("/perguntar")
    fun perguntar(@RequestBody dto: PerguntaDto): ResponseEntity<Map<String, String>> {
        val resposta = aiService.perguntar(dto.pergunta)
        return ResponseEntity.ok(mapOf("resposta" to resposta))
    }

    @GetMapping("/modelos")
    fun listarModelos(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(aiService.listarModelos())
    }
}
