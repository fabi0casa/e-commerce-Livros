# 📚 E-Commerce de Livros

## Descrição
Este é um sistema de e-commerce voltado para a venda de livros, desenvolvido para a disciplina de Laboratório de Engenharia de Software (LES). O projeto implementa funcionalidades como cadastro de livros e clientes, gerenciamento de vendas, controle de estoque e recomendação personalizada com IA generativa.

## Tecnologias Utilizadas
- **Backend:** Spring Boot (Java)
- **Banco de Dados:** MySQL (hospedado no InfinityFree)
- **Frontend:** HTML, CSS, JavaScript
- **IA Generativa:** Gemini (para chatbot e recomendação personalizada)

## 📂 Funcionalidades Principais

### 👤 Cadastro de Clientes
- Cadastro e alteração de clientes
- Inativação de clientes
- Consulta de clientes e transações
- Cadastro de endereços de entrega e cartões de crédito

### 🛒 Gerenciamento de Vendas
- Gerenciamento de carrinho de compras
- Realização de compras e cálculo de frete
- Seleção de forma de pagamento e endereço de entrega
- Controle de status de pedidos (Em processamento, Em trânsito, Entregue)
- Solicitação e autorização de trocas

### 📦 Controle de Estoque
- Entrada e saída de livros no estoque
- Cálculo automático do valor de venda
- Reentrada em estoque a partir de trocas

### 📊 Análise e Recomendação com IA
- Análise de histórico de vendas com comparação de produtos e categorias
- Recomendação personalizada baseada em histórico de compras
- Interação via chatbot com IA para busca e sugestões

## 🏗 Requisitos Não Funcionais
- Tempo de resposta para consultas inferior a 1 segundo
- Registro de logs para todas as transações
- Senha forte e criptografada para os clientes
- Gráfico de vendas em formato de linha

## ✅ Deploy
- Foi feito um Deploy no site Render, acessível pelo seguinte link
- ### [les-livraria.onrender.com](https://les-livraria.onrender.com)


## 🔧 Instalação e Execução

1. **Clone o repositório:**  
   ```sh
   git clone https://github.com/fabin0casa/e-commerce-Livros.git
   cd e-commerce-Livros
   ```

2. **Executar com Docker (Recomendado):**  
   Certifique-se de ter o [Docker](https://www.docker.com/) instalado e em execução.  
   Depois, utilize o comando abaixo para iniciar a aplicação:
   ```sh
   docker-compose up --build -d
   ```
   ```sh
   docker-compose up -d
   ```
   Isso irá levantar tanto o backend quanto o banco de dados automaticamente.  

4. **Executar manualmente (Caso não use Docker):**  

   - **Configurar o Banco de Dados:**  
     - Criar um banco MySQL localmente.  
     - Atualizar as credenciais no arquivo `application.properties` do Spring Boot.  

   - **Rodar a API Spring Boot:**  
     ```sh
     mvn spring-boot:run
     ```

   - **Executar o frontend:**  
     - Abra o link `localhost:8080` em um navegador.  

---

Caso precise parar a aplicação no Docker, utilize:  
```sh
docker-compose down
```

Se precisar reconstruir a imagem:  
```sh
docker-compose up --build -d
```

## 📌 Contribuição
Sinta-se à vontade para abrir issues e pull requests para melhorias no projeto.


