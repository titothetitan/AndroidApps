package br.com.titoschmidt.catsapp.presenter

import android.graphics.Color
import br.com.titoschmidt.catsapp.data.CatsCallback
import br.com.titoschmidt.catsapp.data.CatsDataSource
import br.com.titoschmidt.catsapp.model.Cat
import br.com.titoschmidt.catsapp.model.CatsResponse
import br.com.titoschmidt.catsapp.view.MainActivity
import br.com.titoschmidt.catsapp.view.MainItem

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

    override fun onSuccess(response: List<CatsResponse>) {
        val cats = mutableListOf<MainItem>()

        


        view.showCats(cats)
    }

    override fun onFailure(message: String) {
        view.showFailure(message)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}