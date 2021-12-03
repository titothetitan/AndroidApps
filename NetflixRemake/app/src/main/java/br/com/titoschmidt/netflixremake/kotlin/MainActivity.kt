package br.com.titoschmidt.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.titoschmidt.netflixremake.R
import br.com.titoschmidt.netflixremake.model.Category
import br.com.titoschmidt.netflixremake.model.Movie
import br.com.titoschmidt.netflixremake.util.CategoryTask
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MainActivity : AppCompatActivity() {
    // lateinit vai inicializar depois que a Activity ganhar vida
    private lateinit var categoryAdapter : CategoryAdapter

    // ? = aceita Null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        val categoryTask = CategoryTask(this)

        categoryAdapter = CategoryAdapter(categories)

        recycler_view_main.adapter = categoryAdapter
        recycler_view_main.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        // Preencher as categorias
        categoryTask.setCategoryLoader {
            //no Java é como se fosse o método onResult ao implementar a Interface
            categoryAdapter.categories.clear()
            categoryAdapter.categories.addAll(it) // it = categories
            categoryAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class CategoryHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category) = with(itemView) {
            text_view_category_title.text = category.name
            // Toda vez que rolar uma categoria nova cria um adapter de filmes
            recycler_view_movie.adapter = MovieAdapter(category.movies) { movie : Movie ->
                if(movie.id > 3){
                    Toast.makeText(this@MainActivity, "Somente os três primeiros filmes foram implementados.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@MainActivity, MovieActivity::class.java )
                    intent.putExtra("id", movie.id)
                    startActivity(intent)
                }
            }
            recycler_view_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private inner class CategoryAdapter(val categories : MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder = CategoryHolder(
                layoutInflater.inflate(
                    R.layout.category_item,
                    parent,
                    false)
        )

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) = holder.bind(categories[position])

        override fun getItemCount(): Int = categories.size

    }

    private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie : Movie) = with(itemView){
            // Faz o download da imagem da maneira antiga
            //ImageDownloaderTask(itemView.image_view_cover).execute(movie.getCoverUrl());
            Glide
                .with(context)
                .load(movie.coverUrl)
                .placeholder(R.drawable.placeholder_bg)
                .into(image_view_cover)

            itemView.setOnClickListener {
                onClick?.invoke(movie) // Pega o filme atual que foi clicado
            }
        }
    }

    private inner class MovieAdapter(val movies: List<Movie>, private val listener: ((Movie) -> Unit)?) : RecyclerView.Adapter<MovieHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(
                layoutInflater.inflate(R.layout.movie_item,
                    parent,
                    false),
                listener
            )
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie : Movie = movies[position]
            holder.bind(movie)
        }

        override fun getItemCount(): Int = movies.size

    }
}