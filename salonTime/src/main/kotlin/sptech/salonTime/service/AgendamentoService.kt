package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.exception.*
import sptech.salonTime.mapper.AgendamentoMapper
import sptech.salonTime.repository.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Service
class AgendamentoService(
    private val repository: AgendamentoRepository,
    val pagamentoRepository: PagamentoRepository,
    val usuarioRepository: UsuarioRepository,
    val statusAgendamentoRepository: StatusAgendamentoRepository,
    val servicoRepository: ServicoRepository,
    val cupomRepository: CupomRepository,
    val funcionamentoRepository: FuncionamentoRepository
) {

    fun listar(): List<AgendamentoDto?> {
        val agendamentos = repository.listarTudo()

        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }

    fun listarPorId(id: Int): AgendamentoDto {
        val agendamentoEncontrado = repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.")
        }

        return AgendamentoMapper.toDto(agendamentoEncontrado)
    }

    fun cadastrar(agendamento: CadastroAgendamentoDto): AgendamentoDto {

        val cupom = if (agendamento.cupom != null) {
            cupomRepository.findByCodigo(agendamento.cupom)
                ?: throw CupomNaoEncontradoException("Cupom não encontrado ou inválido.")
        } else {
            null
        }



        if (agendamento.data < LocalDate.now()) {
            throw DataErradaException("A data do agendamento não pode ser anterior à data atual.")
        }



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
        val funcionario = usuarioRepository.findById(agendamento.funcionario)
            .orElseThrow { FuncionarioNaoEcontradoException("Funcionario não encontrado") }

        if (!funcionario.tipoUsuario?.id?.equals(3)!!) {
            throw FuncionarioNaoEcontradoException("O usuário selecionado não é um funcionário.")
        }

        val statusAgendamento = statusAgendamentoRepository.findById(agendamento.statusAgendamento)
            .orElseThrow { StatusAgendamentoNaoEncontradoException("Status de agendamento não encontrado") }
        val servico = servicoRepository.findById(agendamento.servico)
            .orElseThrow { ServicoNaoEcontradoException("Serviço não encontrado") }
        val pagamento = pagamentoRepository.findById(agendamento.pagamento)
            .orElseThrow { PagamentoNaoEncontradoException("Pagamento não encontrado") }


        val novoAgendamento = Agendamento(
            usuario = usuario,
            servico = servico,
            funcionario = funcionario,
            cupom = cupom,
            statusAgendamento = statusAgendamento,
            pagamento = pagamento,
            data = agendamento.data,
            inicio = agendamento.inicio,
            fim = agendamento.fim
        )
        val agendamentoSalvo = repository.save(novoAgendamento)

        return AgendamentoMapper.toDto(agendamentoSalvo)
    }

    fun atualizarAtributo(id: Int, atributo: String, novoValor: String): AgendamentoDto {
        val agendamento = repository.findById(id)
            .orElseThrow { AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.") }


        try {
            val agendamentoAtualizado = when (atributo) {
                "data" -> agendamento.copy(data = LocalDate.parse(novoValor))
                "inicio" -> agendamento.copy(inicio = LocalTime.parse(novoValor))
                "fim" -> agendamento.copy(fim = LocalTime.parse(novoValor))
                "preco" -> agendamento.copy(preco = novoValor.toDouble())
                "status" -> agendamento.copy(
                    statusAgendamento = novoValor.toInt().let { statusAgendamentoRepository.findById(it).orElse(null) })

                else -> throw AtributoInvalidoAoAtualizarException("Atributo inválido: $atributo")
            }
            val agendamentoSalvo = repository.save(agendamentoAtualizado)

            return AgendamentoMapper.toDto(agendamentoSalvo)

        } catch (e: Exception) {
            throw AtributoInvalidoAoAtualizarException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
        }

    }

    fun buscarProximosAgendamentosPorFuncionario(id: Int): List<AgendamentoDto> {

        val funcionario = usuarioRepository.findById(id).orElseThrow {
            FuncionarioNaoEcontradoException("Funcionário com ID $id não encontrado.")
        }

        val agendamentos = repository.buscarProximosAgendamentosPorFuncionario(id)


        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }

    fun buscarProximosAgendamentosPorUsuario(id: Int): AgendamentoDto? {

        val usuario = usuarioRepository.findById(id).orElseThrow {
            UsuarioNaoEncontradoException("Usuário com ID $id não encontrado.")
        }

        val agendamento = repository.buscarProximosAgendamentosPorUsuario(id)

        if (agendamento == null) {
            throw AgendamentoNaoEncontradoException("Nenhum agendamento encontrado para o usuário com ID $id.")
        }

        return AgendamentoMapper.toDto(agendamento)
    }

    fun buscarAgendamentosPassadosPorUsuario(id: Int): List<AgendamentoDto>? {

        val usuario = usuarioRepository.findById(id).orElseThrow {
            UsuarioNaoEncontradoException("Usuário com ID $id não encontrado.")
        }

        val agendamentos = repository.buscarAgendamentosPassadosPorUsuario(id)

        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }

    fun buscarAgendamentosPassadosPorFuncionario(id: Int): List<AgendamentoDto>? {

        val funcionario = usuarioRepository.findById(id).orElseThrow {
            FuncionarioNaoEcontradoException("Funcionário com ID $id não encontrado.")
        }

        val agendamentos = repository.buscarAgendamentosPassadosPorFuncionario(id)

        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }

    fun atualizarStatus(id: Int, status: Int): AgendamentoDto? {
        var agendamentoEcontrado = repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento não encontrado")
        }

        var statusEncontrado = statusAgendamentoRepository.findById(status).orElseThrow {
            StatusAgendamentoNaoEncontradoException("Status de agendamento não encontrado")
        }

        agendamentoEcontrado.id = id
        agendamentoEcontrado.statusAgendamento = statusEncontrado;
        var agendamentoSalvo = repository.save(agendamentoEcontrado)

        return AgendamentoMapper.toDto(agendamentoSalvo)
    }

    fun atualizarValor(id: Int, valor: Double): AgendamentoDto? {
        var agendamentoEcontrado = repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento não encontrado")
        }

        val tipoPagamento = agendamentoEcontrado.pagamento?.id?.let { pagamentoRepository.findById(it).orElseThrow { PagamentoNaoEncontradoException("Pagamento Não encontrado") } }

        val taxa = tipoPagamento?.taxa

        if (taxa != null) {
            agendamentoEcontrado.preco = valor - (valor * taxa / 100)
        }

        var agendamentoSalvo = repository.save(agendamentoEcontrado)
        return AgendamentoMapper.toDto(agendamentoSalvo)

    }

    fun buscarAgendamentosCancelados(): List<AgendamentoDto>? {
        val agendamentos = repository.buscarAgendamentosCancelados()
        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }

    fun obterHorariosDisponiveis(idServico: Int, data: LocalDate): List<HorarioDisponivelDto>? {

        val servico = servicoRepository.findById(idServico).orElseThrow {
            ServicoNaoEcontradoException("Serviço com ID $idServico não encontrado.")
        }
//        if (data.isBefore(LocalDate.now())) {
//            throw DataErradaException("A data do agendamento não pode ser anterior à data atual.")
//        }

        val diaDaSemana: DayOfWeek = data.dayOfWeek

        val diaSemanaEnum: DiaSemana = DiaSemana.valueOf(diaDaSemana.name)

        val funcionamento = funcionamentoRepository.findByDiaSemana(diaSemanaEnum)
            ?: throw FuncionamentoNaoEncontradoException("Funcionamento não encontrado para o dia da semana: $diaDaSemana")

        if (!funcionamento.aberto!!) {
            throw FuncionamentoNaoEncontradoException("O salão não está aberto no dia ${data.dayOfWeek}.")
        }

        val horario: LocalTime? = servico.tempo // 2h45
        val duracao: Long = (horario?.toSecondOfDay()?.toLong() ?: 0) / 60


        val horariosOcupados = repository.buscarHorariosOcupados(data)
        return funcionamento.inicio?.let {
            funcionamento.fim?.let { it1 ->
                gerarHorariosDisponiveis(horariosOcupados,
                    it, it1, 60L, duracao)
            }
        }
    }






    private fun gerarHorariosDisponiveis(
        horariosOcupados: List<HorariosOcupadosDto>,
        abertura: LocalTime,
        fechamento: LocalTime,
        intervaloMinutos: Long,
        duracaoServicoMinutos: Long
    ): List<HorarioDisponivelDto> {
        val horariosDisponiveis = mutableListOf<HorarioDisponivelDto>()

        var horarioAtual = abertura

        while (horarioAtual.plusMinutes(duracaoServicoMinutos) <= fechamento) {
            val fimHorarioAtual = horarioAtual.plusMinutes(duracaoServicoMinutos)

            val conflito = horariosOcupados.any { ocupado ->
                val inicioOcupado = LocalTime.parse(ocupado.getInicio().toString())
                val fimOcupado = LocalTime.parse(ocupado.getFim().toString())

                // Verifica se há sobreposição
                horarioAtual.isBefore(fimOcupado) && fimHorarioAtual.isAfter(inicioOcupado)
            }

            if (!conflito) {
                horariosDisponiveis.add(
                    HorarioDisponivelDto(horario = horarioAtual.toString())
                )
            }

            // Avança para o próximo horário possível
            horarioAtual = horarioAtual.plusMinutes(intervaloMinutos)
        }

        return horariosDisponiveis
    }





    fun listarCalendario(idFuncionario: Int): List<CalendarioDto?> {

        //val agendamentos: List<Agendamento> = repository.findAll()

        val agendamentos: List<Agendamento> = repository.listarCalendarioPorFuncionario(idFuncionario)

        return agendamentos.map { agendamento ->
            CalendarioDto(
                titulo = "${agendamento.usuario?.nome} - ${agendamento.servico?.nome}",
                data = agendamento.data.toString(),
                inicio = agendamento.inicio.toString().substring(0, 5), // "HH:mm"
                fim = agendamento.fim.toString().substring(0, 5) // "HH:mm"
            )
        }
    }
}