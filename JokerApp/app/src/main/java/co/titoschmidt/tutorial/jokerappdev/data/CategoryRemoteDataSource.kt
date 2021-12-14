package co.titoschmidt.tutorial.jokerappdev.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import co.titoschmidt.tutorial.jokerappdev.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
 * 10/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class CategoryRemoteDataSource {

    fun findAllCategories(callback: ListCategoryCallback) {
        HTTPClient.retrofit()
            .create(ChuckNorrisAPI::class.java)
            .findAllCategories(HTTPClient.API_KEY)
            .enqueue(object : Callback<List<String>> { // callback do retrofit2!
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if(response.isSuccessful){
                        // devolve uma lista de strings contendo o nome das categorias
                        val categories = response.body()
                        callback.onSuccess(categories ?: emptyList())
                    } else {
                        // erros da chamada
                        val error = response.errorBody()?.toString()
                        callback.onFailure(error ?: "Erro desconhecido")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    // erros do servidor
                    callback.onFailure(t.message ?: "Erro interno")
                    callback.onComplete()
                }
            })
    }
}