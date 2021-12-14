package co.titoschmidt.tutorial.jokerappdev.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 * 10/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
// Classe que seta e instancia o Retrofit
object HTTPClient {

    private const val BASE_URL = "https://api.tiagoaguiar.co/jokerapp/"

    const val API_KEY = "56417e15-6f63-46a1-acf4-6d9039c5b7be"

    fun retrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Cria um Json já pronto
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()
    }
    // função para exibir um Log mais detalhado
    private fun httpClient() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}