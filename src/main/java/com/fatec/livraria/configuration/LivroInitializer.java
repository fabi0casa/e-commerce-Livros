package com.fatec.livraria.configuration;

import com.fatec.livraria.entity.*;
import com.fatec.livraria.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class LivroInitializer {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final GrupoPrecificacaoRepository grupoPrecificacaoRepository;

    public LivroInitializer(
        LivroRepository livroRepository,
        AutorRepository autorRepository,
        EditoraRepository editoraRepository,
        FornecedorRepository fornecedorRepository,
        GrupoPrecificacaoRepository grupoPrecificacaoRepository
    ) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.grupoPrecificacaoRepository = grupoPrecificacaoRepository;
    }

    @PostConstruct
    public void inicializarLivros() {
        // Exclui todos os livros antes de cadastrar novamente
        livroRepository.deleteAll();

        // Pegando os registros das entidades relacionadas
        Optional<Autor> autor1 = autorRepository.findById(1);
        Optional<Autor> autor2 = autorRepository.findById(2);
        Optional<Autor> autor3 = autorRepository.findById(3);
        Optional<Autor> autor4 = autorRepository.findById(4);
        Optional<Editora> editora1 = editoraRepository.findById(1);
        Optional<Editora> editora2 = editoraRepository.findById(2);
        Optional<Fornecedor> fornecedor1 = fornecedorRepository.findById(1);
        Optional<Fornecedor> fornecedor2 = fornecedorRepository.findById(2);
        Optional<GrupoPrecificacao> grupo1 = grupoPrecificacaoRepository.findById(1);
        Optional<GrupoPrecificacao> grupo2 = grupoPrecificacaoRepository.findById(2);

        // Criando livros e associando com as entidades relacionadas
        List<Livro> livros = List.of(
            new Livro(null, "Diário de Um Banana 9", 2016, "1ª Edição", "978-3-16-148410-0", 223, 
                        "9º Livro da Saga Diário de um Banana!", "1234567890123", "/books//Diario de um banana 9.png",
                        new BigDecimal("50.00"), new BigDecimal("72.90"), 
                        autor4.orElse(null), editora1.orElse(null), fornecedor1.orElse(null), grupo1.orElse(null)),
            
            new Livro(null, "Os Elementos", 2016, "13ª Edição", "978-3-16-148410-0", 459, 
                        "Livro Base para fundamentos da Geometria", "1234567890123", "/books//Os Elementos.png",
                        new BigDecimal("50.00"), new BigDecimal("65.00"), 
                        autor3.orElse(null), editora2.orElse(null), fornecedor2.orElse(null), grupo2.orElse(null)),

            new Livro(null, "Senhor dos Aneis", 2016, "1ª Edição", "978-3-16-148410-0", 322, 
                        "A jornada do Anel começa!", "1234567890123", "/books//Senhor dos aneis.png",
                        new BigDecimal("50.00"), new BigDecimal("185.90"), 
                        autor1.orElse(null), editora2.orElse(null), fornecedor1.orElse(null), grupo2.orElse(null)),

            new Livro(null, "Tradução Ecumênica da Bíblia", 2016, "1ª Edição", "978-3-16-148410-0", 2041, 
                        "Tradução da Bíblia levando em conta todas as denominações", "1234567890123", "/books//TEB.png",
                        new BigDecimal("50.00"), new BigDecimal("290.00"), 
                        autor2.orElse(null), editora1.orElse(null), fornecedor2.orElse(null), grupo1.orElse(null))
        );

        // Salvando os livros no banco
        livroRepository.saveAll(livros);

        System.out.println("✅ Livros cadastrados automaticamente.");
    }
}
