package com.imamfrf.movielook.ui.main.favorite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imamfrf.movielook.R
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.ui.detail.DetailActivity
import com.imamfrf.movielook.ui.main.MainAdapter
import com.imamfrf.movielook.ui.main.MainListener
import com.imamfrf.movielook.ui.main.MainViewModel
import com.imamfrf.movielook.utils.ViewModelFactory
import kotlinx.android.synthetic.main.content_home.view.*
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment(), MainListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var favoriteAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(activity!!)

        setupUI()
        subscribeFavorite()
    }

    private fun obtainViewModel(activity: FragmentActivity): MainViewModel {
        // Use a Factory to inject dependencies into the ViewModel
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProviders.of(activity, factory).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        layout_favorite.text_card_title.text = getString(R.string.your_favorite_movie)
    }

    private fun subscribeFavorite() {
        layout_favorite.progress_bar_home.show()
        val movieObserver = Observer<List<Movie>> {
            val movies = arrayListOf<Movie>()
            movies.addAll(it)
            favoriteAdapter = MainAdapter(movies, this)
            favoriteAdapter.notifyDataSetChanged()
            layout_favorite.recycler_movie_card.apply {
                layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                adapter = favoriteAdapter
            }
            layout_favorite.progress_bar_home.hide()
        }

        viewModel.favoritesMovie.observe(viewLifecycleOwner, movieObserver)
    }

    override fun onItemClick(movie: Movie) {
        val detailIntent = Intent(context, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.MOVIE_ID_EXTRA, movie.id)
        startActivity(detailIntent)
    }
}
