package com.fatec.livraria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/{pagina:(?!api).*}") // Evita conflitos com APIs, se houver
    public String renderizarPagina(@PathVariable String pagina) {
        return pagina; // Busca "templates/{pagina}.html"
    }

    @GetMapping("/{dir}/{pagina}") // Para páginas dentro de subpastas
    public String renderizarPaginaComSubpasta(@PathVariable String dir, @PathVariable String pagina) {
        return dir + "/" + pagina; // Busca "templates/{dir}/{pagina}.html"
    }

    @GetMapping("/{dir1}/{dir2}/{pagina}") // Para páginas com duas subpastas
    public String renderizarPaginaComDuasSubpastas(@PathVariable String dir1, @PathVariable String dir2, @PathVariable String pagina) {
        return dir1 + "/" + dir2 + "/" + pagina; // Busca "templates/{dir1}/{dir2}/{pagina}.html"
    }
}
