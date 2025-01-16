package com.aluracursos.challenge_literalura.principal;

import com.aluracursos.challenge_literalura.dto.AutorDTO;
import com.aluracursos.challenge_literalura.dto.LibroDTO;
import com.aluracursos.challenge_literalura.dto.RespuestaLibrosDTO;
import com.aluracursos.challenge_literalura.model.Autor;
import com.aluracursos.challenge_literalura.model.Libro;
import com.aluracursos.challenge_literalura.service.AutorService;
import com.aluracursos.challenge_literalura.service.ConsumoAPI;
import com.aluracursos.challenge_literalura.service.ConvierteDatos;
import com.aluracursos.challenge_literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvierteDatos convierteDatos;

    private static final String BASE_URL = "https://gutendex.com/books/";

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("------LITERALURA-----");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> buscarLibro(scanner);
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos(scanner);
                case 5 -> listarLibrosPorIdioma(scanner);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private void buscarLibro(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();

        try {
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + encodedTitulo);
            RespuestaLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, RespuestaLibrosDTO.class);

            List<LibroDTO> librosDTO = respuestaLibrosDTO.getLibros();
            if (librosDTO.isEmpty()) {
                System.out.println("Libro no encontrado en la API");
            } else {
                procesarLibrosEncontrados(titulo, librosDTO);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos de la API: " + e.getMessage());
        }
    }

    private void procesarLibrosEncontrados(String titulo, List<LibroDTO> librosDTO) {
        boolean libroRegistrado = false;
        for (LibroDTO libroDTO : librosDTO) {
            if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                Optional<Libro> libroExistente = libroService.obtenerLibroPorTitulo(titulo);
                if (libroExistente.isPresent()) {
                    System.out.println("Detalle: Clave (título)=(" + titulo + ") ya existe");
                    System.out.println("No se puede registrar libros repetidos");
                    libroRegistrado = true;
                } else {
                    registrarLibro(libroDTO);
                    libroRegistrado = true;
                }
                break;
            }
        }

        if (!libroRegistrado) {
            System.out.println("No se encontró un libro con el título " + titulo + " en la API");
        }
    }

    private void registrarLibro(LibroDTO libroDTO) {
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIdioma(libroDTO.getIdiomas().get(0));
        libro.setDescargas(libroDTO.getDescargas());

        AutorDTO primerAutorDto = libroDTO.getAutores().get(0);
        Autor autor = autorService.obtenerAutorPorNombre(primerAutorDto.getNombre())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor();
                    nuevoAutor.setNombre(primerAutorDto.getNombre());
                    nuevoAutor.setNacimiento(primerAutorDto.getNacimiento());
                    nuevoAutor.setFallecimiento(primerAutorDto.getFallecimiento());
                    return autorService.crearAutor(nuevoAutor);
                });

        libro.setAutor(autor);
        libroService.crearLibro(libro);
        System.out.println("Libro registrado: " + libro.getTitulo());
        mostrarDetallesLibro(libroDTO);
    }

    private void listarLibros() {
        libroService.listarLibros().forEach(libro -> {
            System.out.println("-----LIBRO-----");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getDescargas());
        });
    }

    private void listarAutores() {
        autorService.listarAutores().forEach(autor -> {
            System.out.println("-----AUTOR-----");
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getNacimiento());
            System.out.println("Fecha de fallecimiento: " + (autor.getFallecimiento() != null ? autor.getFallecimiento() : "Desconocido"));
            String libros = autor.getLibros().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.joining(", "));
            System.out.println("Libros: [" + libros + "]");
        });
    }

    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Ingrese el año: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autoresVivos = autorService.listarAutoresVivos(ano);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + ano);
        } else {
            autoresVivos.forEach(autor -> {
                System.out.println("-------AUTOR-------");
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " + autor.getNacimiento());
                System.out.println("Fecha de fallecimiento: " + (autor.getFallecimiento() != null ? autor.getFallecimiento() : "Desconocido"));
                System.out.println("Libros: " + autor.getLibros().size());
            });
        }
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.println("Ingrese el idioma (es, en, fr, pt):");
        String idioma = scanner.nextLine();

        if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma) || "pt".equalsIgnoreCase(idioma)) {
            libroService.listarLibroPorIdioma(idioma).forEach(libro -> {
                System.out.println("------LIBRO--------");
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Número de descargas: " + libro.getDescargas());
            });
        } else {
            System.out.println("Idioma no válido. Intente de nuevo.");
        }
    }

    private void mostrarDetallesLibro(LibroDTO libroDTO) {
        System.out.println("------LIBRO--------");
        System.out.println("Título: " + libroDTO.getTitulo());
        System.out.println("Autor: " + (libroDTO.getAutores().isEmpty() ? "Desconocido" : libroDTO.getAutores().get(0).getNombre()));
        System.out.println("Idioma: " + libroDTO.getIdiomas().get(0));
        System.out.println("Número de descargas: " + libroDTO.getDescargas());
    }
}
