package sptech.salonTime.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async

@Service
class EmailService(

    private val mailSender: JavaMailSender) {

    fun enviarEmail(
        nome: String?,
        servico: String?,
        data: String?,
        hora: String?,
        assunto: String,
        destinatario: String,
        destinoTipo: String
    ) {
        val message = mailSender.createMimeMessage()
        val helper = SimpleMailMessage()

        var corpo = ""

        if (destinoTipo == "cliente"){
            corpo = """
            Olá $nome,
            
            Confirmamos o agendamento do seu serviço **$servico**:
            
            📅 Data: $data  
            ⏰ Hora: $hora
        
            Caso precise reagendar ou tirar dúvidas, estamos à disposição.
        
            Atenciosamente,  
            Equipe de Suporte
        """.trimIndent()

        }else if(destinoTipo == "funcionario"){
            corpo = """
            Olá $nome,
            
            Um novo serviço foi agendado para você:
        
            🧰 Serviço: $servico  
            📅 Data: $data  
            ⏰ Hora: $hora
        
            Por favor, verifique sua agenda e esteja preparado para o atendimento.
        
            Qualquer dúvida, entre em contato com a equipe de suporte.
        
            Atenciosamente,  
            Equipe de Suporte
        """.trimIndent()
        }




        helper.setFrom("salontime.atendimento@outlook.com")
        helper.setTo(destinatario)
        helper.setSubject(assunto)
        helper.setText(corpo)

        mailSender.send(message)

        println("✅ Email enviado para $destinatario")
    }
}
