package sptech.salonTime.service

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class AiService(
    @Value("\${ai.gemini.key:}") private val geminiKey: String,
    @Value("\${ai.gemini.model:gemini-1.5-flash}") private val model: String,
) {
    private val restTemplate = RestTemplate()

    @PostConstruct
    fun logKey() {
        if (geminiKey.isBlank()) println("[AI] CHAVE_GEMINI ausente.") else println("[AI] CHAVE_GEMINI carregada (len=${geminiKey.trim().length}, prefix='${geminiKey.take(5)}...'). Modelo=${model}")
    }

    private fun listModelsV1(): Map<*, *>? = try {
        restTemplate.getForEntity<Map<String, Any>>("https://generativelanguage.googleapis.com/v1/models?key=${geminiKey.trim()}").body
    } catch (_: Exception) { null }

    private fun listModelsV1Beta(): Map<*, *>? = try {
        restTemplate.getForEntity<Map<String, Any>>("https://generativelanguage.googleapis.com/v1beta/models?key=${geminiKey.trim()}").body
    } catch (_: Exception) { null }

    // Exposto para o controller: retorna catálogo de modelos v1 e v1beta simplificado
    fun listarModelos(): Map<String, Any> {
        if (geminiKey.isBlank()) return mapOf("erro" to "CHAVE_GEMINI ausente")
        val v1 = listModelsV1()
        val v1beta = listModelsV1Beta()
        return mapOf(
            "v1" to (v1 ?: emptyMap<String, Any>()),
            "v1beta" to (v1beta ?: emptyMap<String, Any>()),
        )
    }

    private fun resolveUsableModel(): String {
        val key = geminiKey.trim()
        if (key.isBlank()) return model
        val v1 = listModelsV1()
        val all = mutableSetOf<String>()
        v1?.get("models")?.let { m ->
            if (m is List<*>) m.forEach { entry ->
                val name = (entry as? Map<*, *>)?.get("name") as? String
                if (name != null) all.add(name.removePrefix("models/"))
            }
        }
        if (all.isEmpty()) return model
        // Prefer explicit configured model if present
        if (all.contains(model)) return model
        // Fallbacks comuns
        val fallbacks = listOf("gemini-1.5-flash", "gemini-1.5-pro", "gemini-pro", "gemini-pro-vision")
        return fallbacks.firstOrNull { all.contains(it) } ?: all.first()
    }

    fun perguntar(pergunta: String): String {
        if (pergunta.isBlank()) return "Pergunta vazia. Informe um contexto ou pergunta."
        val key = geminiKey.trim()
        if (key.isBlank()) return "Configuração ausente: defina CHAVE_GEMINI e reinicie."
        val chosenModel = resolveUsableModel()
        val url = "https://generativelanguage.googleapis.com/v1/models/${chosenModel}:generateContent"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add("x-goog-api-key", key)
        }

        val body = mapOf(
            "contents" to listOf(
                mapOf(
                    "parts" to listOf(mapOf("text" to pergunta))
                )
            )
        )

        return try {
            val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java).body
            if (response == null) return "Resposta nula do provedor."
            if (response["error"] != null) return "Erro bruto IA (${chosenModel}): ${response["error"]}".take(400)
            val candidates = response["candidates"] as? List<*> ?: return "Sem candidatos na resposta.".take(200)
            val first = candidates.firstOrNull() as? Map<*, *> ?: return "Sem candidate principal.".take(200)
            val content = first["content"] as? Map<*, *> ?: return "Sem content dentro do candidate.".take(200)
            val parts = content["parts"] as? List<*> ?: return "Sem parts dentro de content.".take(200)
            val firstPart = parts.firstOrNull() as? Map<*, *> ?: return "Sem part válida.".take(200)
            val text = firstPart["text"] as? String ?: return "Sem texto final.".take(200)
            text.trim()
        } catch (e: Exception) {
            "Erro ao consultar IA: ${e.message}".take(400)
        }
    }
}
