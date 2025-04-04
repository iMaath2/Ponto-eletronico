document.addEventListener('DOMContentLoaded', () => {
    buscarFuncionarios();
});

const areaMensagem = document.getElementById('area-mensagem');

function exibirMensagem(texto, tipo = 'info') {
    areaMensagem.textContent = texto;
    areaMensagem.className = 'mensagem'; 
    if (tipo === 'sucesso') {
        areaMensagem.classList.add('mensagem-sucesso');
    } else if (tipo === 'erro') {
        areaMensagem.classList.add('mensagem-erro');
    } else {
         areaMensagem.classList.add('mensagem-info');
    }
    areaMensagem.style.display = 'block'; 

    setTimeout(() => {
        areaMensagem.textContent = '';
        areaMensagem.style.display = 'none'; 
        areaMensagem.className = 'mensagem'; 
    }, 5000);
}

async function buscarFuncionarios() {
    const container = document.getElementById('cartoes-funcionarios');
    container.innerHTML = '<p>Carregando funcionários...</p>'; 
    areaMensagem.style.display = 'none'; 

    try {
        const response = await fetch('/api/funcionarios');

        if (!response.ok) {
            let erroMsg = `Erro HTTP: ${response.status}`;
            try {
                const erroData = await response.json();
                erroMsg = erroData.message || erroMsg; 
            } catch (jsonError) {
            }
            throw new Error(erroMsg);
        }

        const funcionarios = await response.json();
        container.innerHTML = ''; 

        if (!funcionarios || funcionarios.length === 0) {
            container.innerHTML = '<p>Nenhum funcionário cadastrado.</p>';
            return;
        }

        funcionarios.forEach(funcionario => {
            const card = document.createElement('div');
            card.className = 'cartao-funcionario';
            card.textContent = funcionario.nome;
            card.dataset.funcionarioId = funcionario.id; 
            card.addEventListener('click', tratarCliqueCartao);
            container.appendChild(card);
        });

    } catch (error) {
        console.error('Erro ao buscar funcionários:', error);
        container.innerHTML = '<p>Falha ao carregar funcionários.</p>';
        exibirMensagem(`Falha ao carregar funcionários: ${error.message}`, 'erro');
    }
}

async function tratarCliqueCartao(evento) {
    const card = evento.currentTarget;
    const funcionarioId = card.dataset.funcionarioId;
    const nomeFuncionario = card.textContent; 

    card.style.opacity = '0.7';
    card.style.pointerEvents = 'none'; 

    exibirMensagem(`Registrando ponto para ${nomeFuncionario}...`, 'info');

    try {
        const response = await fetch(`/api/registros/entrada/${funcionarioId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            let erroMsg = `Erro HTTP: ${response.status}`;
             try {
                 const erroData = await response.json();
                 erroMsg = erroData.message || `Funcionário não encontrado ou erro no servidor.`;
             } catch (jsonError) {}
            throw new Error(erroMsg);
        }

        exibirMensagem(`Ponto registrado para ${nomeFuncionario} com sucesso!`, 'sucesso');
        console.log(`Ponto registrado para funcionário ID: ${funcionarioId}`);

    } catch (error) {
        console.error('Erro ao registrar ponto:', error);
        exibirMensagem(`Falha ao registrar ponto para ${nomeFuncionario}. (${error.message})`, 'erro');
    } finally {
         card.style.opacity = '1';
         card.style.pointerEvents = 'auto';
    }
}