package br.com.titoschmidt.chucknorrisio.datasource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe que instancia o Retrofit
public class HTTPClient {

    static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(ChuckNorrisAPI.BASE_URL) // = https://api.chucknorris.io/
                // Cria um Json já pronto
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
