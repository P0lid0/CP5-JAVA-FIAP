package com.polido.biblioteca.controller;

import com.polido.biblioteca.model.Livro;
import com.polido.biblioteca.model.StatusLivro;
import com.polido.biblioteca.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroRepository livroRepository;

    public LivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @ModelAttribute("statusOptions")
    public StatusLivro[] statusOptions() {
        return StatusLivro.values();
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroRepository.findAll());
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Livro livro = new Livro();
        livro.setStatus(StatusLivro.DISPONIVEL); // padrão
        model.addAttribute("livro", livro);
        return "livros/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("livro", livro);
        return "livros/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("livro") Livro livro,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "livros/form";
        }
        if (livro.getStatus() == null) {
            livro.setStatus(StatusLivro.DISPONIVEL);
        }
        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro salvo com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("livro") Livro livroForm,
                            BindingResult br,
                            RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "livros/form";
        }
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        livro.setTitulo(livroForm.getTitulo());
        livro.setAutor(livroForm.getAutor());
        livro.setAnoPublicacao(livroForm.getAnoPublicacao());
        livro.setStatus(livroForm.getStatus());

        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro atualizado com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/emprestar")
    public String marcarEmprestado(@PathVariable Long id, RedirectAttributes ra) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        livro.setStatus(StatusLivro.EMPRESTADO);
        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro marcado como EMPRESTADO.");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/devolver")
    public String marcarDisponivel(@PathVariable Long id, RedirectAttributes ra) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        livro.setStatus(StatusLivro.DISPONIVEL);
        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro marcado como DISPONÍVEL.");
        return "redirect:/livros";
    }
}
