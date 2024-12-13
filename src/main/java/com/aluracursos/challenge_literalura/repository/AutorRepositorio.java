package com.aluracursos.challenge_literalura.repository;

import com.aluracursos.challenge_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
}
