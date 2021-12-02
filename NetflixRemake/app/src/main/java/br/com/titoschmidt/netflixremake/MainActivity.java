package br.com.titoschmidt.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.titoschmidt.netflixremake.model.Category;
import br.com.titoschmidt.netflixremake.model.Movie;
import br.com.titoschmidt.netflixremake.util.CategoryTask;
import br.com.titoschmidt.netflixremake.util.ImageDownloaderTask;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);

        List<Category> categories = new ArrayList<>();

        mainAdapter = new MainAdapter(categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mainAdapter);

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }
    // Método vindo da interface CategoryTask.CategoryLoader para popular as categorias
    @Override
    public void onResult(List<Category> categories) {
        mainAdapter.setCategories(categories);
        mainAdapter.notifyDataSetChanged();
    }

    // Classe de ViewHolder para gerenciar o layout de Categorias
    private class CategoryHolder extends RecyclerView.ViewHolder{

        TextView textViewtitulo;
        RecyclerView recyclerViewMovie;

        public CategoryHolder(@NonNull View itemView) {                        // obs: Uma CategoryHolder vai ter n MovieHolders dentro dela!
            // itemView é o container dinâmico principal da célula category_item, ou seja, o constraintlayout de category_item.xml
            // que foi definido no MainAdapter em '.inflate(R.layout.movie_item'
            super(itemView);
            // como eu inflei o category_item.xml no MainAdapter, eu posso obter com findViewById os elementos dentro do category_item.xml!
            textViewtitulo = itemView.findViewById(R.id.text_view_category_title);
            recyclerViewMovie = itemView.findViewById(R.id.recycler_view_movie);
        }
    }

    // Classe que vai gerenciar a CategoryHolder
    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder>{

        private List<Category> categories;

        private MainAdapter(List<Category> categorias) {
            this.categories = categorias;
        }

        @NonNull
        @Override
        // no OnCreate informa-se qual layout.xml será exibido e manipulado, aqui no caso será category_item.xml
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false)); // false, pois a raiz é o constraintlayout
        }

        @Override
        // Pega o ID da categoria durante a rolagem
        // Enquanto rola na vertical, o OnBind vai desenhando categoria com filmes na horizontal
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category categoria = categories.get(position);
            holder.textViewtitulo.setText(categoria.getNome());
            holder.recyclerViewMovie.setAdapter(new MovieAdapter(categoria.getMovies()));
            holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        // Retorna a quantidade de categorias
        public int getItemCount() {
            return categories.size();
        }

        public void setCategories(List<Category> categories) {
            this.categories.clear();
            this.categories.addAll(categories);
        }
    }

    // Classe de ViewHolder para gerenciar o layout do filme
    private static class MovieHolder extends RecyclerView.ViewHolder{
        final ImageView imageViewCover;

        // Construtor
        public MovieHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            // itemView é o container dinâmico principal da célula movie_item, ou seja, o constraintlayout de movie_item.xml
            // que foi definido em '.inflate(R.layout.movie_item'
            super(itemView);
            // como eu inflei o movie_item.xml no MovieAdapter, eu posso obter com findViewById os elementos dentro do movie_item.xml!
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Pega o ID do filme atual que foi clicado
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }


    // Classe para gerenciar a MovieHolder e os cliques nos filmes
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> implements OnItemClickListener{

        private final List<Movie> filmes;

        private MovieAdapter(List<Movie> filmes) {
            this.filmes = filmes;
        }

        @NonNull
        @Override
        // no OnCreate informa-se qual layout.xml será exibido e manipulado, aqui no caso será movie_item.xml
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false); // false, pois a raiz é o constraintlayout
            return new MovieHolder(view,this);
        }

        @Override
        // Pega o ID do filme durante a rolagem
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie filme = filmes.get(position);
            // Busca a imagem no servidor durante a rolagem e preenche a view
            new ImageDownloaderTask(holder.imageViewCover).execute(filme.getCoverUrl());
        }

        @Override
        // Retorna a quantidade de filmes
        public int getItemCount() {
            return filmes.size();
        }

        @Override
        public void onClick(int position) {
            // Passa o ID do filme clicado para a outra Activity
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra("id", filmes.get(position).getId());
            startActivity(intent);
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }
}