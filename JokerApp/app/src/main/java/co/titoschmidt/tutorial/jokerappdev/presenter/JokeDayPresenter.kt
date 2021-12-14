package co.titoschmidt.tutorial.jokerappdev.presenter

import co.titoschmidt.tutorial.jokerappdev.data.JokeCallback
import co.titoschmidt.tutorial.jokerappdev.data.JokeDayRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.data.JokeRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.model.Joke
import co.titoschmidt.tutorial.jokerappdev.view.JokeDayFragment
import co.titoschmidt.tutorial.jokerappdev.view.JokeFragment

/*
 * 14/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class JokeDayPresenter (
    private val view: JokeDayFragment,
    private val jokeDayRemoteDataSource: JokeDayRemoteDataSource
    ) : JokeCallback
    {
        fun findRandom() {
            view.showProgressBar()
            jokeDayRemoteDataSource.findRandom(this)
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
