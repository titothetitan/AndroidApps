package br.com.titoschmidt.catsapp.data

import br.com.titoschmidt.catsapp.model.CatsResponse

/*
 * 16/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
interface CatsCallback {

    fun onSuccess(response: CatsResponse)

    fun onFailure(response: String)

    fun onComplete()
}