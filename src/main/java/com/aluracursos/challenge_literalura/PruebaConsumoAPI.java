package com.aluracursos.challenge_literalura.main;

import com.aluracursos.challenge_literalura.model.Autor;
import com.aluracursos.challenge_literalura.model.Libro;
import com.aluracursos.challenge_literalura.service.AutorServicio;
import com.aluracursos.challenge_literalura.service.LibroServicio;
import com.aluracursos.challenge_literalura.service.ConsumoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PruebaConsumoAPI implements CommandLineRunner {

    private final ConsumoAPI consumoAPI;
    private final AutorServicio autorServicio;
    private final LibroServicio libroServicio;

    @Autowired
    public PruebaConsumoAPI(ConsumoAPI consumoAPI, AutorServicio autorServicio, LibroServicio libroServicio) {
        this.consumoAPI = consumoAPI;
        this.autorServicio = autorServicio;
        this.libroServicio = libroServicio;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando prueba de consumo de la API...");

        // Buscar libros por título
        String titulo = "Shakespeare";
        List<Libro> libros = consumoAPI.buscarLibrosPorTitulo("Shakespeare");


        // Persistir los datos en la base de datos
        for (Libro libro : libros) {
            Autor autor = libro.getAutor();

            // Guardar autor si no existe en la base de datos
            Autor autorPersistido = autorServicio.guardarAutor(autor);

            // Asignar el autor persistido al libro
            libro.setAutor(autorPersistido);

            // Guardar libro en la base de datos
            libroServicio.guardarLibro(libro);

            System.out.println("Libro guardado: " + libro);
        }

        System.out.println("Prueba finalizada.");
    }
}
