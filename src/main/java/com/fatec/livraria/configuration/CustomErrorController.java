package com.fatec.livraria.configuration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = null;
    
        if (statusObj instanceof Integer) {
            statusCode = (Integer) statusObj;
        } else if (statusObj != null) {
            try {
                statusCode = Integer.valueOf(statusObj.toString());
            } catch (NumberFormatException e) {
                // deixa como null
            }
        }
    
        model.addAttribute("statusCode", statusCode);
    
        // Pega a exceção completa
        Throwable excecao = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (excecao != null) {
            model.addAttribute("mensagemErro", excecao.getMessage());
        } else {
            Object mensagem = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
            model.addAttribute("mensagemErro", mensagem != null ? mensagem.toString() : "Erro desconhecido");
        }

        return "erro/erro";
    }
}
