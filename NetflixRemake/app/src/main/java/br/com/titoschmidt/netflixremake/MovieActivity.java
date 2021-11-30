package br.com.titoschmidt.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.titoschmidt.netflixremake.model.Movie;
import br.com.titoschmidt.netflixremake.model.MovieDetail;
import br.com.titoschmidt.netflixremake.util.ImageDownloaderTask;
import br.com.titoschmidt.netflixremake.util.MovieDetailTask;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private TextView txtTitulo;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitulo = findViewById(R.id.text_view_titulo);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        recyclerView = findViewById(R.id.recycler_view_similar);
        imgCover = findViewById(R.id.image_view_cover);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Seta a toolbar para torná-la compatível com todos os dispositivos
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            // Exibe o ícone Home
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Exibe o ícone o ícone Voltar
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            // Remove o título da Toolbar
            getSupportActionBar().setTitle(null);
        }

        // obs: ContextCompact possui vários métodos estáticos para tornar dispositivos compatíveis

        /* O código abaixo está acessando uma propriedade em shadows.xml

               <item android:id="@+id/cover_drawable" android:drawable="@drawable/movie_4"/>

           e trocando a propriedade desenhável (movie_4) para outra */

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if(drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);
            //((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(drawable);
        }

        List<Movie> movies = new ArrayList<>();

        movieAdapter = new MovieAdapter(movies);

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int id = extras.getInt("id");
            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this); // o listener é a MovieActivity(this) que vai escutar os eventos da Tela Inicial (MainActivity)
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/"+id);
        }
    }
    //Button Voltar <--
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        txtTitulo.setText(movieDetail.getMovie().getTitle());
        txtDesc.setText(movieDetail.getMovie().getDescription());
        txtCast.setText(movieDetail.getMovie().getCast());

        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(imgCover);
        imageDownloaderTask.setShadowEnabled(true);
        imageDownloaderTask.execute(movieDetail.getMovie().getCoverUrl());

        movieAdapter.setMovies(movieDetail.getMovieSimilar());
        movieAdapter.notifyDataSetChanged();
    }

    // Classe de ViewHolder para gerenciar o layout do filme
    private class MovieHolder extends RecyclerView.ViewHolder{
        final ImageView imageViewCover;

        // Construtor
        public MovieHolder(@NonNull View itemView) {
            // itemView é o container dinâmico principal da célula movie_item, ou seja, o constraintlayout de movie_item.xml
            // que foi definido em '.inflate(R.layout.movie_item'
            super(itemView);
            // como eu inflei o movie_item.xml no MovieAdapter, eu posso obter com findViewById os elementos dentro do movie_item.xml!
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
        }
    }

    // Classe de Adapter vai gerenciar a MovieHolder
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>{

        private final List<Movie> filmes;

        private MovieAdapter(List<Movie> filmes) {
            this.filmes = filmes;
        }

        public void setMovies(List<Movie> filmes){
            this.filmes.clear();
            this.filmes.addAll(filmes);
        }

        @NonNull
        @Override
        // no OnCreate informa-se qual layout.xml será exibido e manipulado, aqui no caso será movie_item.xml
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false)); // false, pois a raiz é o constraintlayout
        }

        @Override
        // Pega o ID do filme durante a rolagem
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie filme = filmes.get(position);
            new ImageDownloaderTask(holder.imageViewCover).execute(filme.getCoverUrl());
        }

        @Override
        // Retorna a quantidade de filmes
        public int getItemCount() {
            return filmes.size();
        }
    }
}