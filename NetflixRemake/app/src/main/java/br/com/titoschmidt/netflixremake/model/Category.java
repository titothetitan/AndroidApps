package br.com.titoschmidt.netflixremake.model;

import java.util.List;

public class Category {

    String nome;
    List<Movie> movies;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
