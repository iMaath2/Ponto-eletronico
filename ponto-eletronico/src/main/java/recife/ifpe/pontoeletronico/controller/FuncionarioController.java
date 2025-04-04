package recife.ifpe.pontoeletronico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import recife.ifpe.pontoeletronico.model.Funcionario;
import recife.ifpe.pontoeletronico.service.FuncionarioService;

import java.net.URI;
import java.util.List;

@RestController 
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired 
    private FuncionarioService funcionarioService;
   
    @GetMapping
    public ResponseEntity<List<Funcionario>> buscarTodosFuncionarios() {
         try {
            List<Funcionario> funcionarios = funcionarioService.buscarTodos();
            return ResponseEntity.ok(funcionarios);
        } catch (Exception e) { 
             System.err.println("Erro interno ao buscar funcionários: " + e.getMessage()); 
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar funcionários", e);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable Integer id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        if (funcionario != null) {
            return ResponseEntity.ok(funcionario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody Funcionario novoFuncionario) {
         try {
            if (novoFuncionario == null || novoFuncionario.getNome() == null || novoFuncionario.getNome().isBlank()) {

                 return ResponseEntity.badRequest().body(null);
            }
            Funcionario funcionarioParaSalvar = new Funcionario(null, novoFuncionario.getNome());
            Funcionario funcionarioSalvo = funcionarioService.salvar(funcionarioParaSalvar);

            if (funcionarioSalvo.getId() != null) {
                 URI location = URI.create(String.format("/api/funcionarios/%d", funcionarioSalvo.getId()));
                 return ResponseEntity.created(location).body(funcionarioSalvo);
            } else {
                 System.err.println("Controller: ID do funcionário salvo não retornado pelo serviço.");
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

         } catch (IllegalArgumentException e) {
             System.err.println("Erro de validação ao criar funcionário: " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
         } catch (Exception e) {
             System.err.println("Erro interno ao criar funcionário: " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao criar funcionário", e);
         }
    }
}