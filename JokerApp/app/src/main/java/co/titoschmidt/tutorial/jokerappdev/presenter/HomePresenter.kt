package co.titoschmidt.tutorial.jokerappdev.presenter

import android.graphics.Color
import co.titoschmidt.tutorial.jokerappdev.view.HomeFragment
import co.titoschmidt.tutorial.jokerappdev.data.CategoryRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.data.ListCategoryCallback
import co.titoschmidt.tutorial.jokerappdev.model.Category
import co.titoschmidt.tutorial.jokerappdev.view.CategoryItem

/*
 * 09/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
class HomePresenter(
    private val view: HomeFragment,
    private val dataSource: CategoryRemoteDataSource
    ) : ListCategoryCallback { // implementa a interface

    fun findAllCategories(){
        view.showProgress()
        dataSource.findAllCategories(this)
    }

    // A resposta espera uma lista strings e depois a converte para ViewHolder (CategoryItem) - os dóis códigos abaixo fazem a mesma coisa
    override fun onSuccess(response: List<String>){
        val categories = mutableListOf<CategoryItem>()

        val start = 40
        val end = 190
        val diff = (end - start)  / response.size

        response.forEachIndexed { i, category ->
            val hsv = floatArrayOf(
                start + (diff * i).toFloat(),
                100.0f,
                100.0f,
            )
            val objCategory = Category(category, Color.HSVToColor(hsv).toLong())
            categories.add(CategoryItem(objCategory))
        }
        /*
        val categories = response.map { Category(it, 0xffface6e) }
        */

        view.showCategories(categories)
    }

    override fun onFailure(message : String){
        view.showFailure(message)
    }

    override fun onComplete(){
        view.hideProgress()
    }
}

// Simula uma requisição
/*fun fakeRequest(){
    Handler(Looper.getMainLooper()).postDelayed({
        val response = arrayListOf(
            Category("categoria a", 0xffface6e),
            Category("categoria b", 0xffface6e),
            Category("categoria c", 0xffface6e),
            Category("categoria d", 0xffface6e)
        )
        onSuccess(response)

        onComplete()
    },2000)
}*/