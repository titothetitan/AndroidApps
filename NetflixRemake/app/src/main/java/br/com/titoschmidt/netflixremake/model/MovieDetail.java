package br.com.titoschmidt.netflixremake.model;

import java.util.List;

public class MovieDetail {

    private final Movie movie;
    private final List<Movie> movieSimilar;

    public MovieDetail(Movie movie, List<Movie> movieSimilar) {
        this.movie = movie;
        this.movieSimilar = movieSimilar;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Movie> getMovieSimilar() {
        return movieSimilar;
    }
}
