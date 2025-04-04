document.addEventListener('DOMContentLoaded', () => {
    buscarFuncionariosParaSelecao();
});

const selectElement = document.getElementById('selecao-funcionario');
const areaMensagemConsulta = document.getElementById('area-mensagem-consulta');
const registrosContainer = document.getElementById('exibicao-registros');

function exibirMensagemConsulta(texto, tipo = 'info') {
    areaMensagemConsulta.textContent = texto;
    areaMensagemConsulta.className = 'mensagem';
    if (tipo === 'sucesso') {
        areaMensagemConsulta.classList.add('mensagem-sucesso');
    } else if (tipo === 'erro') {
        areaMensagemConsulta.classList.add('mensagem-erro');
    } else {
        areaMensagemConsulta.classList.add('mensagem-info');
    }
     areaMensagemConsulta.style.display = 'block';

     if (tipo !== 'info') {
         setTimeout(() => {
             areaMensagemConsulta.textContent = '';
              areaMensagemConsulta.style.display = 'none';
              areaMensagemConsulta.className = 'mensagem';
         }, 7000);
     }
}

async function buscarFuncionariosParaSelecao() {
    selectElement.disabled = true;
    selectElement.innerHTML = '<option value="">Carregando...</option>';
    registrosContainer.innerHTML = '<p>Selecione um funcionário para ver os registros.</p>'; 
    areaMensagemConsulta.style.display = 'none';

    try {
        const response = await fetch('/api/funcionarios');
        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }
        const funcionarios = await response.json();

        selectElement.innerHTML = '<option value="">-- Selecione um Funcionário --</option>'; 

        if (!funcionarios || funcionarios.length === 0) {
             selectElement.innerHTML = '<option value="">Nenhum funcionário encontrado</option>';
            return; 
        }

        funcionarios.sort((a, b) => a.nome.localeCompare(b.nome));

        funcionarios.forEach(funcionario => {
            const option = document.createElement('option');
            option.value = funcionario.id;
            option.textContent = funcionario.nome;
            selectElement.appendChild(option);
        });

        selectElement.addEventListener('change', tratarSelecaoFuncionario);

    } catch (error) {
        console.error('Erro ao buscar funcionários para seleção:', error);
        selectElement.innerHTML = '<option value="">Falha ao carregar</option>';
        exibirMensagemConsulta(`Falha ao carregar a lista de funcionários: ${error.message}`, 'erro');
    } finally {
        selectElement.disabled = false; 
    }
}

function tratarSelecaoFuncionario(evento) {
    const funcionarioId = evento.target.value;
    areaMensagemConsulta.style.display = 'none'; 

    if (funcionarioId) {
        registrosContainer.innerHTML = '<p>Buscando registros...</p>'; 
        buscarRegistrosPorFuncionario(funcionarioId);
    } else {
        registrosContainer.innerHTML = '<p>Selecione um funcionário para ver os registros.</p>';
    }
}

async function buscarRegistrosPorFuncionario(funcionarioId) {
    try {
        const response = await fetch(`/api/registros/funcionario/${funcionarioId}`);
        if (!response.ok) {
             let erroMsg = `Erro HTTP: ${response.status}`;
             try {
                 const erroData = await response.json();
                 erroMsg = erroData.message || `Funcionário não encontrado ou erro ao buscar registros.`;
             } catch (jsonError) {}
             throw new Error(erroMsg);
        }
        const registros = await response.json();

        registrosContainer.innerHTML = ''; 

        if (!registros || registros.length === 0) {
            registrosContainer.innerHTML = '<p>Nenhum registro de ponto encontrado para este funcionário.</p>';
            return;
        }

        const lista = document.createElement('ul');
        registros.forEach(registro => {
            const item = document.createElement('li');
            let dataHoraFormatada = 'Data inválida';
            if (registro.dataHoraEntrada) {
                 try {
                    const dataHora = new Date(registro.dataHoraEntrada);
                    if (!isNaN(dataHora.getTime())) {
                         const formatador = new Intl.DateTimeFormat('pt-BR', {
                            year: 'numeric', month: '2-digit', day: '2-digit',
                            hour: '2-digit', minute: '2-digit', second: '2-digit',
                            hour12: false 
                        });
                        dataHoraFormatada = formatador.format(dataHora);
                    }
                 } catch (e) {
                    console.error("Erro ao formatar data:", registro.dataHoraEntrada, e);
                 }
            }
            item.textContent = `Entrada registrada em: ${dataHoraFormatada}`;
            lista.appendChild(item);
        });
        registrosContainer.appendChild(lista);

    } catch (error) {
        console.error('Erro ao buscar registros:', error);
        registrosContainer.innerHTML = `<p style="color: red;">Falha ao buscar registros: ${error.message}</p>`;
    }
}