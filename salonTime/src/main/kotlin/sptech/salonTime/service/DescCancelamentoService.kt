package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.dto.NovaDescricaoCancelamentoDto
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.exception.AgendamentoNaoEncontradoException
import sptech.salonTime.exception.DescCancelamentoNaoEncontradoException
import sptech.salonTime.mapper.AvaliacaoMapper
import sptech.salonTime.mapper.DescCancelamentoMapper
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.DescCancelamentoRepository

@Service
class DescCancelamentoService (var repository: DescCancelamentoRepository, val agendamentoRepository: AgendamentoRepository) {

    fun listar(): List<DescCancelamentoDto> {
        var cancelamento = repository.findAll()

        return cancelamento.map{ DescCancelamentoMapper.toDTO(it)!! }
    }

    fun listarPorId(id: Int): DescCancelamentoDto? {
        var cancelamentoAchado = repository.findById(id).orElseThrow{DescCancelamentoNaoEncontradoException("Cancelamento não encontrado.")}

        return DescCancelamentoMapper.toDTO(cancelamentoAchado)
    }

    fun criar(descCancelamento: DescCancelamento): DescCancelamentoDto? {

        var agendamento = descCancelamento.agendamento?.id?.let { agendamentoRepository.findById(it).orElseThrow { AgendamentoNaoEncontradoException("Agendamento não encontrado") } }


        val descCancelamentoSalvo = repository.save(descCancelamento)

        return DescCancelamentoMapper.toDTO(descCancelamentoSalvo)

    }

    fun atualizar(id: Int, descCancelamento: DescCancelamento): DescCancelamentoDto? {
       var cancelamento = repository.findById(id).orElseThrow { DescCancelamentoNaoEncontradoException("Cancelamento não encontrado") }

        descCancelamento.id = id
        repository.save(descCancelamento)
        return DescCancelamentoMapper.toDTO(descCancelamento)

    }

    fun atualizarDescricao(id: Int, novaDescricao: NovaDescricaoCancelamentoDto): DescCancelamentoDto? {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            cancelamento.descricao = novaDescricao.descricao
            var cancelamentoSalvo = repository.save(cancelamento)

            return DescCancelamentoMapper.toDTO(cancelamentoSalvo)

        } else {
            return null
        }
    }

    fun deletar(id: Int) {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            repository.delete(cancelamento)
        }
    }
}