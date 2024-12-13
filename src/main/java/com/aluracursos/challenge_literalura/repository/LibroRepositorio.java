package com.aluracursos.challenge_literalura.repository;

import com.aluracursos.challenge_literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
}