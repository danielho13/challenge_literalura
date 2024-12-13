package com.aluracursos.challenge_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("title")
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonAlias("authors")
    private Autor autor;

    @ElementCollection
    @CollectionTable(name = "idiomas_libro", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("download_count")
    private int descargas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Título: " + titulo +
                ", Idiomas: " + idiomas +
                ", Descargas: " + descargas +
                ", Autor: " + (autor != null ? autor.getNombre() : "N/A");
    }
}

