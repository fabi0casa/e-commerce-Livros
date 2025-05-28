
// Função que insere o HTML do chatbot na página
function createChatbot() {
    const chatbotHTML = `
        <button class="chatbot-button" id="chatbot-toggle">Chatbot</button>
        <div class="chatbot-container" id="chatbot">
            <div class="chatbot-header">Assistente Virtual</div>
            <div class="chatbot-messages" id="chatbot-messages"></div>
            <div class="chatbot-input">
                <input type="text" id="chatbot-input" placeholder="Digite uma mensagem...">
                <button id="chatbot-send">Enviar</button>
            </div>
        </div>
    `;
    const container = document.createElement('div');
    container.innerHTML = chatbotHTML;
    document.body.appendChild(container);

    // Eventos
    document.getElementById('chatbot-toggle').addEventListener('click', toggleChatbot);
    document.getElementById('chatbot-send').addEventListener('click', sendMessage);
    document.getElementById('chatbot-input').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            sendMessage();
        }
    });
}

function toggleChatbot() {
    const chatbot = document.getElementById('chatbot');
    if (chatbot.classList.contains('show')) {
        chatbot.style.opacity = '0';
        chatbot.style.transform = 'scale(0.9)';
        setTimeout(() => chatbot.classList.remove('show'), 300);
    } else {
        chatbot.classList.add('show');
        chatbot.style.opacity = '1';
        chatbot.style.transform = 'scale(1)';
    }
}

async function sendMessage() {
    const input = document.getElementById('chatbot-input');
    const message = input.value.trim();
    if (message === '') return;

    const messagesContainer = document.getElementById('chatbot-messages');

    // Mensagem do usuário
    const userMessage = document.createElement('div');
    userMessage.textContent = message;
    userMessage.classList.add('chatbot-message', 'user-message');
    messagesContainer.appendChild(userMessage);

    // Limpa input e mostra carregando
    input.value = '';
    messagesContainer.scrollTop = messagesContainer.scrollHeight;

    const botMessage = document.createElement('div');
    botMessage.classList.add('chatbot-message', 'bot-message');
    
    const typingIndicator = document.createElement('div');
    typingIndicator.classList.add('typing-indicator');
    typingIndicator.innerHTML = '<span></span><span></span><span></span>';
    
    botMessage.appendChild(typingIndicator);
    messagesContainer.appendChild(botMessage);
    

    // Chamada real ao backend
    const resposta = await getBotResponse(message);
    botMessage.textContent = resposta;

    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

async function getBotResponse(userMessage) {
    try {
        const response = await fetch('/chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ mensagem: userMessage })
        });

        if (!response.ok) {
            throw new Error(`Erro: ${response.status}`);
        }

        const data = await response.json();
        return data.resposta || "Desculpe, não consegui entender sua pergunta.";
    } catch (error) {
        console.error("Erro ao conversar com o chatbot:", error);
        return "Ocorreu um erro ao tentar responder. Tente novamente mais tarde.";
    }
}

function resetChatHistorico() {
    const url = '/chatbot/reset';
    const data = {};  // qualquer payload se quiser

    const blob = new Blob([JSON.stringify(data)], { type: 'application/json' });
    navigator.sendBeacon(url, blob);
}

window.addEventListener('beforeunload', resetChatHistorico);


// Executa quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', createChatbot);

