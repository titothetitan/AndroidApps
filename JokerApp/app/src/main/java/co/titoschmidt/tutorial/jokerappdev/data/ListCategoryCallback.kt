package co.titoschmidt.tutorial.jokerappdev.data

/*
 * 10/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
interface ListCategoryCallback {

    fun onSuccess(response: List<String>){

    }

    fun onFailure(response : String){

    }

    fun onComplete(){

    }
}