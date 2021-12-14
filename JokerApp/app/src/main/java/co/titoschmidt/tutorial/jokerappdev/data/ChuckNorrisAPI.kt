package co.titoschmidt.tutorial.jokerappdev.data

import co.titoschmidt.tutorial.jokerappdev.model.Joke
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * 10/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
interface ChuckNorrisAPI {

    @GET("jokes/categories")                                  // Retorna uma lista de strings
    fun findAllCategories(@Query("apiKey") apiKey : String) : Call<List<String>>

    @GET("jokes/random")                                                                                              // Retorna um Joke
    fun findRandom(@Query("category") categoryName: String? = null, @Query("apiKey") apiKey: String = HTTPClient.API_KEY) : Call<Joke>
}