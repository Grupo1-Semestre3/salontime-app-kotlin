package sptech.salonTime.service

import com.sendgrid.*
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EmailService(
    @Value("\${sendgrid.api.key}") private val apiKey: String,
    @Value("\${sendgrid.from.email}") private val fromEmail: String
) {

    @Async
    fun enviarEmail(
        nome: String?,
        servico: String?,
        data: String?,
        hora: String?,
        assunto: String,
        destinatario: String,
        destinoTipo: String
    ) {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val dataFormatada = LocalDate.parse(data).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val corpo = when (destinoTipo.lowercase()) {
                "cliente" -> """
                    <p>Olá <b>$nome</b>,</p>
                    <p>Confirmamos o agendamento do seu serviço <b>$servico</b>:</p>
                    <p>📅 Data: $dataFormatada<br>⏰ Hora: $hora</p>
                    <p>Caso precise reagendar ou tirar dúvidas, estamos à disposição.</p>
                    <p>Atenciosamente,<br>Equipe de Suporte</p>
                """.trimIndent()

                "funcionario" -> """
                    <p>Olá <b>$nome</b>,</p>
                    <p>Um novo serviço foi agendado para você:</p>
                    <p>🧰 Serviço: $servico<br>📅 Data: $data<br>⏰ Hora: $hora</p>
                    <p>Por favor, verifique sua agenda e esteja preparado para o atendimento.</p>
                    <p>Qualquer dúvida, entre em contato com a equipe de suporte.</p>
                    <p>Atenciosamente,<br>Equipe de Suporte</p>
                """.trimIndent()

                else -> "Olá, este é um e-mail do SalonTime."
            }

            // Construção do email SendGrid
            val from = Email(fromEmail)
            val to = Email(destinatario)
            val content = Content("text/html", corpo)
            val mail = Mail(from, assunto, to, content)

            // Envio via SendGrid
            val sg = SendGrid(apiKey)
            val request = Request()

            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()

            val response = sg.api(request)

            println("STATUS: ${response.statusCode}")
            println("BODY: ${response.body}")
            println("HEADERS: ${response.headers}")
            println("✅ Email enviado para $destinatario")

        } catch (e: Exception) {
            println("❌ Erro ao enviar email: ${e.message}")
            e.printStackTrace()
        }
    }
}
