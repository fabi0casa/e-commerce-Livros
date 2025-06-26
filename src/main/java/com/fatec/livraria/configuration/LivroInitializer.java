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
@DependsOn({"autorService", "editoraService", "fornecedorService", "grupoPrecificacaoService", "categoriaService"})
public class LivroInitializer {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final GrupoPrecificacaoRepository grupoPrecificacaoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final CategoriaRepository categoriaRepository;
    

    public LivroInitializer(
        LivroRepository livroRepository,
        AutorRepository autorRepository,
        EditoraRepository editoraRepository,
        FornecedorRepository fornecedorRepository,
        GrupoPrecificacaoRepository grupoPrecificacaoRepository,
        CarrinhoRepository carrinhoRepository,
        CategoriaRepository categoriaRepository
    ) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.grupoPrecificacaoRepository = grupoPrecificacaoRepository;
        this.carrinhoRepository = carrinhoRepository;
        ;this.categoriaRepository = categoriaRepository;
    }

    @PostConstruct
    public void inicializarLivros() {
        if (livroRepository.count() != 16) {
            // Exclui todos os livros antes de cadastrar novamente
            carrinhoRepository.deleteAll();
            livroRepository.deleteAll();

            // Pegando os registros das entidades relacionadas
            Optional<Autor> tolkien = autorRepository.findById(1);
            Optional<Autor> konings = autorRepository.findById(2);
            Optional<Autor> euclides = autorRepository.findById(3);
            Optional<Autor> jeffKinney = autorRepository.findById(4);
            Optional<Autor> saintExupery = autorRepository.findById(5);
            Optional<Autor> orwell = autorRepository.findById(6);
            Optional<Autor> mojang = autorRepository.findById(7);
            Optional<Autor> varios = autorRepository.findById(8);
            Optional<Autor> machado = autorRepository.findById(9);
            Optional<Autor> editoraEuropa = autorRepository.findById(10);
            Optional<Autor> jkRowling = autorRepository.findById(11);
            Optional<Autor> danBrown = autorRepository.findById(12);
            Optional<Autor> yuval = autorRepository.findById(13);
            Optional<Autor> willianYoung = autorRepository.findById(14);

            Optional<Editora> happerCollins = editoraRepository.findById(1);
            Optional<Editora> saraiva = editoraRepository.findById(2);
            Optional<Editora> pandaBooks = editoraRepository.findById(3);
            Optional<Editora> autentica = editoraRepository.findById(4);

            Optional<Fornecedor> suzano = fornecedorRepository.findById(1);
            Optional<Fornecedor> pernanbucanas = fornecedorRepository.findById(2);
            Optional<Fornecedor> independente = fornecedorRepository.findById(3);
            Optional<Fornecedor> nacional = fornecedorRepository.findById(4);

            Optional<GrupoPrecificacao> popular = grupoPrecificacaoRepository.findById(1);
            Optional<GrupoPrecificacao> padrao = grupoPrecificacaoRepository.findById(2);
            Optional<GrupoPrecificacao> premium = grupoPrecificacaoRepository.findById(3);
            Optional<GrupoPrecificacao> colecionador = grupoPrecificacaoRepository.findById(4);

            Optional<Categoria> comedia = categoriaRepository.findById(1);
            Optional<Categoria> ficcao = categoriaRepository.findById(2);
            Optional<Categoria> fantasia = categoriaRepository.findById(3);
            Optional<Categoria> historia = categoriaRepository.findById(4);
            Optional<Categoria> literaturaBrasileira = categoriaRepository.findById(5);
            Optional<Categoria> infantoJuvenil = categoriaRepository.findById(6);
            Optional<Categoria> religiao = categoriaRepository.findById(7);
            Optional<Categoria> didatico = categoriaRepository.findById(8);


            // Criando livros e associando com as entidades relacionadas
            List<Livro> livros = List.of(
                new Livro(null, "Diário de Um Banana 9", 2016, "1ª Edição", "978-3-16-148410-0", 223, 
                        "Greg Heffley encara novos desafios quando sua escola inicia um projeto de cultura sustentável. Entre trapalhadas hilárias e confusões inesperadas, ele precisa lidar com responsabilidades que nunca quis. Será que vai sobreviver ao caos?", 
                        "1234567890123", "/books/Diario de um banana 9.png/",
                        new BigDecimal("50.00"), new BigDecimal("72.90"), 
                        jeffKinney.orElse(null), happerCollins.orElse(null), suzano.orElse(null), popular.orElse(null), List.of(comedia.orElse(null)),90),

                new Livro(null, "Os Elementos", 2016, "13ª Edição", "978-3-16-148410-0", 459, 
                        "Obra fundamental da matemática, 'Os Elementos' de Euclides apresenta os princípios da geometria euclidiana. Com demonstrações lógicas rigorosas, este livro influenciou a ciência e a filosofia por mais de dois mil anos.", 
                        "1234567890123", "/books/Os Elementos.png/",
                        new BigDecimal("50.00"), new BigDecimal("65.00"), 
                        euclides.orElse(null), saraiva.orElse(null), pernanbucanas.orElse(null), padrao.orElse(null), List.of(historia.orElse(null), didatico.orElse(null)),86),

                new Livro(null, "Senhor dos Anéis", 2016, "1ª Edição", "978-3-16-148410-0", 322, 
                        "A jornada épica de Frodo Bolseiro para destruir o Um Anel e impedir que Sauron domine a Terra Média. Entre batalhas, criaturas místicas e alianças improváveis, esta aventura redefiniu o gênero da fantasia para sempre.", 
                        "1234567890123", "/books/Senhor dos aneis.png/",
                        new BigDecimal("50.00"), new BigDecimal("185.90"), 
                        tolkien.orElse(null), saraiva.orElse(null), suzano.orElse(null), padrao.orElse(null), List.of(fantasia.orElse(null)),68),

                new Livro(null, "Tradução Ecumênica da Bíblia", 2016, "1ª Edição", "978-3-16-148410-0", 2041, 
                        "Uma tradução da Bíblia que une diferentes tradições cristãs, oferecendo um texto acessível e fiel aos originais. Com notas explicativas e contexto histórico, é uma referência essencial para estudiosos e fiéis.", 
                        "1234567890123", "/books/TEB.png/",
                        new BigDecimal("50.00"), new BigDecimal("290.00"), 
                        konings.orElse(null), happerCollins.orElse(null), pernanbucanas.orElse(null), popular.orElse(null), List.of(religiao.orElse(null)),37),

                new Livro(null, "O Pequeno Príncipe", 1943, "1ª Edição", "978-3-16-148410-0", 96,
                        "A clássica história de um menino que viaja de planeta em planeta descobrindo o verdadeiro significado do amor e da amizade.",
                        "1234567890123", "/books/O Pequeno Principe.webp/", new BigDecimal("50.00"), new BigDecimal("39.90"),
                        saintExupery.orElse(null), pandaBooks.orElse(null), suzano.orElse(null), popular.orElse(null), List.of(fantasia.orElse(null), ficcao.orElse(null)),56),
        
                new Livro(null, "1984", 1949, "1ª Edição", "978-3-16-148410-0", 328,
                        "Uma distopia clássica que retrata um mundo de vigilância extrema e controle totalitário.",
                        "1234567890123", "/books/1984.webp/", new BigDecimal("50.00"), new BigDecimal("42.00"),
                        orwell.orElse(null), saraiva.orElse(null), nacional.orElse(null), padrao.orElse(null), List.of(ficcao.orElse(null)),62),
        
                new Livro(null, "Guia de Redstone Minecraft", 2020, "2ª Edição", "978-3-16-148410-0", 112,
                        "Manual completo para dominar circuitos de Redstone no mundo de Minecraft.",
                        "1234567890123", "/books/Guia Redstone.jpg/", new BigDecimal("50.00"), new BigDecimal("29.90"),
                        mojang.orElse(null), pandaBooks.orElse(null), pernanbucanas.orElse(null), popular.orElse(null), List.of(infantoJuvenil.orElse(null)),45),
        
                new Livro(null, "365 Histórias para Sonhar", 2018, "1ª Edição", "978-3-16-148410-0", 365,
                        "Uma história para cada dia do ano, ideal para a hora de dormir com os pequenos.",
                        "1234567890123", "/books/365 historias.jpg/", new BigDecimal("50.00"), new BigDecimal("69.90"),
                        varios.orElse(null), autentica.orElse(null), suzano.orElse(null), padrao.orElse(null), List.of(infantoJuvenil.orElse(null)),36),
        
                new Livro(null, "Memórias Póstumas de Brás Cubas", 1881, "1ª Edição", "978-3-16-148410-0", 200,
                        "O clássico de Machado de Assis narrado por um defunto-autor, repleto de crítica social e ironia.",
                        "1234567890123", "/books/bras cubas.jpg/", new BigDecimal("50.00"), new BigDecimal("25.00"),
                        machado.orElse(null), saraiva.orElse(null), nacional.orElse(null), popular.orElse(null), List.of(literaturaBrasileira.orElse(null)),78),
        
                new Livro(null, "Dossiê OLD!Gamer Volume 09: Nintendo 64", 2021, "1ª Edição", "978-3-16-148410-0", 148,
                        "Revista especializada que aprofunda no histórico e curiosidades do lendário Nintendo 64.",
                        "1234567890123", "/books/nintendo 64.jpg/", new BigDecimal("50.00"), new BigDecimal("59.90"),
                        editoraEuropa.orElse(null), pandaBooks.orElse(null), independente.orElse(null), colecionador.orElse(null), List.of(infantoJuvenil.orElse(null)),59),

                new Livro(null, "Dom Casmurro", 1899, "1ª Edição", "978-3-16-148410-0", 256,
                        "A história de Bentinho e Capitu, marcada por ciúmes, dúvidas e a famosa 'olhos de ressaca'.",
                        "1234567890123", "/books/Dom Casmurro.png/", new BigDecimal("50.00"), new BigDecimal("27.90"),
                        machado.orElse(null), saraiva.orElse(null), nacional.orElse(null), popular.orElse(null), List.of(literaturaBrasileira.orElse(null)),87),

                new Livro(null, "Harry Potter e a Pedra Filosofal", 1997, "1ª Edição", "978-3-16-148410-0", 223,
                        "O começo da saga do bruxo mais famoso do mundo, que descobre seu destino em Hogwarts.",
                        "1234567890123", "/books/harry potter.webp/", new BigDecimal("50.00"), new BigDecimal("49.90"),
                        jkRowling.orElse(null), happerCollins.orElse(null), suzano.orElse(null), padrao.orElse(null), List.of(fantasia.orElse(null), ficcao.orElse(null)),98),

                new Livro(null, "O Código Da Vinci", 2003, "1ª Edição", "978-3-16-148410-0", 480,
                        "Thriller que mistura arte, simbologia e conspiração com o professor Robert Langdon.",
                        "1234567890123", "/books/Codigo Da Vinci.jpg/", new BigDecimal("50.00"), new BigDecimal("45.00"),
                        danBrown.orElse(null), saraiva.orElse(null), nacional.orElse(null), padrao.orElse(null), List.of(historia.orElse(null)),66),

                new Livro(null, "A Revolução dos Bichos", 1945, "1ª Edição", "978-3-16-148410-0", 152,
                        "Uma fábula satírica sobre o totalitarismo, escrita por George Orwell.",
                        "1234567890123", "/books/Revolucao dos Bichos.jpg/", new BigDecimal("50.00"), new BigDecimal("29.90"),
                        orwell.orElse(null), pandaBooks.orElse(null), independente.orElse(null), popular.orElse(null), List.of(ficcao.orElse(null)),56),

                new Livro(null, "Sapiens: Uma Breve História da Humanidade", 2011, "1ª Edição", "978-3-16-148410-0", 464,
                        "Uma visão impactante sobre a evolução da humanidade desde o Homo sapiens até os dias atuais.",
                        "1234567890123", "/books/sapiens.png/", new BigDecimal("50.00"), new BigDecimal("69.90"),
                        yuval.orElse(null), autentica.orElse(null), suzano.orElse(null), premium.orElse(null), List.of(didatico.orElse(null)),95),

                new Livro(null, "A Cabana", 2007, "1ª Edição", "978-3-16-148410-0", 240,
                        "Um homem reencontra sua fé ao ter um encontro inesperado com Deus após uma tragédia pessoal.",
                        "1234567890123", "/books/A cabana.webp/", new BigDecimal("50.00"), new BigDecimal("39.90"),
                        willianYoung.orElse(null), pandaBooks.orElse(null), nacional.orElse(null), popular.orElse(null), List.of(religiao.orElse(null)),102)
            );

            // Salvando os livros no banco
            livroRepository.saveAll(livros);

            System.out.println("✅ Livros cadastrados automaticamente.");
        }
    }
    
}