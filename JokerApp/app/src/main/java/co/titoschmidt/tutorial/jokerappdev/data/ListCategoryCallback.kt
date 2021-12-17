package co.titoschmidt.tutorial.jokerappdev.data

/*
 * 10/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
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