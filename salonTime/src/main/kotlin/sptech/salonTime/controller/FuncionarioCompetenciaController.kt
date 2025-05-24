package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.FuncionarioCompetenciaDto
import sptech.salonTime.entidade.FuncionarioCompetencia
import sptech.salonTime.service.FuncionarioCompetenciaService

@RestController
@RequestMapping("/funcionario-competencia")
class FuncionarioCompetenciaController (val service: FuncionarioCompetenciaService) {

    @GetMapping
    fun listar(): ResponseEntity<List<FuncionarioCompetencia>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/servico/{id}")
    fun listarPorServico(@PathVariable id: Int): ResponseEntity<List<FuncionarioCompetenciaDto>> {
        return ResponseEntity.status(200).body(service.listarPorServico(id))
    }

    @PostMapping
    fun inserir(@RequestBody funcionarioCompetencia: FuncionarioCompetencia): ResponseEntity<FuncionarioCompetencia>{
        return ResponseEntity.status(201).body(service.salvar(funcionarioCompetencia))
    }

    @PutMapping("/{id}")
    fun editar(@PathVariable id: Int, @RequestBody funcionarioCompetencia: FuncionarioCompetencia): ResponseEntity<FuncionarioCompetencia>{
        return ResponseEntity.status(200).body(service.editar(id, funcionarioCompetencia))
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void>{
        service.deletar(id)
        return ResponseEntity.status(204).build()
    }

}