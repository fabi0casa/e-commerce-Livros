package com.fatec.livraria.service;

import com.fatec.livraria.dto.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private LivroService livroService;

    public void criarVenda(VendaRequest vendaReq, Pedido pedido) {
        var livro = livroService.buscarPorId(vendaReq.getLivroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Venda venda = new Venda();
        venda.setStatus("Em Processamento");
        venda.setValor(vendaReq.getValor());
        venda.setLivro(livro);
        venda.setPedido(pedido);

        vendaRepository.save(venda);
    }

    public Venda atualizarStatus(Integer vendaId, String status) {
        return vendaRepository.findById(vendaId).map(venda -> {
            venda.setStatus(status);
            return vendaRepository.save(venda);
        }).orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }
}
