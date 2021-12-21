package br.com.titoschmidt.catsapp.data

import br.com.titoschmidt.catsapp.model.CatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            .enqueue(object : Callback<CatsResponse> {
                override fun onResponse(
                    call: Call<CatsResponse>,
                    response: Response<CatsResponse>
                ) {
                    if(response.isSuccessful){
                        val cat = response.body()
                        cat?.let { callback.onSuccess(it) }
                    } else {
                        // erros da chamada
                        val error = response.errorBody()?.toString()
                        callback.onFailure("call error: " + error ?: "Erro desconhecido")
                    }
                    callback.onComplete()
                }
                // erros do servidor
                override fun onFailure(call: Call<CatsResponse>, t: Throwable) {
                    callback.onFailure("server error: " + t.message ?: "Erro interno")
                }
            })
    }
}