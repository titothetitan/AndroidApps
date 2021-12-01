package br.com.titoschmidt.netflixremake.kotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.titoschmidt.netflixremake.MainActivity
import br.com.titoschmidt.netflixremake.R
import br.com.titoschmidt.netflixremake.model.Category
import br.com.titoschmidt.netflixremake.model.Movie
import br.com.titoschmidt.netflixremake.util.CategoryTask
import br.com.titoschmidt.netflixremake.util.ImageDownloaderTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var categories = arrayListOf<Category>()

        mainAdapter = MainAdapter(categories)

        recycler_view_main.adapter = mainAdapter
        recycler_view_main.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader { categories ->
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(categories)
            mainAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories : MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(layoutInflater.inflate(
                    R.layout.category_item,
                    parent,
                    false))
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            var category : Category = categories[position]
            holder.vincula(category)
        }

        override fun getItemCount(): Int = categories.size
    }

    private inner class MovieAdapter(val movies : List<Movie>) : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false))
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            var movie : Movie = movies[position]
            holder.vincula(movie)
        }

        override fun getItemCount(): Int = movies.size
    }

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun vincula(category: Category) {
            itemView.text_view_title.text = category.nome
            itemView.recycler_view_movie.adapter = MovieAdapter(category.filmes)
            itemView.recycler_view_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun vincula(movie : Movie){
            ImageDownloaderTask(itemView.image_view_cover).execute(movie.coverUrl)
            itemView.image_view_cover.setOnClickListener{

            }
        }
    }
}