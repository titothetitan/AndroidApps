package co.titoschmidt.tutorial.jokerappdev.presenter

import co.titoschmidt.tutorial.jokerappdev.data.JokeCallback
import co.titoschmidt.tutorial.jokerappdev.data.JokeRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.model.Joke
import co.titoschmidt.tutorial.jokerappdev.view.JokeFragment

/*
 * 13/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class JokePresenter(
    private val view: JokeFragment,
    private val jokeDataSource: JokeRemoteDataSource
    ) : JokeCallback {

    fun findBy(category: String) {
        view.showProgressBar()
        jokeDataSource.findBy(this, category)
    }

    override fun onSuccess(response: Joke) {
        view.showJoke(response)
    }

    override fun onFailure(response: String) {
        view.showFailure(response)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}