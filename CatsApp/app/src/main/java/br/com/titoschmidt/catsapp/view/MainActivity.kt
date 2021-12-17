package br.com.titoschmidt.catsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.titoschmidt.catsapp.R
import br.com.titoschmidt.catsapp.data.CatsDataSource
import br.com.titoschmidt.catsapp.presenter.CatsPresenter
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.main_item.*


class MainActivity : AppCompatActivity() {

    private lateinit var dataSource: CatsDataSource
    private lateinit var presenter: CatsPresenter
    private lateinit var progressBar: ProgressBar
    val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataSource = CatsDataSource()

        presenter = CatsPresenter(this, dataSource)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_main)

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        recyclerView.layoutManager = GridLayoutManager(this,4)

        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        presenter.findAllCats()

    }

    fun showCats(cats: List<MainItem>){
        adapter.addAll(cats)
        adapter.notifyDataSetChanged()

    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    fun showFailure(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}