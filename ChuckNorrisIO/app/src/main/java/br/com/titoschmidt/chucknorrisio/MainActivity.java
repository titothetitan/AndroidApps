package br.com.titoschmidt.chucknorrisio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.List;

import br.com.titoschmidt.chucknorrisio.databinding.ActivityMainBinding;
import br.com.titoschmidt.chucknorrisio.datasource.CategoryRemoteDataSource;
import br.com.titoschmidt.chucknorrisio.models.CategoryItem;
import br.com.titoschmidt.chucknorrisio.presentation.CategoryPresenter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    // GroupAdapter é um adapter superior ao nativo e já está embutido na ViewHolder
    private GroupAdapter adapter;
    private ProgressDialog progressDialog;
    private CategoryPresenter presenter;
    private CategoryRemoteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView rv = findViewById(R.id.rv_main);
        adapter = new GroupAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                // Pega a categoria que o usuário selecionou
                CategoryItem categoryItem = (CategoryItem) item;
                Intent abrirJoke = new Intent(MainActivity.this, JokeActivity.class);
                abrirJoke.putExtra(JokeActivity.CATEGORY_KEY, (categoryItem.getNomeCategoria()));
                startActivity(abrirJoke);
            }
        });

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        dataSource = new CategoryRemoteDataSource();
        presenter = new CategoryPresenter (this, dataSource);
        presenter.requestAll();
    }

    public void exibirFalha(String msgErro){
        Toast.makeText(MainActivity.this, msgErro, Toast.LENGTH_SHORT).show();
    }

    public void exibirProgressBar(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void esconderProgressBar(){
        if(progressDialog != null){
            progressDialog.hide();
        }
    }

    public void showCategories(List<CategoryItem> categoryItems){
        adapter.addAll(categoryItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home){

        } else if(id == R.id.nav_gallery){

        } else if(id == R.id.nav_slideshow){

        } else if(id == R.id.nav_manage){

        } else if(id == R.id.nav_share){

        } else if(id == R.id.nav_send){

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}