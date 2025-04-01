package com.fatec.livraria.service;

import com.fatec.livraria.entity.Editora;
import com.fatec.livraria.repository.EditoraRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditoraService {
    private final EditoraRepository editoraRepository;

    public EditoraService(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    @PostConstruct
    public void inicializarEditoras() {
        if (editoraRepository.count() == 0) {
            List<Editora> editoras = List.of(
                new Editora(null, "HarperCollins"),
                new Editora(null, "Saraiva"),
                new Editora(null, "PandaBooks"),
                new Editora(null, "Autêntica")
            );
            editoraRepository.saveAll(editoras);
            System.out.println("✅ Editoras cadastradas automaticamente.");
        }
    }
}
