package sptech.salonTime.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.exception.CupomConfiguracaoException
import sptech.salonTime.repository.CupomConfiguracaoRepository
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith

class CupomConfiguracaoServiceTest {

 private lateinit var repository: CupomConfiguracaoRepository
 private lateinit var service: CupomConfiguracaoService

 private val cupom = CupomConfiguracao(
  id = 1,
  intervaloAtendimento = 10,
  porcentagemDesconto = 15
 )

 @BeforeEach
 fun setup() {
  repository = mock(CupomConfiguracaoRepository::class.java)
  service = CupomConfiguracaoService(repository)
 }

 @Test
 fun `salvar deve persistir novo cupom`() {
  `when`(repository.save(cupom)).thenReturn(cupom)

  val resultado = service.salvar(cupom)

  assertNotNull(resultado)
  assertEquals(15, resultado.porcentagemDesconto)
  verify(repository).save(cupom)
 }

 @Test
 fun `editar deve atualizar cupom existente`() {
  val atualizado = cupom.copy(intervaloAtendimento = 20)
  `when`(repository.existsById(1)).thenReturn(true)
  `when`(repository.save(any(CupomConfiguracao::class.java))).thenReturn(atualizado)

  val resultado = service.editar(1, atualizado)

  assertEquals(20, resultado.intervaloAtendimento)
  verify(repository).save(atualizado)
 }

 @Test
 fun `editar deve lançar exceção se cupom não existir`() {
  `when`(repository.existsById(99)).thenReturn(false)

  val excecao = assertFailsWith<CupomConfiguracaoException> {
   service.editar(99, cupom)
  }

  assertEquals("Cupom de configuração com ID 99 não encontrado.", excecao.message)
 }

 @Test
 fun `editarIntervalo deve alterar apenas o intervalo`() {
  val atualizado = cupom.copy(intervaloAtendimento = 25)
  `when`(repository.findById(1)).thenReturn(Optional.of(cupom))
  `when`(repository.save(any())).thenReturn(atualizado)

  val resultado = service.editarIntervalo(1, 25)

  assertEquals(25, resultado.intervaloAtendimento)
  verify(repository).save(any())
 }

 @Test
 fun `editarPorcentagem deve alterar apenas a porcentagem`() {
  val atualizado = cupom.copy(porcentagemDesconto = 30)
  `when`(repository.findById(1)).thenReturn(Optional.of(cupom))
  `when`(repository.save(any())).thenReturn(atualizado)

  val resultado = service.editarPorcentagem(1, 30)

  assertEquals(30, resultado.porcentagemDesconto)
  verify(repository).save(any())
 }

 @Test
 fun `listarTodos deve retornar lista de cupons`() {
  val lista = listOf(
   CupomConfiguracao(1, 10, 20),
   CupomConfiguracao(2, 15, 25)
  )

  `when`(repository.findAll()).thenReturn(lista)

  val resultado = service.listarTodos()

  assertEquals(2, resultado.size)
  assertEquals(25, resultado[1].porcentagemDesconto)
  verify(repository).findAll()
 }
}
