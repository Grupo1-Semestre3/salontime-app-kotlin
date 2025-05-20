package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.exception.*
import sptech.salonTime.repository.*
import java.time.LocalDate
import java.time.LocalTime

@Service
class AgendamentoService(private val repository: AgendamentoRepository, val pagamentoRepository: PagamentoRepository, val usuarioRepository: UsuarioRepository, val statusAgendamentoRepository: StatusAgendamentoRepository, val servicoRepository: ServicoRepository) {

    fun listar(): List<Agendamento?> {
        val agendamentos = repository.listarTudo()

        return agendamentos
    }

    fun listarPorId(id: Int ): Agendamento{
        val agendamento = repository.findById(id)

        return repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.")
        }
    }

        fun cadastrar(agendamento: CadastroAgendamentoDto): Agendamento {
            val existeAgendamento = repository.existeConflitoDeAgendamento(
                agendamento.data, agendamento.inicio, agendamento.fim
            )

            if (agendamento.data.isBefore(LocalDate.now())) {
                throw IllegalArgumentException("A data do agendamento não pode ser anterior à data atual.")
            }

            if (existeAgendamento > 0) {
                throw ConflitoDeAgendamentoException("Já existe um agendamento nesse horário.")
            }

            val usuario = usuarioRepository.findById(agendamento.usuario)
                .orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }
            val statusAgendamento = statusAgendamentoRepository.findById(agendamento.statusAgendamento)
                .orElseThrow { StatusAgendamentoNaoEncontradoException("Status de agendamento não encontrado") }
            val servico = servicoRepository.findById(agendamento.servico)
                .orElseThrow { ServicoNaoEcontradoException("Serviço não encontrado") }
            val pagamento = pagamentoRepository.findById(agendamento.pagamento)
                .orElseThrow { PagamentoNaoEncontradoException("Pagamento não encontrado") }
            val funcinario = usuarioRepository.findById(agendamento.funcionario)
                .orElseThrow { UsuarioNaoEncontradoException("Funcionário não encontrado") }

            val novoAgendamento = Agendamento(
                usuario = usuario,
                servico = servico,
                funcionario = funcinario,
                statusAgendamento = statusAgendamento,
                pagamento = pagamento,
                data = agendamento.data,
                inicio = agendamento.inicio,
                fim = agendamento.fim,
                preco = agendamento.preco
            )
            return repository.save(novoAgendamento)
        }

    fun atualizarAtributo(id: Int, atributo: String, novoValor: String): Agendamento {
        val agendamento = repository.findById(id).orElseThrow{AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.") }


            try {
                val agendamentoAtualizado = when (atributo) {
                    "data" -> agendamento.copy(data = LocalDate.parse(novoValor))
                    "inicio" -> agendamento.copy(inicio = LocalTime.parse(novoValor))
                    "fim" -> agendamento.copy(fim = LocalTime.parse(novoValor))
                    "preco" -> agendamento.copy(preco = novoValor.toDouble())
                    "status" -> agendamento.copy(statusAgendamento = novoValor.toInt().let { statusAgendamentoRepository.findById(it).orElse(null) })
                    else -> throw AtributoInvalidoAoAtualizarException("Atributo inválido: $atributo")
                }
                return repository.save(agendamentoAtualizado)
            } catch (e: Exception) {
                throw AtributoInvalidoAoAtualizarException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
            }

    }

    fun buscarProximosAgendamentos(): List<Agendamento>? {
        return repository.buscarProximosAgendamentos()
    }

}