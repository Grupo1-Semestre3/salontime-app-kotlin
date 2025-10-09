package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.*
import sptech.salonTime.exception.*
import sptech.salonTime.mapper.AgendamentoMapper
import sptech.salonTime.repository.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class AgendamentoService(
    private val repository: AgendamentoRepository,
    val pagamentoRepository: PagamentoRepository,
    val usuarioRepository: UsuarioRepository,
    val statusAgendamentoRepository: StatusAgendamentoRepository,
    val servicoRepository: ServicoRepository,
    val cupomRepository: CupomRepository,
    val funcionamentoRepository: FuncionamentoRepository,
    val horarioExcecaoRepository: HorarioExcecaoRepository,
    val funcionarioCompetenciaRepository: FuncionarioCompetenciaRepository,
    val repositoryCupomDestinadoRepository: CupomDestinadoRepository,
    val cupomDestinadoRepository: CupomDestinadoRepository,
    val emailService: EmailService
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

    fun historicoAgendamento(idAgendamento: Int): List<HistoricoAgendamentoDto> {
        repository.findById(idAgendamento).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento com ID $idAgendamento não encontrado.")
        }

        return repository.findHistoricoByAgendamentoId(idAgendamento)


    }



    fun cadastrar(agendamento: CadastroAgendamentoDto): AgendamentoDto {

        var cupomDestinado = CupomDestinado()

        val cupom = if (agendamento.cupom != null) {
            cupomRepository.findByCodigo(agendamento.cupom)
                ?: throw CupomNaoEncontradoException("Cupom não encontrado ou inválido.")
        } else {null}

        val usuario = usuarioRepository.findById(agendamento.usuario)
            .orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }

        if (cupom != null) {

            val usuarioEncontrado = usuarioRepository.findById(agendamento.usuario)

            if (cupom == null) {
                throw CupomNaoEncontradoException("Cupom inválido")
            }

            // Se existir, verifico se está ativo e se a data de fim é maior que a data atual
            if (cupom.ativo == false || cupom.fim!! < java.time.LocalDate.now()) {
                throw CupomNaoEncontradoException("Cupom expirado")
            }

            val cupomUsado = repositoryCupomDestinadoRepository.findByCupomAndUsuario(cupom, usuarioEncontrado)

            if(cupom.tipoDestinatario == "TODOS"){
                if (cupomUsado?.usado == true) {
                    throw CupomNaoEncontradoException("Cupom já utilizado")
                }else if(cupomUsado?.usado == false){
                    cupomUsado.usado = true
                    cupomDestinado = cupomUsado

                }
                if (cupomUsado == null) {
                    cupomDestinado = CupomDestinado(
                        cupom = cupom,
                        usuario = usuario,
                        usado = true
                    )
                }
            }else if(cupom.tipoDestinatario == "EXCLUSIVO"){

                if (cupomUsado != null && cupomUsado.usado == false) {
                    cupomUsado.usado = true
                    cupomDestinado = cupomUsado
                }else{
                    throw CupomNaoEncontradoException("Cupom já utilizado ou não pertence ao usuário")
                }

            }



        }

        val servico = servicoRepository.findById(agendamento.servico)
            .orElseThrow { ServicoNaoEcontradoException("Serviço não encontrado") }

        val fimServico = agendamento.inicio.plusHours(servico.tempo?.hour?.toLong() ?: 0)
            .plusMinutes(servico.tempo?.minute?.toLong() ?: 0)

        if (agendamento.data < LocalDate.now()) {
            throw DataErradaException("A data do agendamento não pode ser anterior à data atual.")
        }

        val existeAgendamento = repository.existeConflitoDeAgendamento(
            agendamento.data, agendamento.inicio, fimServico
        )

        if (agendamento.data.isBefore(LocalDate.now())) {
            throw IllegalArgumentException("A data do agendamento não pode ser anterior à data atual.")
        }

        if (existeAgendamento > 0) {

            val idServicoConflito = repository.pegarAgendamentoConlfito(
                agendamento.data, agendamento.inicio, fimServico
            )

            val checkSimultaneoAgendamentoExistente = servicoRepository.verificarSimultaneo(idServicoConflito)
            val checkSimultaneoFuturoAgendamento = servicoRepository.verificarSimultaneo(agendamento.servico)

            if (checkSimultaneoAgendamentoExistente == 1 && checkSimultaneoFuturoAgendamento == 1) {
                // Permitir o agendamento, pois ambos os serviços permitem simultaneidade
            } else {
                // Não permitir o agendamento, pois pelo menos um dos serviços não permite simultaneidade
                throw ConflitoDeAgendamentoException("Já existe um agendamento nesse horário.")

            }
        }

        // usuario estava verificando aqui

        val listaDeFuncionarios = usuarioRepository.buscarIdsFuncionarios()

        val funcionariosDisponiveis = usuarioRepository.buscasFuncionariosDisponiveisPorData(
            agendamento.data,
            agendamento.inicio,
            fimServico,
            listaDeFuncionarios
        )

        val funcionario = usuarioRepository.findById(funcionariosDisponiveis.get(0))
            .orElseThrow { FuncionarioNaoEcontradoException("Funcionario não encontrado") }

        if (!funcionario.tipoUsuario?.id?.equals(3)!! && !funcionario.tipoUsuario?.id?.equals(1)!!) {
            throw FuncionarioNaoEcontradoException("O usuário selecionado não é um funcionário.")
        }

        val statusAgendamento = statusAgendamentoRepository.findById(agendamento.statusAgendamento)
            .orElseThrow { StatusAgendamentoNaoEncontradoException("Status de agendamento não encontrado") }

        val pagamento = pagamentoRepository.findById(agendamento.pagamento)
            .orElseThrow { PagamentoNaoEncontradoException("Pagamento não encontrado") }


        /*usuario.email?.let {
            emailService.enviarEmail(
                nome = usuario.nome,
                servico = servico.nome,
                data = agendamento.data.toString(),
                hora = agendamento.inicio.toString().substring(0, 5),
                assunto = "Confirmação de Agendamento",
                destinatario = it,
                destinoTipo = "cliente"
            )
        }

        funcionario.email?.let {
            emailService.enviarEmail(
                nome = funcionario.nome,
                servico = funcionario.nome,
                data = agendamento.data.toString(),
                hora = agendamento.inicio.toString().substring(0, 5),
                assunto = "Confirmação de Agendamento",
                destinatario = it,
                destinoTipo = "funcionario"
            )
        }
*/

        val novoAgendamento = Agendamento(
            usuario = usuario,
            servico = servico,
            funcionario = funcionario,
            cupom = cupom,
            statusAgendamento = statusAgendamento,
            pagamento = pagamento,
            data = agendamento.data,
            inicio = agendamento.inicio,
            fim = fimServico
        )
        val agendamentoSalvo = repository.save(novoAgendamento)

        if (agendamento.cupom != null) {
            cupomDestinadoRepository.save(cupomDestinado)
        }



        return AgendamentoMapper.toDto(agendamentoSalvo)
    }

    fun reagendar(id: Int, agendamento: ReagendamentoAgendamentoDto): AgendamentoDto? {

        val agendamentoEcontrado = repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.")
        }

        val servico = agendamentoEcontrado.servico?.id?.let { servicoRepository.findById(it) }?.orElseThrow{ServicoNaoEcontradoException("Serviço não encontrado")}

        val fimServico = agendamento.inicio.plusHours(servico?.tempo?.hour?.toLong() ?: 0)
            .plusMinutes(servico?.tempo?.minute?.toLong() ?: 0)


        if (agendamento.novaData.isBefore(LocalDate.now())) {
            throw DataErradaException("A data do agendamento não pode ser anterior à data atual.")
        }

        val existeAgendamento = repository.existeConflitoDeAgendamento(
            agendamento.novaData, agendamento.inicio, fimServico
        )

        if (existeAgendamento > 0) {

            val idServicoConflito = repository.pegarAgendamentoConlfito(
                agendamento.novaData, agendamento.inicio, fimServico
            )

            val checkSimultaneoAgendamentoExistente = servicoRepository.verificarSimultaneo(idServicoConflito)
            val checkSimultaneoFuturoAgendamento = servicoRepository.verificarSimultaneo(servico?.id!!)

            if (checkSimultaneoAgendamentoExistente == 1 && checkSimultaneoFuturoAgendamento == 1) {
                // Permitir o agendamento, pois ambos os serviços permitem simultaneidade
            } else {
                // Não permitir o agendamento, pois pelo menos um dos serviços não permite simultaneidade
                throw ConflitoDeAgendamentoException("Já existe um agendamento nesse horário.")

            }
        }

        agendamentoEcontrado.data = agendamento.novaData
        agendamentoEcontrado.inicio = agendamento.inicio
        agendamentoEcontrado.fim = fimServico

        val agendamentoSalvo = repository.save(agendamentoEcontrado)

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

        val tipoPagamento = agendamentoEcontrado.pagamento?.id?.let {
            pagamentoRepository.findById(it).orElseThrow { PagamentoNaoEncontradoException("Pagamento Não encontrado") }
        }

