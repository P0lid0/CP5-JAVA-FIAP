package com.polido.biblioteca.controller;

import com.polido.biblioteca.model.StatusLivro;
import com.polido.biblioteca.repository.EmprestimoRepository;
import com.polido.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    public HomeController(EmprestimoRepository emprestimoRepository,
                          LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("emprestimosAtivos", emprestimoRepository.findByDataDevolucaoIsNull());
        model.addAttribute("livrosDisponiveis", livroRepository.findByStatus(StatusLivro.DISPONIVEL));
        return "index";
    }
}
