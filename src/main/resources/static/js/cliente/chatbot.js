
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

function sendMessage() {
    const input = document.getElementById('chatbot-input');
    const message = input.value.trim();
    if (message === '') return;

    const messagesContainer = document.getElementById('chatbot-messages');
    const userMessage = document.createElement('div');
    userMessage.textContent = message;
    userMessage.classList.add('chatbot-message', 'user-message');
    messagesContainer.appendChild(userMessage);

    setTimeout(() => {
        const botMessage = document.createElement('div');
        botMessage.textContent = getBotResponse(message);
        botMessage.classList.add('chatbot-message', 'bot-message');
        messagesContainer.appendChild(botMessage);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }, 1000);

    input.value = '';
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function getBotResponse(userMessage) {
    return "Esta é uma mensagem de teste!";
}

// Executa quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', createChatbot);