//        val taxa = tipoPagamento?.taxa
//
//        if (taxa != null) {
//            agendamentoEcontrado.preco = valor - (valor * taxa / 100)
//        }

        agendamentoEcontrado.preco = valor
        var agendamentoSalvo = repository.save(agendamentoEcontrado)
        return AgendamentoMapper.toDto(agendamentoSalvo)

    }

    fun buscarAgendamentosCancelados(): List<AgendamentoDto>? {
        val agendamentos = repository.buscarAgendamentosCancelados()
        return agendamentos.map { agendamento ->
            AgendamentoMapper.toDto(agendamento)
        }
    }


    /*fun obterHorariosDisponiveis(idServico: Int, data: LocalDate): List<HorarioDisponivelDto>? {

        val servico = servicoRepository.findById(idServico).orElseThrow {
            ServicoNaoEcontradoException("Serviço com ID $idServico não encontrado.")
        }

        val funcionariosComCompetencia = funcionarioCompetenciaRepository
            .findAllByServicoId(idServico)
            .map { it.funcionario }
            .filter { it.ativo }

        val idsFuncionarios = funcionariosComCompetencia.map { it.id }


//        COMENTADO APENAS POR TESTE
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


        val horariosOcupados = repository.buscarHorariosOcupados(data, idsFuncionarios)


        return funcionamento.inicio?.let {
            funcionamento.fim?.let { it1 ->
                gerarHorariosDisponiveis(
                    horariosOcupados,
                    it, it1, 60L, duracao, funcionariosComCompetencia
                )
            }
        }
    }


    private fun gerarHorariosDisponiveis(
        horariosOcupados: List<HorariosOcupadosDto>,
        abertura: LocalTime,
        fechamento: LocalTime,
        intervaloMinutos: Long,
        duracaoServicoMinutos: Long,
        funcionariosComCompetencia: List<Usuario>
    ): List<HorarioDisponivelDto> {
        val horariosDisponiveis = mutableListOf<HorarioDisponivelDto>()

        var horarioAtual = abertura

        while (horarioAtual.plusMinutes(duracaoServicoMinutos) <= fechamento) {
            val fimHorarioAtual = horarioAtual.plusMinutes(duracaoServicoMinutos)

            val todosFuncionariosOcupados = funcionariosComCompetencia.all { funcionario ->
                horariosOcupados.any { ocupado ->
                    val inicioOcupado = ocupado.getInicio()
                    val fimOcupado = ocupado.getFim()

                    // Só considera conflitos do mesmo funcionário
                    ocupado.getFuncionarioId() == funcionario.id &&
                            horarioAtual.isBefore(fimOcupado) && fimHorarioAtual.isAfter(inicioOcupado)
                }
            }

            if (!todosFuncionariosOcupados) {
                horariosDisponiveis.add(
                    HorarioDisponivelDto(horario = horarioAtual.toString())
                )
            }

            horarioAtual = horarioAtual.plusMinutes(intervaloMinutos)
        }


        return horariosDisponiveis
    }
*/




