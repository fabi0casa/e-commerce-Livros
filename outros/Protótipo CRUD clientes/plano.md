Este `plano` foi elaborado pelo GPT, mas não seguirei ele a risca, usarei apenas como um apoio ou um 'checklist' 

**Protótipo CRUD de Clientes - E-commerce de Livros**

## **Planejamento das Telas**
### **1. Tela de Listagem de Clientes**
**Objetivo:** Exibir uma lista de clientes cadastrados no sistema, permitindo filtros e ações de edição e inativação.

**Componentes:**
- Campo de pesquisa (filtrar por nome, e-mail, CPF, telefone, etc.)
- Botão "Adicionar Cliente"
- Tabela com colunas:
  - ID do Cliente
  - Nome Completo
  - E-mail
  - Telefone
  - Opções (Editar / Inativar / Ver Transações)
- Paginação

### **2. Tela de Cadastro de Cliente**
**Objetivo:** Permitir o cadastro de novos clientes.

**Campos obrigatórios (conforme RN0026):**
- Nome Completo
- Data de Nascimento
- CPF
- Gênero
- Telefone (Tipo, DDD e Número)
- E-mail
- Senha (com confirmação, seguindo RNF0031, RNF0032 e RNF0033)
- Endereço Residencial:
  - Tipo de Residência (Casa, Apartamento, etc.)
  - Tipo de Logradouro
  - Logradouro
  - Número
  - Bairro
  - CEP
  - Cidade
  - Estado
  - País
  - Observação (Opcional)

**Botões:**
- "Salvar"
- "Cancelar"

### **3. Tela de Edição de Cliente**
**Objetivo:** Permitir a alteração dos dados cadastrais do cliente.

**Componentes:**
- Campos editáveis idênticos ao cadastro
- Aba separada para:
  - Gerenciar endereços de entrega (RF0026)
  - Gerenciar cartões de crédito (RF0027)
  - Consultar transações (RF0025)
- Botões:
  - "Salvar Alterações"
  - "Cancelar"

### **4. Tela de Inativação de Cliente**
**Objetivo:** Permitir que um cliente seja inativado.

**Componentes:**
- Mensagem de confirmação antes de inativar
- Opção para justificar a inativação
- Botões:
  - "Confirmar Inativação"
  - "Cancelar"

### **5. Tela de Consulta de Transações do Cliente**
**Objetivo:** Exibir o histórico de compras do cliente.

**Componentes:**
- Campo de filtro por período
- Tabela com colunas:
  - ID da Transação
  - Data
  - Valor
  - Forma de Pagamento
  - Status da Compra (Em Processamento, Em Trânsito, Entregue, etc.)
  - Opção para visualizar detalhes

### **6. Tela de Gerenciamento de Endereços**
**Objetivo:** Permitir a adição, edição e remoção de endereços de entrega do cliente.

**Componentes:**
- Listagem de endereços cadastrados
- Botão "Adicionar Novo Endereço"
- Opções de "Editar" e "Excluir" para cada endereço

### **7. Tela de Gerenciamento de Cartões**
**Objetivo:** Permitir a adição, edição e remoção de cartões de crédito do cliente.

**Componentes:**
- Listagem de cartões cadastrados
- Botão "Adicionar Novo Cartão"
- Opções de "Editar", "Excluir" e "Definir como Preferencial"

### **8. Tela de Alterar Apenas a Senha**
**Objetivo:** Permitir que a senha do cliente seja alterada sem modificar os outros dados.

**Componentes:**
- Campo "Senha Atual"
- Campo "Nova Senha"
- Campo "Confirmar Nova Senha"
- Botão "Salvar"
- Botão "Cancelar"

## **Fluxo de Navegação**
1. Administrador acessa a "Listagem de Clientes".
2. Pode optar por "Cadastrar Novo Cliente", "Editar Cliente", "Inativar Cliente" ou "Consultar Transações".
3. Dentro do cadastro/edição, pode gerenciar endereços e cartões.
4. Pode acessar a tela de "Alterar Senha" caso precise modificar apenas a senha do cliente.

---
Esse planejamento garante que todos os requisitos funcionais e regras de negócio sejam atendidos de forma organizada e eficiente.

