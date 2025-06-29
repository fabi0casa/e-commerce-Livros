from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

#esse teste espera que o primeiro cliente da lista seja admin.

class TestTesteCrud():
    def setup_method(self, method):
        self.driver = webdriver.Firefox()
        self.vars = {}

    def teardown_method(self, method):
        self.driver.quit()

    def acessar_pagina(self):
        """Acessa a página de gerenciamento de clientes."""
        self.driver.get("http://localhost:8080/login")
        self.driver.set_window_size(1600, 864)
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button").click()
        time.sleep(3)
        self.driver.find_element(By.LINK_TEXT, "Gerenciar Clientes").click()
        time.sleep(1)

    def cadastrar_cliente(self):
        """Realiza o cadastro de um novo cliente."""
        self.driver.find_element(By.CSS_SELECTOR, ".new-client").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "nome").click()
        self.driver.find_element(By.ID, "nome").send_keys("Fábio Casagrande")
        time.sleep(1)
        self.driver.find_element(By.ID, "dataNascimento").click()
        self.driver.find_element(By.ID, "dataNascimento").send_keys("2004-12-21")
        time.sleep(1)
        self.driver.find_element(By.ID, "cpf").click()
        self.driver.find_element(By.ID, "cpf").send_keys("899.889.982-98")
        time.sleep(1)
        self.driver.find_element(By.ID, "genero").click()
        self.driver.find_element(By.ID, "genero").click()
        time.sleep(1)
        dropdown = self.driver.find_element(By.ID, "genero")
        dropdown.find_element(By.XPATH, "//option[. = 'Masculino']").click()
        self.driver.find_element(By.CSS_SELECTOR, "#genero > option:nth-child(2)").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "telefone").click()
        self.driver.find_element(By.ID, "telefone").send_keys("(11) 97875-6281")
        time.sleep(1)
        self.driver.find_element(By.ID, "email").click()
        self.driver.find_element(By.ID, "email").send_keys("fabiocasa@gmail.com")
        time.sleep(1)
        self.driver.find_element(By.ID, "senha").click()
        self.driver.find_element(By.ID, "senha").send_keys("Bauro2332#")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".password-container:nth-child(17) img").click()
        self.driver.find_element(By.CSS_SELECTOR, ".password-container:nth-child(20) img").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "senha").click()
        self.driver.find_element(By.ID, "confirmarSenha").click()
        self.driver.find_element(By.ID, "confirmarSenha").send_keys("Bauro2332#")
        time.sleep(1)
        self.driver.find_element(By.ID, "tipoLogradouro").click()
        self.driver.find_element(By.ID, "tipoLogradouro").send_keys("rua")
        time.sleep(1)
        self.driver.find_element(By.ID, "logradouro").click()
        self.driver.find_element(By.ID, "logradouro").send_keys("flores")
        time.sleep(1)
        self.driver.find_element(By.ID, "numero").click()
        self.driver.find_element(By.ID, "numero").send_keys("74")
        time.sleep(1)
        self.driver.find_element(By.ID, "bairro").click()
        self.driver.find_element(By.ID, "bairro").send_keys("centro")
        time.sleep(1)
        self.driver.find_element(By.ID, "cep").click()
        self.driver.find_element(By.ID, "cep").send_keys("08888-888")
        time.sleep(1)
        self.driver.find_element(By.ID, "cidade").click()
        self.driver.find_element(By.ID, "cidade").send_keys("mogi das cruzes")
        time.sleep(1)
        self.driver.find_element(By.ID, "estado").click()
        self.driver.find_element(By.ID, "estado").send_keys("sp")
        time.sleep(1)
        self.driver.find_element(By.ID, "pais").click()
        self.driver.find_element(By.ID, "pais").send_keys("Brasil")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(46)").click()
        time.sleep(4)

        assert self.driver.switch_to.alert.text == "Sucesso! Cliente cadastrado com sucesso!"
        self.driver.switch_to.alert.accept()

    def verificar_cliente(self):
        """Verifica se o cliente cadastrado aparece na lista."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(1)").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "btnVerEnderecos").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".accordion").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "#addressModal .close").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "btnVerCartoes").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#cardModal .close").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#clientModal .close").click()

    def verificar_transacoes(self):
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) .transacoes-btn").click()
        time.sleep(4)
        self.driver.find_element(By.CSS_SELECTOR, ".botao-return").click()

    def pesquisar_cliente(self):
        """Testa a funcionalidade de pesquisa de clientes com diferentes filtros."""
        time.sleep(2)

        # Pesquisar por nome
        self.driver.find_element(By.ID, "nome").send_keys("Fábio Casagrande")
        self.driver.find_element(By.ID, "pesquisar").click()
        time.sleep(1)

        # Pesquisar por nome e gênero
        self.driver.find_element(By.ID, "genero").send_keys("masculino")
        self.driver.find_element(By.ID, "pesquisar").click()
        time.sleep(1)

        # Pesquisar por nome e e-mail
        self.driver.find_element(By.ID, "email").send_keys("fabiocasa@gmail.com")
        self.driver.find_element(By.ID, "pesquisar").click()
        time.sleep(1)

        # Pesquisar por nome e telefone
        self.driver.find_element(By.ID, "telefone").send_keys("(11) 97875-6281")
        self.driver.find_element(By.ID, "pesquisar").click()
        time.sleep(1)

        # Pesquisar por nome e data de nascimento
        self.driver.find_element(By.ID, "dataNascimento").send_keys("2004-12-21")
        self.driver.find_element(By.ID, "pesquisar").click()
        time.sleep(1)

    def atualizar_dados_cliente(self):
        """Atualiza o telefone do cliente cadastrado."""
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(2)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#editarDadosLink > button").click()
        time.sleep(1)

        telefone = self.driver.find_element(By.ID, "telefone")
        time.sleep(1)
        telefone.clear()
        time.sleep(1)
        telefone.send_keys("(11) 97578-9027")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(15)").click()
        time.sleep(2)

        assert self.driver.switch_to.alert.text == "Cliente atualizado com sucesso!"
        self.driver.switch_to.alert.accept()

    def verificar_alteracoes(self):
        time.sleep(2)
        #pesquisando denovo o cliente
        self.driver.find_element(By.ID, "nome").click()
        self.driver.find_element(By.ID, "nome").send_keys("Fábio Casagrande")
        time.sleep(1)
        self.driver.find_element(By.ID, "pesquisar").click()

        #conferindo se os dados foram alterados
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(1)").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "#clientModal .close").click()
        time.sleep(2)

    def atualizar_senha_cliente(self):
        """Testa a atualização da senha do cliente."""

        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(2)").click()
        self.driver.find_element(By.CSS_SELECTOR, "#alterarSenhaLink > button").click()
        time.sleep(1)

        self.driver.find_element(By.ID, "senhaAtual").send_keys("Bauro3223#")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".password-container:nth-child(4) img").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".password-container:nth-child(6) img").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".password-container:nth-child(9) img").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "senha").send_keys("FabioTeste6754#")
        time.sleep(1)
        self.driver.find_element(By.ID, "confirmarSenha").send_keys("FabioTeste6754#")
        time.sleep(1)

        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(10)").click()
        time.sleep(3)

        assert self.driver.switch_to.alert.text == "Erro ao alterar senha: Senha atual incorreta"
        self.driver.switch_to.alert.accept()

        time.sleep(1)
        self.driver.find_element(By.ID, "senhaAtual").clear()
        time.sleep(1)
        self.driver.find_element(By.ID, "senhaAtual").send_keys("Bauro2332#")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(10)").click()
        time.sleep(2)

        assert self.driver.switch_to.alert.text == "Senha alterada com sucesso!"
        self.driver.switch_to.alert.accept()

    def cadastrar_endereco_cliente(self):
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(2)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#editarEnderecoLink > button").click()
        time.sleep(1)

        """Verifica Endereço Existente"""
        self.driver.find_element(By.CSS_SELECTOR, ".client-actions > button:nth-child(1)").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "#detalhesModal .close").click()
        time.sleep(1)

        """Cadastra um novo endereço para o cliente."""
        self.driver.find_element(By.CSS_SELECTOR, ".new-client").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "tipoLogradouro").send_keys("Rua")
        time.sleep(1)
        self.driver.find_element(By.ID, "logradouro").send_keys("Francisco Franco")
        time.sleep(1)
        self.driver.find_element(By.ID, "numero").send_keys("89")
        time.sleep(1)
        self.driver.find_element(By.ID, "bairro").send_keys("Centro")
        time.sleep(1)
        self.driver.find_element(By.ID, "cep").send_keys("08542-755")
        time.sleep(1)
        self.driver.find_element(By.ID, "cidade").send_keys("Mogi das Cruzes")
        time.sleep(1)
        self.driver.find_element(By.ID, "estado").send_keys("São Paulo")
        time.sleep(1)

        #definindo se endereço é residencial ou não
        dropdown = self.driver.find_element(By.ID, "residencia")
        dropdown.find_element(By.XPATH, "//option[. = 'Sim']").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#residencia > option:nth-child(2)").click()
        self.driver.find_element(By.ID, "residencia").click()
        time.sleep(1)
        dropdown = self.driver.find_element(By.ID, "residencia")
        dropdown.find_element(By.XPATH, "//option[. = 'Não']").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#residencia > option:nth-child(3)").click()

        #definindo se o endereço é de entrega
        self.driver.find_element(By.ID, "entrega").click()
        dropdown = self.driver.find_element(By.ID, "entrega")
        time.sleep(1)
        dropdown.find_element(By.XPATH, "//option[. = 'Não']").click()
        self.driver.find_element(By.CSS_SELECTOR, "#entrega > option:nth-child(3)").click()
        time.sleep(1)

        #definindo se o endereço é de cobrança
        self.driver.find_element(By.ID, "cobranca").click()
        dropdown = self.driver.find_element(By.ID, "cobranca")
        time.sleep(1)
        dropdown.find_element(By.XPATH, "//option[. = 'Sim']").click()
        self.driver.find_element(By.CSS_SELECTOR, "#cobranca > option:nth-child(2)").click()
        self.driver.find_element(By.CSS_SELECTOR, ".container").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(28)").click()
        time.sleep(4)

        assert self.driver.switch_to.alert.text == "Endereço cadastrado com sucesso!"
        self.driver.switch_to.alert.accept()

    def editar_endereco(self):
        """Testa a edição do endereço de um cliente."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(2)").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "logradouro").clear()
        time.sleep(1)
        self.driver.find_element(By.ID, "logradouro").send_keys("Alberto Franco")
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(28)").click()
        time.sleep(2)

        assert self.driver.switch_to.alert.text == "Endereço atualizado com sucesso!"
        self.driver.switch_to.alert.accept()

        """Exclui o primeiro endereço."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(3)").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(4)").click()
        time.sleep(3)

        assert self.driver.switch_to.alert.text == "Endereço excluído com sucesso."
        self.driver.switch_to.alert.accept()

        #saindo da página de edição de endereços
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".botao-return").click()

    def crud_cartao(self):
        """Testa o crud de cartões para um cliente específico."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(2)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "#editarCartaoLink > button").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".new-client").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "numeroCartao").click()
        self.driver.find_element(By.ID, "numeroCartao").send_keys("4949 4949 4949 4949")
        time.sleep(1)
        self.driver.find_element(By.ID, "nomeImpresso").click()
        self.driver.find_element(By.ID, "nomeImpresso").send_keys("Fabio C")
        time.sleep(1)
        self.driver.find_element(By.ID, "bandeira").click()
        time.sleep(1)
        dropdown = self.driver.find_element(By.ID, "bandeira")
        time.sleep(1)
        dropdown.find_element(By.XPATH, "//option[. = 'Visa']").click()
        self.driver.find_element(By.CSS_SELECTOR, "#bandeira > option:nth-child(2)").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "codigoSeguranca").click()
        self.driver.find_element(By.ID, "codigoSeguranca").send_keys("4123")
        time.sleep(1)
        self.driver.find_element(By.ID, "preferencial").click()
        dropdown = self.driver.find_element(By.ID, "preferencial")
        time.sleep(1)
        dropdown.find_element(By.XPATH, "//option[. = 'Sim']").click()
        self.driver.find_element(By.CSS_SELECTOR, "#preferencial > option:nth-child(2)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(12)").click()
        time.sleep(4)

        assert self.driver.switch_to.alert.text == "Cartão cadastrado com sucesso!"
        self.driver.switch_to.alert.accept()

        """Editar cartão."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-actions > button:nth-child(1)").click()
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "#detalhesModal .close").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(2)").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "nomeImpresso").click()
        self.driver.find_element(By.ID, "nomeImpresso").clear()
        time.sleep(1)
        self.driver.find_element(By.ID, "nomeImpresso").send_keys("Fabio Casagrande")
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(12)").click()
        time.sleep(2)

        assert self.driver.switch_to.alert.text == "Cartão atualizado com sucesso!"
        self.driver.switch_to.alert.accept()

        """Exclui o cartão."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(3)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, "button:nth-child(4)").click()
        time.sleep(2)

        assert self.driver.switch_to.alert.text == "Cartão excluído com sucesso."
        self.driver.switch_to.alert.accept()

        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".botao-return").click()

    def excluir_cliente(self):
        """Testa a exclusão de um cliente."""
        time.sleep(2)
        self.driver.find_element(By.CSS_SELECTOR, ".client-item:nth-child(1) button:nth-child(3)").click()
        time.sleep(1)
        self.driver.find_element(By.CSS_SELECTOR, ".modal-content > button:nth-child(4)").click()
        time.sleep(2)
        assert self.driver.switch_to.alert.text == "Cliente excluído com sucesso."
        self.driver.switch_to.alert.accept()
        time.sleep(5)

    def main(self):
        """Executa todos os testes de CRUD em sequência."""
        self.acessar_pagina()
        self.cadastrar_cliente()
        self.verificar_cliente()
        self.verificar_transacoes()
        self.pesquisar_cliente()
        self.atualizar_dados_cliente()
        self.verificar_alteracoes()
        self.atualizar_senha_cliente()
        self.cadastrar_endereco_cliente()
        self.editar_endereco()
        self.crud_cartao()
        self.excluir_cliente()

# Para rodar os testes
if __name__ == "__main__":
    test = TestTesteCrud()
    test.setup_method(None)
    try:
        test.main()
    finally:
        test.teardown_method(None)
