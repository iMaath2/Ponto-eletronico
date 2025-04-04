package recife.ifpe.pontoeletronico.model;

import java.time.LocalDateTime;

public class RegistroPonto {
    private Integer id;
    private Integer funcionarioId; 
    private LocalDateTime dataHoraEntrada;

    public RegistroPonto() {
    }

    public RegistroPonto(Integer id, Integer funcionarioId, LocalDateTime dataHoraEntrada) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }
}