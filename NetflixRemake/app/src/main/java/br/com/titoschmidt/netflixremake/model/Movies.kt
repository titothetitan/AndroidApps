package br.com.titoschmidt.netflixremake.model

/*
 * 03/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 *
 */
class Movies {

    data class Category(var name: String = "",
                         var movies: List<Movie> = arrayListOf())

    data class Movie(var id: Int = 0,
                     var title: String = "",
                     var description: String = "",
                     var cast: String = "",
                     var coverUrl: String = "")

    data class MovieDetail(var movie: Movie,
                           var movieSimilar: List<Movie>)
}