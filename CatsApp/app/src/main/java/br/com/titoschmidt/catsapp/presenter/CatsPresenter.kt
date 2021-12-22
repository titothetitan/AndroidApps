package br.com.titoschmidt.catsapp.presenter

import android.util.Log
import br.com.titoschmidt.catsapp.data.CatsCallback
import br.com.titoschmidt.catsapp.data.CatsDataSource
import br.com.titoschmidt.catsapp.model.Cat
import br.com.titoschmidt.catsapp.model.CatsResponse
import br.com.titoschmidt.catsapp.view.MainActivity

/*
 * 16/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
class CatsPresenter(
    private val view: MainActivity,
    private val dataSource: CatsDataSource) : CatsCallback {

    fun findAllCats() {
        view.showProgressBar()
        dataSource.findAllCats(this)
    }

    override fun onSuccess(response: CatsResponse) {
        /*
        val cats = mutableListOf<Cat>()

        for(cat in response)
            cats.add(Cat(cat))


        for(cat in response){
            cats.add(Cat(cat))
        }
        */
        //Log.d("cats", response.cats.toString())
        view.showCats(response.cats)
    }

    override fun onFailure(message: String) {
        view.showFailure(message)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}