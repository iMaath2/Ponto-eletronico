package recife.ifpe.pontoeletronico.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import recife.ifpe.pontoeletronico.model.Funcionario;

import java.util.List;

@Repository 
public class FuncionarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Funcionario> buscarTodos() {
        String sql = "SELECT id, nome FROM funcionarios ORDER BY nome";
        List<Funcionario> funcionarios = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome")
                )
        );
        return funcionarios;
    }

    public Funcionario buscarPorId(Integer id) {
        String sql = "SELECT id, nome FROM funcionarios WHERE id = ?";
        try {
            Funcionario funcionario = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new Funcionario(
                            rs.getInt("id"),
                            rs.getString("nome")
                    ),
                id); 
            return funcionario;
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public Funcionario salvar(Funcionario funcionario) {
        String sqlInsert = "INSERT INTO funcionarios (nome) VALUES (?)";
        jdbcTemplate.update(sqlInsert, funcionario.getNome());
        
        String sqlSelectId = "SELECT id FROM funcionarios WHERE nome = ? ORDER BY id DESC LIMIT 1";
        try {
            Integer idRecemCriado = jdbcTemplate.queryForObject(sqlSelectId, Integer.class, funcionario.getNome());
            funcionario.setId(idRecemCriado); 
            return funcionario;
        } catch (EmptyResultDataAccessException e) {

            System.err.println("Erro crítico: Não foi possível encontrar o ID do funcionário recém-inserido: " + funcionario.getNome());
            return funcionario; 
        }
    }
}