package recife.ifpe.pontoeletronico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional; // Opcional

import recife.ifpe.pontoeletronico.model.Funcionario;
import recife.ifpe.pontoeletronico.model.RegistroPonto;
import recife.ifpe.pontoeletronico.repository.FuncionarioRepository;
import recife.ifpe.pontoeletronico.repository.RegistroPontoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroPontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository; 

 
    public void registrarEntrada(Integer funcionarioId) {
        Funcionario funcionario = funcionarioRepository.buscarPorId(funcionarioId);
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário com ID " + funcionarioId + " não encontrado.");
        }

        LocalDateTime agora = LocalDateTime.now();
        registroPontoRepository.salvar(funcionarioId, agora);
    }

    public List<RegistroPonto> buscarRegistrosPorFuncionario(Integer funcionarioId) {
         Funcionario funcionario = funcionarioRepository.buscarPorId(funcionarioId);
         if (funcionario == null && funcionarioId != null) {
              throw new IllegalArgumentException("Funcionário com ID " + funcionarioId + " não encontrado ao buscar registros.");
         }
        return registroPontoRepository.buscarPorFuncionarioId(funcionarioId);
    }
}