package br.com.titoschmidt.chucknorrisio.datasource;

import java.util.List;

import br.com.titoschmidt.chucknorrisio.models.Joke;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChuckNorrisAPI {

    static final String BASE_URL = "https://api.chucknorris.io/";

    @GET("jokes/categories")
    Call<List<String>> findAll();
    // o Retrofit converte o Json para a classe Joke
    @GET ("jokes/random")
    Call<Joke> findRandomBy(@Query("category") String category);
}
