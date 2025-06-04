package sptech.salonTime.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.http.ResponseEntity
import sptech.salonTime.dto.AgendamentoDto
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.dto.UsuarioPublicoDto
import sptech.salonTime.entidade.*
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.StatusAgendamentoRepository
import sptech.salonTime.service.AgendamentoService
import java.time.LocalDate
import java.time.LocalTime

class AgendamentoControllerTest {

    val repository = mock(AgendamentoRepository::class.java)
    val service = mock(AgendamentoService::class.java)
    val statusAgendamentoRepository = mock(StatusAgendamentoRepository::class.java)
    val controller = AgendamentoController(repository, statusAgendamentoRepository, service)


    lateinit var agendamento: Agendamento

    lateinit var agendamentoDto: AgendamentoDto

    @BeforeEach
    fun setup() {
        agendamento = Agendamento(
            id = 1,
            servico = Servico(1, "Corte de Cabelo", 30.0, LocalTime.of(10, 0,0), null),
            usuario = Usuario(1, TipoUsuario(1, "cliente"), "João", "joao@email.com", "joao123", "senha", null, null),
            funcionario = Usuario(2, TipoUsuario(2, "funcionario"), "Maria", "maria@email.com", "maria123", "senha", null, null),
            statusAgendamento = StatusAgendamento(1, "Confirmado"),
            pagamento = Pagamento(1, "Cartão"),
            cupom = Cupom(1, "DESCONTO10", "10% de desconto", "codiguin", true),
            data = LocalDate.now(),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 50.0
        )

        agendamentoDto = AgendamentoDto(
            id = 1,
            servico = agendamento.servico,
            usuario = UsuarioPublicoDto(
                id = agendamento.usuario?.id,
                tipoUsuario = agendamento.usuario?.tipoUsuario,
                nome = agendamento.usuario?.nome,
                email = agendamento.usuario?.email,
                login = agendamento.usuario?.login
            ),
            funcionario = UsuarioPublicoDto(
                id = agendamento.funcionario?.id,
                tipoUsuario = agendamento.funcionario?.tipoUsuario,
                nome = agendamento.funcionario?.nome,
                email = agendamento.funcionario?.email,
                login = agendamento.funcionario?.login
            ),
            statusAgendamento = agendamento.statusAgendamento,
            pagamento = agendamento.pagamento,
            data = agendamento.data.toString(),
            inicio = agendamento.inicio.toString(),
            fim = agendamento.fim.toString(),
            cupom = agendamento.cupom,
            preco = agendamento.preco
        )
    }

    @Test
    @DisplayName("Consulta todos os agendamentos")
    fun get() {

        Mockito.`when`(service.listar()).thenReturn(
            listOf(agendamentoDto)
        )

        val response: ResponseEntity<List<AgendamentoDto?>> = controller.get()


        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)


    }

    @Test
    @DisplayName("Consulta agendamento por id")
    fun getById() {

        Mockito.`when`(agendamento.id?.let { service.listarPorId(it) }).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto>? = agendamento.id?.let { controller.getById(it) }


        if (response != null) {
            assertEquals(200, response.statusCode.value())
            assertEquals(agendamento.id, response.body?.id)
        }

    }

    @Test
    @DisplayName("Cadastra um agendamento")
    fun post() {

        val dto = CadastroAgendamentoDto(
            usuario = 1,
            servico = 1,
            funcionario = 2,
            statusAgendamento = 1,
            pagamento = 1,
            data = LocalDate.now(),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 50.0
        )

        Mockito.`when`(service.cadastrar(dto)).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto> = controller.post(dto)

         assertEquals(201, response.statusCode.value())
         assertNotNull(response.body?.id)

    }

    @Test
    @DisplayName("Atualiza um atributo de um agendamento")
    fun patch() {

        val id = 1
        val atributo = "statusAgendamento"
        val novoValor = "Cancelado"

        Mockito.`when`(service.atualizarAtributo(id, atributo, novoValor)).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto> = controller.patch(id, atributo, novoValor)

        assertEquals(200, response.statusCode.value())
        assertEquals(agendamento.id, response.body?.id)

    }

    @Test
    @DisplayName("Consulta próximos agendamentos por funcionario")
    fun getProximosAgendamentos() {
     Mockito.`when`(service.buscarProximosAgendamentosPorFuncionario(2)).thenReturn(
      listOf(agendamentoDto)
     )

     val response: ResponseEntity<List<AgendamentoDto>> = controller.getProximosAgendamentos(2)


     assertEquals(200, response.statusCode.value())
     assertEquals(1, response.body?.size)
    }

    @Test
    @DisplayName("Consulta próximos agendamentos por usuario")
    fun getProximosAgendamentosPorUsuario() {
        Mockito.`when`(service.buscarProximosAgendamentosPorUsuario(1)).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto> = controller.getProximosAgendamentosPorUsuario(1)


        assertEquals(200, response.statusCode.value())
        assertEquals(agendamentoDto, response.body)
    }

    @Test
    @DisplayName("Consulta agendamentos passados por usuario")
    fun getAgendamentosPassadosPorUsuario() {
        Mockito.`when`(service.buscarAgendamentosPassadosPorUsuario(1)).thenReturn(
            listOf(agendamentoDto)
        )

        val response: ResponseEntity<List<AgendamentoDto>> = controller.getAgendamentosPassadosPorUsuario(1)
        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    @Test
    @DisplayName("Consulta agendamentos passados por funcionario")
    fun getAgendamentosPassadosPorFuncionario() {
        Mockito.`when`(service.buscarAgendamentosPassadosPorFuncionario(2)).thenReturn(
            listOf(agendamentoDto)
        )

        val response: ResponseEntity<List<AgendamentoDto>> = controller.getAgendamentosPassadosPorFuncionario(2)
        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    @Test
    @DisplayName("Atualiza o status de um agendamento")
    fun patchStatus() {
        val id = 1
        val status = 2

        Mockito.`when`(service.atualizarStatus(id, status)).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto> = controller.patchStatus(id, status)

        assertEquals(200, response.statusCode.value())
        assertEquals(agendamento.id, response.body?.id)
    }

    @Test
    @DisplayName("Atualiza o valor de um agendamento")
    fun patchValor() {
        val id = 1
        val valor = 60.0

        Mockito.`when`(service.atualizarValor(id, valor)).thenReturn(agendamentoDto)

        val response: ResponseEntity<AgendamentoDto> = controller.patchValor(id, valor)

        assertEquals(200, response.statusCode.value())
        assertEquals(agendamento.id, response.body?.id)
    }

    @Test
    @DisplayName("Consulta agendamentos cancelados")
    fun getAgendamentosCancelados() {
        Mockito.`when`(service.buscarAgendamentosCancelados()).thenReturn(
            listOf(agendamentoDto)
        )

        val response: ResponseEntity<List<AgendamentoDto>> = controller.getAgendamentosCancelados()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }
}