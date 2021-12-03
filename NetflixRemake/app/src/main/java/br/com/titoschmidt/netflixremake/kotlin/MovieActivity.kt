package br.com.titoschmidt.netflixremake.kotlin

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.titoschmidt.netflixremake.R
import br.com.titoschmidt.netflixremake.model.Movie
import br.com.titoschmidt.netflixremake.model.MovieDetail
import br.com.titoschmidt.netflixremake.util.ImageDownloaderTask
import br.com.titoschmidt.netflixremake.util.MovieDetailTask
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_movie.view.*
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.movie_item_similar.view.*

class MovieActivity : AppCompatActivity() {

    private lateinit var movieAdapter : MovieAdapter
    private lateinit var imgCover : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val extras : Bundle ?= intent.extras
        if(extras != null){
            val id : Int = extras.getInt("id")
            val movieDetailTask = MovieDetailTask(this)
            movieDetailTask.setMovieDetailLoader { movieDetail : MovieDetail ->
                text_view_movie_title.text = movieDetail.movie.title
                text_view_desc.text = movieDetail.movie.description
                text_view_cast.text = movieDetail.movie.cast

                /* Carrega a imagem da maneira antiga
                ImageDownloaderTask(image_view_cover).apply {
                    setShadowEnabled(true)
                    execute(movieDetail.movie.coverUrl)
                }
                */
                // Carrega a imagem usando Glide
                Glide
                    .with(this)
                    .load(movieDetail.movie.coverUrl)
                     // Escuta os eventos de download
                     // Intercepta a imagem vinda e adiciona-a no shadows.xml para receber efeito de sombreamento
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val drawable: LayerDrawable? = ContextCompat.getDrawable(baseContext, R.drawable.shadows) as LayerDrawable?
                            drawable?.let {
                                drawable.setDrawableByLayerId(R.id.cover_drawable, resource)
                                (target as DrawableImageViewTarget).view.setImageDrawable(drawable)
                            }
                            return true
                        }

                    })
                    .into(image_view_cover)

                movieAdapter.movies.clear()
                movieAdapter.movies.addAll(movieDetail.movieSimilar)
                movieAdapter.notifyDataSetChanged()
            }
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/$id")

            // Seta a toolbar para torná-la compatível com todos os dispositivos
            setSupportActionBar(toolbar)

            supportActionBar?. let { toolbar : ActionBar ->
                toolbar.setDisplayHomeAsUpEnabled(true)
                toolbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
                toolbar.setTitle(null)
            }

            val movies = arrayListOf<Movie>()
            movieAdapter = MovieAdapter(movies)
            recycler_view_similar.adapter = movieAdapter
            recycler_view_similar.layoutManager = GridLayoutManager(this,3)
        }
    }
    //Button Voltar <--
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private class MovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie : Movie){
            with(itemView) {
                //ImageDownloaderTask(itemView.image_view_cover).execute(movie.coverUrl)
                Glide
                    .with(context)
                    .load(movie.coverUrl)
                    .placeholder(R.drawable.placeholder_bg)
                    .into(image_view_similar_covers)
            }
        }
    }

    private inner class MovieAdapter(val movies : MutableList<Movie>) : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(
                layoutInflater.inflate(
                    R.layout.movie_item_similar,
                    parent,
                false
                )
            )
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie : Movie = movies[position]
            holder.bind(movie)
        }

        override fun getItemCount(): Int {
            return movies.size
        }
    }
}