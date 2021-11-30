package br.com.titoschmidt.chucknorrisio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import br.com.titoschmidt.chucknorrisio.databinding.ActivityJokeBinding;
import br.com.titoschmidt.chucknorrisio.datasource.JokeRemoteDataSource;
import br.com.titoschmidt.chucknorrisio.models.Joke;
import br.com.titoschmidt.chucknorrisio.presentation.JokePresenter;

public class JokeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityJokeBinding binding;
    private JokeRemoteDataSource dataSource;
    private JokePresenter presenter;

    static final String CATEGORY_KEY = "category_key";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJokeBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_joke);

        setSupportActionBar(binding.toolbar);

        if(getIntent().getExtras() != null){
            String category = getIntent().getExtras().getString(CATEGORY_KEY);
            Log.d("cat", category);
            if(category != null) {
                // Altera o texto da ActionBar
                getSupportActionBar().setTitle(category);
                // Exibe a seta de retorno <-
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                dataSource = new JokeRemoteDataSource();
                presenter = new JokePresenter(this, dataSource);
                presenter.findJokeBy(category);

                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.findJokeBy(category);
                    }
                });
            }
        }
    }

    public void mostraJoke(Joke joke){
        TextView txtJoke = findViewById(R.id.txt_joke);
        txtJoke.setText(joke.getValue());
    }

    public void mostraFalha(String msgErro){
        Toast.makeText(JokeActivity.this, msgErro, Toast.LENGTH_SHORT).show();
    }

    public void mostraProgressBar(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void escondeProgressBar(){
        if(progressDialog != null){
            progressDialog.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }
}