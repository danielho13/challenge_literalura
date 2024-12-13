package com.aluracursos.challenge_literalura.service;

import com.aluracursos.challenge_literalura.model.Autor;
import com.aluracursos.challenge_literalura.repository.AutorRepositorio;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {
    private final AutorRepositorio autorRepositorio;

    public AutorServicio(AutorRepositorio autorRepositorio) {
        this.autorRepositorio = autorRepositorio;
    }

    public Autor guardarAutor(Autor autor) {
        return autorRepositorio.save(autor);
    }
}
