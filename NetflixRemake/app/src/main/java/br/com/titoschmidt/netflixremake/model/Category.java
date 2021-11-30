package br.com.titoschmidt.netflixremake.model;

import java.util.List;

public class Category {

    String nome;
    List<Movie> filmes;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Movie> getFilmes() {
        return filmes;
    }

    public void setFilmes(List<Movie> filmes) {
        this.filmes = filmes;
    }
}
