package com.fatec.livraria.configuration;

import com.fatec.livraria.entity.*;
import com.fatec.livraria.repository.*;
import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@DependsOn({"autorService", "editoraService", "fornecedorService", "grupoPrecificacaoService"})
public class LivroInitializer {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final GrupoPrecificacaoRepository grupoPrecificacaoRepository;
    private final CarrinhoRepository carrinhoRepository;

    public LivroInitializer(
        LivroRepository livroRepository,
        AutorRepository autorRepository,
        EditoraRepository editoraRepository,
        FornecedorRepository fornecedorRepository,
        GrupoPrecificacaoRepository grupoPrecificacaoRepository,
        CarrinhoRepository carrinhoRepository
    ) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.grupoPrecificacaoRepository = grupoPrecificacaoRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    @PostConstruct
    public void inicializarLivros() {
        if (livroRepository.count() != 4) {
            // Exclui todos os livros antes de cadastrar novamente
            carrinhoRepository.deleteAll();
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
                        "Greg Heffley encara novos desafios quando sua escola inicia um projeto de cultura sustentável. Entre trapalhadas hilárias e confusões inesperadas, ele precisa lidar com responsabilidades que nunca quis. Será que vai sobreviver ao caos?", 
                        "1234567890123", "/books//Diario de um banana 9.png",
                        new BigDecimal("50.00"), new BigDecimal("72.90"), 
                        autor4.orElse(null), editora1.orElse(null), fornecedor1.orElse(null), grupo1.orElse(null)),

                new Livro(null, "Os Elementos", 2016, "13ª Edição", "978-3-16-148410-0", 459, 
                        "Obra fundamental da matemática, 'Os Elementos' de Euclides apresenta os princípios da geometria euclidiana. Com demonstrações lógicas rigorosas, este livro influenciou a ciência e a filosofia por mais de dois mil anos.", 
                        "1234567890123", "/books//Os Elementos.png",
                        new BigDecimal("50.00"), new BigDecimal("65.00"), 
                        autor3.orElse(null), editora2.orElse(null), fornecedor2.orElse(null), grupo2.orElse(null)),

                new Livro(null, "Senhor dos Anéis", 2016, "1ª Edição", "978-3-16-148410-0", 322, 
                        "A jornada épica de Frodo Bolseiro para destruir o Um Anel e impedir que Sauron domine a Terra Média. Entre batalhas, criaturas místicas e alianças improváveis, esta aventura redefiniu o gênero da fantasia para sempre.", 
                        "1234567890123", "/books//Senhor dos aneis.png",
                        new BigDecimal("50.00"), new BigDecimal("185.90"), 
                        autor1.orElse(null), editora2.orElse(null), fornecedor1.orElse(null), grupo2.orElse(null)),

                new Livro(null, "Tradução Ecumênica da Bíblia", 2016, "1ª Edição", "978-3-16-148410-0", 2041, 
                        "Uma tradução da Bíblia que une diferentes tradições cristãs, oferecendo um texto acessível e fiel aos originais. Com notas explicativas e contexto histórico, é uma referência essencial para estudiosos e fiéis.", 
                        "1234567890123", "/books//TEB.png",
                        new BigDecimal("50.00"), new BigDecimal("290.00"), 
                        autor2.orElse(null), editora1.orElse(null), fornecedor2.orElse(null), grupo1.orElse(null))
            );

            // Salvando os livros no banco
            livroRepository.saveAll(livros);

            System.out.println("✅ Livros cadastrados automaticamente.");
        }
    }
    
}