/*

HORARIO DISPONIVEL OTIMIZADO MAS SIMULTANEO NÃO FUNCIONA

    fun obterHorariosDisponiveis(idServico: Int, data: LocalDate): List<HorarioDisponivelDto> {
        val servico = servicoRepository.findById(idServico)
            .orElseThrow { ServicoNaoEcontradoException("Serviço com ID $idServico não encontrado.") }

        val funcionarios = funcionarioCompetenciaRepository
            .findAllByServicoId(idServico)
            .map { it.funcionario }
            .filter { it.ativo }

        if (funcionarios.isEmpty()) return emptyList()

        val duracaoMinutos = servico.tempo?.toSecondOfDay()?.div(60)?.toLong()
            ?: throw IllegalArgumentException("Serviço sem duração definida.")

        val horariosValidos = mutableSetOf<LocalTime>()

        val diaDaSemana: DayOfWeek = data.dayOfWeek

        val diaSemanaEnum: DiaSemana = DiaSemana.valueOf(diaDaSemana.name)

        funcionarios.forEach { funcionario ->
            // 1 - Verificar se existe exceção para o funcionário
            val excecao = horarioExcecaoRepository.findExcecaoPorData(data, funcionario.id!!)
            val funcionamento = funcionamentoRepository.findByFuncionarioAndDiaSemana(
                funcionario.id!!, DiaSemana.valueOf(data.dayOfWeek.name)
            )

            val inicio = excecao?.inicio ?: funcionamento?.inicio
            val fim = excecao?.fim ?: funcionamento?.fim
            val aberto = excecao?.aberto ?: funcionamento?.aberto
            val capacidade = excecao?.capacidade ?: funcionamento?.capacidade ?: 1

            if (inicio == null || fim == null || aberto != true) return@forEach

            // 2 - Buscar agendamentos existentes do funcionário para o dia
            val agendamentos = repository.buscarHorariosOcupadosPorFuncionario(data, funcionario.id)

            // 3 - Gerar possíveis horários a cada 60 minutos
            var horarioAtual = inicio
            if (horarioAtual != null) {
                while (horarioAtual != null && horarioAtual.plusMinutes(duracaoMinutos) <= fim) {
                    horarioAtual?.let { atual ->
                        val fimHorario = atual.plusMinutes(duracaoMinutos)

                        val conflitos = agendamentos.count { ag ->
                            atual.isBefore(ag.fim) && fimHorario.isAfter(ag.inicio)
                        }

                        val podeAgendar = if (servico.simultaneo == true) {
                            conflitos < capacidade
                        } else {
                            conflitos == 0
                        }

                        if (podeAgendar) {
                            horariosValidos.add(atual)
                        }

                        horarioAtual = atual.plusMinutes(60)
                    }
                }

            }
        }

        return horariosValidos.sorted().map { HorarioDisponivelDto(it.toString().substring(0, 5)) }
    }

    */

    fun obterHorariosDisponiveis(idServico: Int, data: LocalDate): List<HorarioDisponivelDto> {
        val servico = servicoRepository.findById(idServico)
            .orElseThrow { ServicoNaoEcontradoException("Serviço com ID $idServico não encontrado.") }

        val funcionarios = funcionarioCompetenciaRepository
            .findAllByServicoId(idServico)
            .map { it.funcionario }
            .filter { it.ativo }

        if (funcionarios.isEmpty()) return emptyList()

        val duracaoMinutos = servico.tempo?.toSecondOfDay()?.div(60)?.toLong()
            ?: throw IllegalArgumentException("Serviço sem duração definida.")

        val horariosValidos = mutableSetOf<LocalTime>()
        val diaSemanaEnum = DiaSemana.valueOf(data.dayOfWeek.name)

        funcionarios.forEach { funcionario ->
            val excecao = horarioExcecaoRepository.findExcecaoPorData(data, funcionario.id!!)
            val funcionamento = funcionamentoRepository.findByFuncionarioAndDiaSemana(funcionario.id!!, diaSemanaEnum)

            val inicio = excecao?.inicio ?: funcionamento?.inicio
            val fim = excecao?.fim ?: funcionamento?.fim
            val aberto = excecao?.aberto ?: funcionamento?.aberto
            val capacidade = excecao?.capacidade ?: funcionamento?.capacidade ?: 1

            if (inicio != null && fim != null && aberto == true) {
                val agendamentos = repository.buscarHorariosOcupadosPorFuncionario(data, funcionario.id)
                var horarioAtual: LocalTime = inicio

                while (horarioAtual.plusMinutes(duracaoMinutos) <= fim) {
                    val fimHorario = horarioAtual.plusMinutes(duracaoMinutos)

                    val conflitos = agendamentos.filter { ag ->
                        horarioAtual.isBefore(ag.fim) && fimHorario.isAfter(ag.inicio)
                    }

                    val podeAgendar = when {
                        // já existe agendamento de serviço NÃO simultâneo → bloqueia
                        conflitos.any { it.servico?.simultaneo != true } -> false

                        // serviço novo é simultâneo → respeitar capacidade
                        servico.simultaneo == true -> conflitos.size < capacidade

                        // serviço novo não é simultâneo → só se não houver conflito
                        else -> conflitos.isEmpty()
                    }

                    if (podeAgendar) {
                        horariosValidos.add(horarioAtual)
                    }

                    horarioAtual = horarioAtual.plusMinutes(60) // step fixo de 60min
                }
            }
        }

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return horariosValidos.sorted()
            .map { HorarioDisponivelDto(it.format(formatter)) }
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