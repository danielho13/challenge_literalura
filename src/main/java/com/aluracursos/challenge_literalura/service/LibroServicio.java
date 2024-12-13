package com.aluracursos.challenge_literalura.service;

import com.aluracursos.challenge_literalura.model.Libro;
import com.aluracursos.challenge_literalura.repository.LibroRepositorio;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {
    private final LibroRepositorio libroRepositorio;

    public LibroServicio(LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepositorio.save(libro);
    }
}
