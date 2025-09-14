package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

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
        val helper = MimeMessageHelper(message, "utf-8")

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




        helper.setFrom("salontime.atendimento@gmail.com")
        helper.setTo(destinatario)
        helper.setSubject(assunto)
        helper.setText(corpo, false) // false = texto simples, true = HTML

        mailSender.send(message)

        println("✅ Email enviado para $destinatario")
    }
}
