package com.polido.biblioteca.controller;

import com.polido.biblioteca.model.Emprestimo;
import com.polido.biblioteca.model.Livro;
import com.polido.biblioteca.repository.EmprestimoRepository;
import com.polido.biblioteca.repository.LivroRepository;
import com.polido.biblioteca.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoController(EmprestimoRepository emprestimoRepository,
                                LivroRepository livroRepository,
                                UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Lista todos os empréstimos
    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("emprestimos", emprestimoRepository.findAll());
        return "emprestimos/lista";
    }

    // Lista apenas os ativos
    @GetMapping("/ativos")
    public String listarAtivos(Model model) {
        model.addAttribute("emprestimos", emprestimoRepository.findByDataDevolucaoIsNull());
        return "emprestimos/lista";
    }

    // Formulário novo empréstimo
    @GetMapping("/novo")
    public String novo(Model model) {
        Emprestimo e = new Emprestimo();
        e.setDataRetirada(LocalDate.now());
        model.addAttribute("emprestimo", e);
        model.addAttribute("livros", livroRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "emprestimos/form";
    }

    // Salvar empréstimo
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Emprestimo emprestimo) {
        // seta livro emprestado
        Livro livro = livroRepository.findById(emprestimo.getLivro().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        livro.setStatus(com.polido.biblioteca.model.StatusLivro.EMPRESTADO);
        livroRepository.save(livro);

        emprestimoRepository.save(emprestimo);
        return "redirect:/emprestimos/ativos";
    }

    // Devolver empréstimo
    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id) {
        Emprestimo emp = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Define data de devolução
        emp.setDataDevolucao(LocalDate.now());

        // Atualiza livro para DISPONÍVEL
        Livro livro = emp.getLivro();
        livro.setStatus(com.polido.biblioteca.model.StatusLivro.DISPONIVEL);
        livroRepository.save(livro);

        emprestimoRepository.save(emp);

        return "redirect:/emprestimos/ativos";
    }
}
