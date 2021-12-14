package co.titoschmidt.tutorial.jokerappdev.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import co.titoschmidt.tutorial.jokerappdev.data.JokeRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.model.Joke
import co.titoschmidt.tutorial.jokerappdev.presenter.JokePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import titoschmidt.tutorial.jokerappdev.R

/*
 * 13/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class JokeFragment : Fragment() {

    private lateinit var jokePresenter: JokePresenter
    private lateinit var jokeDataSource: JokeRemoteDataSource
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView

    companion object{
        const val CATEGORY_KEY = "category_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jokeDataSource = JokeRemoteDataSource()
        jokePresenter = JokePresenter(this, jokeDataSource)
    }
    // Infla a View fragment_joke
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_joke, container, false)
    }
    // Com a View já inflada, no onViewCreated tem-se referência da view e seus elementos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName = arguments?.getString(CATEGORY_KEY)

        progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_joke)

        textView = view.findViewById<TextView>(R.id.text_joke)

        imageView = view.findViewById<ImageView>(R.id.img_joke)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            if (categoryName != null) {
                jokePresenter.findBy(categoryName)
            }
        }

        if (categoryName != null) {
            jokePresenter.findBy(categoryName)
            activity?.findViewById<Toolbar>(R.id.toolbar)?.title = categoryName
        }
    }

    fun showJoke(joke : Joke){
        textView.text = joke.text
        Picasso.get().load(joke.iconUrl).into(imageView)
    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    fun showFailure(mensagem : String){
        Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
    }
}