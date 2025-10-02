package com.polido.biblioteca.repository;

import com.polido.biblioteca.model.Livro;
import com.polido.biblioteca.model.StatusLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.polido.biblioteca.model.StatusLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByStatus(StatusLivro status);
}
