package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Notificacao;
import com.fatec.livraria.entity.Cliente;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByClienteAndIsVistoFalse(Cliente cliente, Sort sort);
}
