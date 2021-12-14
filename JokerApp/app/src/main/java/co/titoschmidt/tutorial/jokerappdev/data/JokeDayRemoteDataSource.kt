package co.titoschmidt.tutorial.jokerappdev.data

import co.titoschmidt.tutorial.jokerappdev.model.Joke
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

/*
 * 14/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class JokeDayRemoteDataSource {
    fun findRandom(callback: JokeCallback) {
        HTTPClient.retrofit()
            .create(ChuckNorrisAPI::class.java)
            .findRandom()
            .enqueue(object : Callback<Joke> { // callback do retrofit2!
                override fun onResponse(
                    call: Call<Joke>,
                    response: Response<Joke>
                ) {
                    if(response.isSuccessful){
                        val joke = response.body()
                        callback.onSuccess(joke ?: throw RuntimeException("Piada não encontrada."))
                    } else {
                        // erros da chamada
                        val error = response.errorBody()?.toString()
                        callback.onFailure(error ?: "Erro desconhecido")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<Joke>, t: Throwable) {
                    // erros do servidor
                    callback.onFailure(t.message ?: "Erro interno")
                    callback.onComplete()
                }

            })
    }
}