package co.titoschmidt.tutorial.jokerappdev.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.titoschmidt.tutorial.jokerappdev.data.CategoryRemoteDataSource
import co.titoschmidt.tutorial.jokerappdev.presenter.HomePresenter
import com.xwray.groupie.GroupieAdapter
import titoschmidt.tutorial.jokerappdev.R

/*
 * 07/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
class HomeFragment : Fragment() {

    private lateinit var homePresenter: HomePresenter
    private lateinit var dataSource: CategoryRemoteDataSource
    val adapter = GroupieAdapter()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = CategoryRemoteDataSource()
        homePresenter = HomePresenter(this, dataSource)
    }

    // Infla a View fragment_home
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    // Com a View já inflada, no onViewCreated tem-se referência da view e seus elementos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        // como o fragment está dentro da activity então deve-se usar requireContext()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        if(adapter.itemCount==0){
            homePresenter.findAllCategories()
        }

        adapter.setOnItemClickListener { item, view ->
            val bundle = Bundle()
            val categoryName = (item as CategoryItem).category.name
            bundle.putString("category_key", categoryName)
            findNavController().navigate(R.id.action_nav_home_to_nav_joke, bundle)
        }
    }

    fun showCategories(categories: List<CategoryItem>){
        adapter.addAll(categories)
        adapter.notifyDataSetChanged()
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}