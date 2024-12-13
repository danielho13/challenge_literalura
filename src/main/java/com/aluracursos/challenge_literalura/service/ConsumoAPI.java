package com.aluracursos.challenge_literalura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aluracursos.challenge_literalura.model.Libro;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public class ConsumoAPI {

    private static final String BASE_URL = "https://gutendex.com/books/";
    private final HttpClient clienteHttp;

    public ConsumoAPI() {
        this.clienteHttp = HttpClient.newHttpClient();
    }

    public List<Libro> buscarLibrosPorTitulo(String titulo) {
        try {
            String url = BASE_URL + "?search=" + titulo;
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> respuesta = clienteHttp.send(solicitud, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode nodoRaiz = mapper.readTree(respuesta.body());
            JsonNode resultados = nodoRaiz.get("results");

            // Mapear los resultados a una lista de libros
            return mapper.readerForListOf(Libro.class).readValue(resultados);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
