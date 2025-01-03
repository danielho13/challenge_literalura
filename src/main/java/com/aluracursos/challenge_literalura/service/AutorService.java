package com.aluracursos.challenge_literalura.service;


import com.aluracursos.challenge_literalura.model.Autor;
import com.aluracursos.challenge_literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutores(){
        return autorRepository.findAllConLibros();
    }
    public List<Autor> listarAutoresVivos (int year){
        return autorRepository.findByAutororesVivosConLibros(year);
    }
    public Autor crearAutor (Autor autor){
        return autorRepository.save(autor);
    }
    public Optional<Autor> obtenerAutorPorId(Long id){
        return autorRepository.findById(id);
    }
    public Optional<Autor> obtenerAutorPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }
    public Autor actualizarAutor(Long id, Autor autorDetalles){
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        autor.setNombre(autorDetalles.getNombre());
        autor.setNacimiento(autorDetalles.getNacimiento());
        autor.setFallecimiento(autorDetalles.getFallecimiento());
        return autorRepository.save(autor);
    }
    public void eliminarAutor(Long id){
        autorRepository.deleteById(id);
    }
}
