package sptech.salonTime.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

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
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            // Monta o corpo do e-mail
            val corpo = when (destinoTipo.lowercase()) {
                "cliente" -> """
                    <p>Olá <b>$nome</b>,</p>
                    <p>Confirmamos o agendamento do seu serviço <b>$servico</b>:</p>
                    <p>📅 Data: $data<br>⏰ Hora: $hora</p>
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

            // Configurações do e-mail
            helper.setFrom("salontime.atendimento@gmail.com")
            helper.setTo(destinatario)
            helper.setSubject(assunto)
            helper.setText(corpo, true) // true = corpo HTML

            mailSender.send(message)
            println("✅ Email enviado para $destinatario")

        } catch (e: Exception) {
            println("❌ Erro ao enviar email: ${e.message}")
        }
    }
}
