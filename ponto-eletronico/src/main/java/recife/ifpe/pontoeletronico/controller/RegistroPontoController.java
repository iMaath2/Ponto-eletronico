package recife.ifpe.pontoeletronico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import recife.ifpe.pontoeletronico.model.RegistroPonto;
import recife.ifpe.pontoeletronico.service.RegistroPontoService;

import java.util.List;

@RestController
@RequestMapping("/api/registros") 
@CrossOrigin(origins = "*")
public class RegistroPontoController {

    @Autowired 
    private RegistroPontoService registroPontoService;

    @PostMapping("/entrada/{funcionarioId}")
    public ResponseEntity<Void> registrarEntrada(@PathVariable Integer funcionarioId) {
        try {
            registroPontoService.registrarEntrada(funcionarioId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
             System.err.println("Tentativa de registrar ponto para ID inválido: " + funcionarioId + " - " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Erro interno ao registrar entrada para ID " + funcionarioId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao registrar ponto", e);
        }
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<RegistroPonto>> buscarPorFuncionario(@PathVariable Integer funcionarioId) {
        try {
            List<RegistroPonto> registros = registroPontoService.buscarRegistrosPorFuncionario(funcionarioId);
            return ResponseEntity.ok(registros);
        } catch (IllegalArgumentException e) {
             System.err.println("Tentativa de buscar registros para ID inválido: " + funcionarioId + " - " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
             System.err.println("Erro interno ao buscar registros para ID " + funcionarioId + ": " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao buscar registros", e);
        }
    }
}