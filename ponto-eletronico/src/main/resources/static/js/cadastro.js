document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('form-cadastro');
    if (form) {
        form.addEventListener('submit', tratarSubmitCadastro);
    }
});

const areaMensagemCadastro = document.getElementById('area-mensagem-cadastro');

function exibirMensagemCadastro(texto, tipo = 'info') {
    areaMensagemCadastro.textContent = texto;
    areaMensagemCadastro.className = 'mensagem'; 
    if (tipo === 'sucesso') {
        areaMensagemCadastro.classList.add('mensagem-sucesso');
    } else if (tipo === 'erro') {
        areaMensagemCadastro.classList.add('mensagem-erro');
    } else {
        areaMensagemCadastro.classList.add('mensagem-info');
    }
    areaMensagemCadastro.style.display = 'block';

    setTimeout(() => {
        areaMensagemCadastro.textContent = '';
        areaMensagemCadastro.style.display = 'none';
        areaMensagemCadastro.className = 'mensagem';
    }, 6000);
}

async function tratarSubmitCadastro(evento) {
    evento.preventDefault(); 

    const nomeInput = document.getElementById('nome-funcionario');
    const nome = nomeInput.value.trim();
    const submitButton = evento.target.querySelector('button[type="submit"]');

    if (!nome) {
        exibirMensagemCadastro('Por favor, digite o nome do funcionário.', 'erro');
        return;
    }

    submitButton.disabled = true;
    submitButton.textContent = 'Cadastrando...';
    areaMensagemCadastro.style.display = 'none'; 

    const dadosFuncionario = {
        nome: nome
    };

    try {
        const response = await fetch('/api/funcionarios', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dadosFuncionario),
        });

        if (!response.ok) {
            let erroMsg = `Erro HTTP: ${response.status}`;
             try {
                 const erroData = await response.json();
                 erroMsg = erroData.message || `Não foi possível cadastrar o funcionário.`;
             } catch (jsonError) {}

             if(response.status === 400) {
                 erroMsg = `Dados inválidos. Verifique o nome fornecido.`;
             }

            throw new Error(erroMsg);
        }

        const funcionarioCriado = await response.json(); 
        exibirMensagemCadastro(`Funcionário "${funcionarioCriado.nome}" cadastrado com sucesso! (ID: ${funcionarioCriado.id})`, 'sucesso');
        nomeInput.value = ''; 

    } catch (error) {
        console.error('Erro ao cadastrar funcionário:', error);
        exibirMensagemCadastro(`Erro ao cadastrar: ${error.message}`, 'erro');
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = 'Cadastrar';
    }
}