package sptech.salonTime.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.http.ResponseEntity
import sptech.salonTime.dto.CadastroAgendamentoDto
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

    @BeforeEach
    fun setup() {
        agendamento = Agendamento(
            id = 1,
            servico = Servico(id = 1, nome = "Corte", tempo = LocalTime.of(0, 30), status = "ATIVO", preco = 50.0),
            usuario = Usuario(
                id = 1,
                tipoUsuario = TipoUsuario(),
                nome = "Cliente",
                email = "cliente@email.com",
                login = false
            ),
            funcionario = Usuario(id = 2, tipoUsuario = TipoUsuario(), nome = "Funcionário", email = "func@email.com"),
            statusAgendamento = StatusAgendamento(id = 1, status = "Agendado"),
            pagamento = Pagamento(id = 1, forma = "Cartão", taxa = 0.0),
            data = LocalDate.now(),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 50.0
        )
    }

    @Test
    @DisplayName("Consulta todos os agendamentos")
    fun get() {

        Mockito.`when`(service.listar()).thenReturn(
            mutableListOf(mock(Agendamento::class.java))
        )

        val response: ResponseEntity<List<Agendamento?>> = controller.get()


        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)


    }

    @Test
    @DisplayName("Consulta agendamento por id")
    fun getById() {

        Mockito.`when`(agendamento.id?.let { service.listarPorId(it) }).thenReturn(agendamento)

        val response: ResponseEntity<Agendamento>? = agendamento.id?.let { controller.getById(it) }


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

        Mockito.`when`(service.cadastrar(dto)).thenReturn(mock(Agendamento::class.java))

        val response: ResponseEntity<Agendamento> = controller.post(dto)

         assertEquals(201, response.statusCode.value())
         assertNotNull(response.body?.id)

    }

    @Test
    @DisplayName("Atualiza um atributo de um agendamento")
    fun patch() {

        val id = 1
        val atributo = "statusAgendamento"
        val novoValor = "Cancelado"

        Mockito.`when`(service.atualizarAtributo(id, atributo, novoValor)).thenReturn(agendamento)

        val response: ResponseEntity<Agendamento> = controller.patch(id, atributo, novoValor)

        assertEquals(200, response.statusCode.value())
        assertEquals(agendamento.id, response.body?.id)

    }

    @Test
    @DisplayName("Consulta próximos agendamentos")
    fun getProximosAgendamentos() {
     Mockito.`when`(service.buscarProximosAgendamentos()).thenReturn(
      mutableListOf(mock(Agendamento::class.java))
     )

     val response: ResponseEntity<List<Agendamento>> = controller.getProximosAgendamentos()


     assertEquals(200, response.statusCode.value())
     assertEquals(1, response.body?.size)
    }

}