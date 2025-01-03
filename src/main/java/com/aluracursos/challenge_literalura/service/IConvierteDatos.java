package com.aluracursos.challenge_literalura.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String Json, Class<T> clase);
}
