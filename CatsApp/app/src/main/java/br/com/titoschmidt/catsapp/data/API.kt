package br.com.titoschmidt.catsapp.data

import br.com.titoschmidt.catsapp.model.Cat
import br.com.titoschmidt.catsapp.model.CatsResponse
import retrofit2.Call
import retrofit2.http.GET

/*
 * 15/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
interface API {
    @GET("3/gallery/search/?q=cats")
    fun findAllCats() : Call<CatsResponse>
}