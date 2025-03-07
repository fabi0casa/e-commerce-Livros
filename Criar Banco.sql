-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -----------------------------------------------------
-- Table `mydb`.`CLIENTES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CLIENTES` (
  `cli_id` INT NOT NULL AUTO_INCREMENT,
  `cli_nome` VARCHAR(45) NOT NULL,
  `cli_data_nascimento` DATE NOT NULL,
  `cli_cpf` VARCHAR(20) NOT NULL,
  `cli_telefone` VARCHAR(20) NOT NULL,
  `cli_email` VARCHAR(45) NOT NULL,
  `cli_senha` VARCHAR(45) NOT NULL,
  `cli_ranking` INT NOT NULL,
  `cli_genero` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`cli_id`),
  UNIQUE INDEX `cli_id_UNIQUE` (`cli_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ENDEREÇOS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ENDEREÇOS` (
  `end_id` INT NOT NULL AUTO_INCREMENT,
  `end_tipo` VARCHAR(45) NOT NULL,
  `end_logradouro` VARCHAR(45) NOT NULL,
  `end_numero` VARCHAR(10) NOT NULL,
  `end_bairro` VARCHAR(45) NOT NULL,
  `end_cep` VARCHAR(10) NOT NULL,
  `end_cidade` VARCHAR(45) NOT NULL,
  `end_estado` VARCHAR(45) NOT NULL,
  `end_pais` VARCHAR(45) NOT NULL,
  `end_observacoes` VARCHAR(255) NULL,
  `end_frase_identificadora` VARCHAR(125) NOT NULL,
  `end_isResidencial` TINYINT(1) NOT NULL,
  `end_isEntrega` TINYINT(1) NOT NULL,
  `end_isCobranca` TINYINT(1) NOT NULL,
  `end_cli_id` INT NOT NULL,
  PRIMARY KEY (`end_id`, `end_cli_id`),
  UNIQUE INDEX `end_id_UNIQUE` (`end_id` ASC) VISIBLE,
  INDEX `fk_ENDEREÇOS_CLIENTES_idx` (`end_cli_id` ASC) VISIBLE,
  CONSTRAINT `fk_ENDEREÇOS_CLIENTES`
    FOREIGN KEY (`end_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`BANDEIRAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BANDEIRAS` (
  `ban_id` INT NOT NULL AUTO_INCREMENT,
  `ban_nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ban_id`),
  UNIQUE INDEX `ban_id_UNIQUE` (`ban_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CARTOES DE CREDITO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CARTOES DE CREDITO` (
  `crt_id` INT NOT NULL AUTO_INCREMENT,
  `crt_numero` VARCHAR(45) NOT NULL,
  `crt_nome_impresso` VARCHAR(45) NOT NULL,
  `crt_codigo_seguranca` VARCHAR(20) NOT NULL,
  `car_isPreferencial` TINYINT(1) NOT NULL,
  `crt_cli_id` INT NOT NULL,
  `crt_ban_id` INT NOT NULL,
  PRIMARY KEY (`crt_id`, `crt_cli_id`, `crt_ban_id`),
  UNIQUE INDEX `car_id_UNIQUE` (`crt_id` ASC) VISIBLE,
  INDEX `fk_CARTOES DE CREDITO_CLIENTES1_idx` (`crt_cli_id` ASC) VISIBLE,
  INDEX `fk_CARTOES DE CREDITO_BANDEIRAS1_idx` (`crt_ban_id` ASC) VISIBLE,
  CONSTRAINT `fk_CARTOES DE CREDITO_CLIENTES1`
    FOREIGN KEY (`crt_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CARTOES DE CREDITO_BANDEIRAS1`
    FOREIGN KEY (`crt_ban_id`)
    REFERENCES `BANDEIRAS` (`ban_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`TRANSACOES CLIENTE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRANSACOES CLIENTE` (
  `tra_id` INT NOT NULL AUTO_INCREMENT,
  `tra_data_hora` DATETIME NOT NULL,
  `tra_tipo_operacao` VARCHAR(45) NOT NULL,
  `tra_quantidade` INT NOT NULL,
  `tra_valor` DECIMAL(10,2) NOT NULL,
  `tra_forma_pagamento` VARCHAR(45) NOT NULL,
  `tra_cli_id` INT NOT NULL,
  PRIMARY KEY (`tra_id`, `tra_cli_id`),
  UNIQUE INDEX `tra_id_UNIQUE` (`tra_id` ASC) VISIBLE,
  INDEX `fk_TRANSACOES CLIENTE_CLIENTES1_idx` (`tra_cli_id` ASC) VISIBLE,
  CONSTRAINT `fk_TRANSACOES CLIENTE_CLIENTES1`
    FOREIGN KEY (`tra_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`AUTORES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AUTORES` (
  `aut_id` INT NOT NULL AUTO_INCREMENT,
  `aut_nome` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`aut_id`),
  UNIQUE INDEX `aut_id_UNIQUE` (`aut_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`EDITORAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EDITORAS` (
  `edi_id` INT NOT NULL AUTO_INCREMENT,
  `edi_nome` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`edi_id`),
  UNIQUE INDEX `edi_id_UNIQUE` (`edi_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`FORNECEDORES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `FORNECEDORES` (
  `for_id` INT NOT NULL AUTO_INCREMENT,
  `for_nome` VARCHAR(100) NOT NULL,
  UNIQUE INDEX `for_id_UNIQUE` (`for_id` ASC) VISIBLE,
  PRIMARY KEY (`for_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`GRUPO PRECIFICACAO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GRUPO PRECIFICACAO` (
  `gpr_id` INT NOT NULL AUTO_INCREMENT,
  `gpr_nome` VARCHAR(45) NOT NULL,
  `gpr_margem_lucro` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`gpr_id`),
  UNIQUE INDEX `gpr_id_UNIQUE` (`gpr_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`LIVROS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LIVROS` (
  `lvr_id` INT NOT NULL AUTO_INCREMENT,
  `lvr_ano_publicacao` YEAR(4) NOT NULL,
  `lvr_edicao` VARCHAR(45) NOT NULL,
  `lvr_isbn` VARCHAR(45) NOT NULL,
  `lvr_num_paginas` INT NOT NULL,
  `lvr_sinopse` VARCHAR(255) NOT NULL,
  `lvr_codigo_barras` VARCHAR(60) NOT NULL,
  `lvr_caminho_imagem` VARCHAR(255) NOT NULL,
  `lvr_preco_custo` DECIMAL(10,2) NOT NULL,
  `lvr_preco_venda` DECIMAL(10,2) NOT NULL,
  `lvr_aut_id` INT NOT NULL,
  `lvr_edi_id` INT NOT NULL,
  `lvr_for_id` INT NOT NULL,
  `lvr_gpr_id` INT NOT NULL,
  PRIMARY KEY (`lvr_id`, `lvr_aut_id`, `lvr_edi_id`, `lvr_for_id`, `lvr_gpr_id`),
  UNIQUE INDEX `lvr_id_UNIQUE` (`lvr_id` ASC) VISIBLE,
  INDEX `fk_LIVROS_AUTORES1_idx` (`lvr_aut_id` ASC) VISIBLE,
  INDEX `fk_LIVROS_EDITORAS1_idx` (`lvr_edi_id` ASC) VISIBLE,
  INDEX `fk_LIVROS_FORNECEDORES1_idx` (`lvr_for_id` ASC) VISIBLE,
  INDEX `fk_LIVROS_GRUPO PRECIFICACAO1_idx` (`lvr_gpr_id` ASC) VISIBLE,
  CONSTRAINT `fk_LIVROS_AUTORES1`
    FOREIGN KEY (`lvr_aut_id`)
    REFERENCES `AUTORES` (`aut_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LIVROS_EDITORAS1`
    FOREIGN KEY (`lvr_edi_id`)
    REFERENCES `EDITORAS` (`edi_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LIVROS_FORNECEDORES1`
    FOREIGN KEY (`lvr_for_id`)
    REFERENCES `FORNECEDORES` (`for_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LIVROS_GRUPO PRECIFICACAO1`
    FOREIGN KEY (`lvr_gpr_id`)
    REFERENCES `GRUPO PRECIFICACAO` (`gpr_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CARRINHO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CARRINHO` (
  `car_id` INT NOT NULL AUTO_INCREMENT,
  `car_quantidade` INT NOT NULL,
  `car_valor` DECIMAL(10,2) NOT NULL,
  `car_data` DATE NOT NULL,
  `car_cli_id` INT NOT NULL,
  `car_lvr_id` INT NOT NULL,
  PRIMARY KEY (`car_id`),
  UNIQUE INDEX `car_id_UNIQUE` (`car_id` ASC) VISIBLE,
  INDEX `fk_CARRINHO_CLIENTES1_idx` (`car_cli_id` ASC) VISIBLE,
  INDEX `fk_CARRINHO_LIVROS1_idx` (`car_lvr_id` ASC) VISIBLE,
  CONSTRAINT `fk_CARRINHO_CLIENTES1`
    FOREIGN KEY (`car_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CARRINHO_LIVROS1`
    FOREIGN KEY (`car_lvr_id`)
    REFERENCES `LIVROS` (`lvr_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`VENDAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VENDAS` (
  `vnd_id` INT NOT NULL AUTO_INCREMENT,
  `vnd_data_hora` DATETIME NOT NULL,
  `vnd_quantidade` INT NOT NULL,
  `vnd_forma_pagamento` VARCHAR(45) NOT NULL,
  `vnd_status` VARCHAR(15) NOT NULL,
  `vnd_cli_id` INT NOT NULL,
  `vnd_lvr_id` INT NOT NULL,
  `vnd_end_id` INT NOT NULL,
  PRIMARY KEY (`vnd_id`, `vnd_cli_id`, `vnd_lvr_id`, `vnd_end_id`),
  UNIQUE INDEX `vnd_id_UNIQUE` (`vnd_id` ASC) VISIBLE,
  INDEX `fk_VENDAS_CLIENTES1_idx` (`vnd_cli_id` ASC) VISIBLE,
  INDEX `fk_VENDAS_LIVROS1_idx` (`vnd_lvr_id` ASC) VISIBLE,
  INDEX `fk_VENDAS_ENDEREÇOS1_idx` (`vnd_end_id` ASC) VISIBLE,
  CONSTRAINT `fk_VENDAS_CLIENTES1`
    FOREIGN KEY (`vnd_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VENDAS_LIVROS1`
    FOREIGN KEY (`vnd_lvr_id`)
    REFERENCES `LIVROS` (`lvr_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VENDAS_ENDEREÇOS1`
    FOREIGN KEY (`vnd_end_id`)
    REFERENCES `ENDEREÇOS` (`end_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CUPONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CUPONS` (
  `cup_id` INT NOT NULL AUTO_INCREMENT,
  `cup_codigo` VARCHAR(45) NOT NULL,
  `cup_valor` DECIMAL(10,2) NOT NULL,
  `cup_tipo` VARCHAR(45) NOT NULL,
  `cup_cli_id` INT NOT NULL,
  PRIMARY KEY (`cup_id`),
  UNIQUE INDEX `cup_id_UNIQUE` (`cup_id` ASC) VISIBLE,
  INDEX `fk_CUPONS_CLIENTES1_idx` (`cup_cli_id` ASC) VISIBLE,
  CONSTRAINT `fk_CUPONS_CLIENTES1`
    FOREIGN KEY (`cup_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`LOGS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LOGS` (
  `log_id` INT NOT NULL AUTO_INCREMENT,
  `log_data_hora` VARCHAR(45) NOT NULL,
  `log_usuario` VARCHAR(45) NOT NULL,
  `log_descricao` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`log_id`),
  UNIQUE INDEX `log_id_UNIQUE` (`log_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CATEGORIAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CATEGORIAS` (
  `cat_id` INT NOT NULL AUTO_INCREMENT,
  `cat_nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE INDEX `cat_id_UNIQUE` (`cat_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ESTOQUE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ESTOQUE` (
  `est_id` INT NOT NULL AUTO_INCREMENT,
  `est_quantidade` INT NOT NULL,
  `est_data_entrada` DATE NOT NULL,
  `est_lvr_id` INT NOT NULL,
  PRIMARY KEY (`est_id`, `est_lvr_id`),
  UNIQUE INDEX `est_id_UNIQUE` (`est_id` ASC) VISIBLE,
  INDEX `fk_ESTOQUE_LIVROS1_idx` (`est_lvr_id` ASC) VISIBLE,
  CONSTRAINT `fk_ESTOQUE_LIVROS1`
    FOREIGN KEY (`est_lvr_id`)
    REFERENCES `LIVROS` (`lvr_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`LIVROS_CATEGORIAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LIVROS_CATEGORIAS` (
  `lvc_id` INT NOT NULL AUTO_INCREMENT,
  `lvc_lvr_id` INT NOT NULL,
  `lvc_cat_id` INT NOT NULL,
  PRIMARY KEY (`lvc_id`, `lvc_lvr_id`, `lvc_cat_id`),
  INDEX `fk_LIVROS_has_CATEGORIAS_CATEGORIAS1_idx` (`lvc_cat_id` ASC) VISIBLE,
  INDEX `fk_LIVROS_has_CATEGORIAS_LIVROS1_idx` (`lvc_lvr_id` ASC) VISIBLE,
  UNIQUE INDEX `lvc_id_UNIQUE` (`lvc_id` ASC) VISIBLE,
  CONSTRAINT `fk_LIVROS_has_CATEGORIAS_LIVROS1`
    FOREIGN KEY (`lvc_lvr_id`)
    REFERENCES `LIVROS` (`lvr_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LIVROS_has_CATEGORIAS_CATEGORIAS1`
    FOREIGN KEY (`lvc_cat_id`)
    REFERENCES `CATEGORIAS` (`cat_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`NOTIFICACOES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `NOTIFICACOES` (
  `not_id` INT NOT NULL AUTO_INCREMENT,
  `not_titulo` VARCHAR(50) NOT NULL,
  `not_descricao` VARCHAR(255) NOT NULL,
  `not_cli_id` INT NOT NULL,
  PRIMARY KEY (`not_id`),
  UNIQUE INDEX `not_id_UNIQUE` (`not_id` ASC) VISIBLE,
  INDEX `fk_NOTIFICACOES_CLIENTES1_idx` (`not_cli_id` ASC) VISIBLE,
  CONSTRAINT `fk_NOTIFICACOES_CLIENTES1`
    FOREIGN KEY (`not_cli_id`)
    REFERENCES `CLIENTES` (`cli_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
