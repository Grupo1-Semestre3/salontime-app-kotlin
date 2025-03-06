package sptech.salonTime

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/clientes")
class ClientesController {

    val clientes = mutableListOf<Clientes>()


    @GetMapping
    fun lista(@RequestParam(required = false) email:String?,
              @RequestParam(required = false) senha:String?): ResponseEntity<List<Clientes>> {
        if (email == null && senha == null) {
            if (clientes.isEmpty()) {
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(200).body(clientes)
        }

        val filtraEmail = email != null
        val filtraSenha = senha != null

        val listaFiltrada = mutableListOf<Clientes>()

        if (filtraEmail && filtraSenha) {
            listaFiltrada.addAll(clientes.filter {
                it.email == email || it.senha == senha
            })
        }
        else if (filtraEmail) {
           listaFiltrada.addAll(clientes.filter{ it.email == email })
        } else {
            listaFiltrada.addAll(clientes.filter { it.senha!! == senha!! })
        }

        if (listaFiltrada.isEmpty()) {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(200).body(listaFiltrada)

    }

    @PostMapping("/{nome}/{cpf}/{email}/{senha}")
    fun criar(@PathVariable nome:String, @PathVariable cpf:String,
              @PathVariable email: String,@PathVariable senha:String):String {
        val novosClientes = Clientes(nome, cpf, email, senha)
        clientes.add(novosClientes)
        return "Cliente cadastrado com sucesso"
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id:Int):String {
        clientes.removeAt(id)
        return "Cliente excluído com sucesso"
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id:Int, @RequestBody clienteAtualizado: Clientes):String {
        clientes[id] = clienteAtualizado
        return "Cliente atualizado com sucesso!"
    }

}