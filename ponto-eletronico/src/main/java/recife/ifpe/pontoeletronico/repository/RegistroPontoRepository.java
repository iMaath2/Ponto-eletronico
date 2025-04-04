package recife.ifpe.pontoeletronico.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import recife.ifpe.pontoeletronico.model.RegistroPonto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RegistroPontoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Integer funcionarioId, LocalDateTime dataHoraEntrada) {
        String sql = "INSERT INTO registros_ponto (funcionario_id, data_hora_entrada) VALUES (?, ?)";
        jdbcTemplate.update(sql, funcionarioId, Timestamp.valueOf(dataHoraEntrada));
    }

    public List<RegistroPonto> buscarPorFuncionarioId(Integer funcionarioId) {
        String sql = "SELECT id, funcionario_id, data_hora_entrada FROM registros_ponto WHERE funcionario_id = ? ORDER BY data_hora_entrada DESC";
        List<RegistroPonto> registros = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Timestamp timestamp = rs.getTimestamp("data_hora_entrada");
            LocalDateTime dataHora = (timestamp != null) ? timestamp.toLocalDateTime() : null;
            return new RegistroPonto(
                rs.getInt("id"),
                rs.getInt("funcionario_id"),
                dataHora
            );
        }, funcionarioId); 
        return registros;
    }
}