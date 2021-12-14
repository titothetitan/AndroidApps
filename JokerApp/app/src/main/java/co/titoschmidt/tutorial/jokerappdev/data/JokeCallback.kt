package co.titoschmidt.tutorial.jokerappdev.data

import co.titoschmidt.tutorial.jokerappdev.model.Joke

/*
 * 13/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
interface JokeCallback {

    fun onSuccess(response : Joke){

    }

    fun onFailure(response : String){

    }

    fun onComplete(){

    }
}