package recife.ifpe.pontoeletronico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import recife.ifpe.pontoeletronico.model.Funcionario;
import recife.ifpe.pontoeletronico.repository.FuncionarioRepository;

import java.util.List;

@Service 
public class FuncionarioService {

    @Autowired 
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> buscarTodos() {
        return funcionarioRepository.buscarTodos();
    }

    public Funcionario buscarPorId(Integer id) {
        return funcionarioRepository.buscarPorId(id);
    }
    
    public Funcionario salvar(Funcionario funcionario) {
        
        if (funcionario == null || funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do funcionário não pode ser vazio.");
        }        
        return funcionarioRepository.salvar(funcionario);
    }

}