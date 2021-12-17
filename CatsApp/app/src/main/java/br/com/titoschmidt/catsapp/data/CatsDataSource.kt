package br.com.titoschmidt.catsapp.data

import br.com.titoschmidt.catsapp.model.CatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.lang.RuntimeException

/*
 * 16/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
class CatsDataSource {
    fun findAllCats(callback: CatsCallback) {
        HTTPClient.retrofit()
            .create(API::class.java)
            .findAllCats()
            .enqueue(object : Callback<List<CatsResponse>> {
                override fun onResponse(
                    call: Call<List<CatsResponse>>,
                    response: Response<List<CatsResponse>>
                ) {
                    if(response.isSuccessful){
                        val cats = response.body()
                        callback.onSuccess(cats ?: emptyList())
                    } else {
                        val error = response.errorBody()?.toString()
                        callback.onFailure(error ?: "Erro desconhecido")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<List<CatsResponse>>, t: Throwable) {
                    callback.onFailure(t.message ?: "Erro interno")
                }
            })
    }
}