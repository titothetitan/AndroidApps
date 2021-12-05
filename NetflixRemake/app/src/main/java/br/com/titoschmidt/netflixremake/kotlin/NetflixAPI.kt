package br.com.titoschmidt.netflixremake.kotlin

import br.com.titoschmidt.netflixremake.model.Categories
import br.com.titoschmidt.netflixremake.model.MovieDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/*
 * 05/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */

    interface NetflixAPI {

        @GET("home")
        // Retorna uma lista de categorias
        fun listCategories(): Call<Categories>

        @GET("{id}")
        fun getMovieById(@Path("id") id: Int) : Call<MovieDetail>

    }
    // Constrói a requisição
    fun retrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl("https://tiagoaguiar.co/api/netflix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /*

    Chamada da requisição em MainActivity

    retrofit().create(NetflixAPI::class.java)
                .listCategories()         // Categories é o data model criado em Movies.kt
                .enqueue(object : Callback<Categories> {
                    override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                       if(response.isSuccessful){
                           response.body()?.let {
                               categoryAdapter.categories.clear()
                               categoryAdapter.categories.addAll(it.categories) // it = categories
                               categoryAdapter.notifyDataSetChanged()
                           }
                       }
                    }

                    override fun onFailure(call: Call<Categories>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                });

     */