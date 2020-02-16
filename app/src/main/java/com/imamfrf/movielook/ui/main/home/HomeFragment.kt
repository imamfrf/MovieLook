package com.imamfrf.movielook.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imamfrf.movielook.R
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.ui.detail.DetailActivity
import com.imamfrf.movielook.ui.main.MainAdapter
import com.imamfrf.movielook.ui.main.MainListener
import com.imamfrf.movielook.ui.main.MainViewModel
import com.imamfrf.movielook.utils.ViewModelFactory
import kotlinx.android.synthetic.main.content_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(),
    MainListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var nowPlayingAdapter: MainAdapter
    private lateinit var popularAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(activity!!)

        setupUI()
        subscribeNowPlaying()
        subscribePopular()
    }

    private fun obtainViewModel(activity: FragmentActivity): MainViewModel {
        // Use a Factory to inject dependencies into the ViewModel
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    private fun setupUI(){
        layout_now_playing.text_card_title.text = getString(R.string.now_playing)
        layout_popular_movie.text_card_title.text = getString(R.string.popular)
    }

    private fun subscribeNowPlaying(){
        layout_now_playing.progress_bar_home.show()
        val movieObserver = Observer<List<Movie>>{
            val movies = arrayListOf<Movie>()
            movies.addAll(it)
            showNowPlayingList(movies)
            layout_now_playing.progress_bar_home.hide()
        }

        viewModel.nowPlayingMovie.observe(viewLifecycleOwner, movieObserver)
    }

    private fun subscribePopular(){
        layout_popular_movie.progress_bar_home.show()
        val movieObserver = Observer<List<Movie>>{
            val movies = arrayListOf<Movie>()
            movies.addAll(it)
            showPopularList(movies)
            layout_popular_movie.progress_bar_home.hide()
        }

        viewModel.popularMovie.observe(viewLifecycleOwner, movieObserver)
    }

    private fun showNowPlayingList(movies: ArrayList<Movie>){
        nowPlayingAdapter = MainAdapter(movies, this)
        nowPlayingAdapter.notifyDataSetChanged()
        layout_now_playing.recycler_movie_card.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = nowPlayingAdapter
        }
    }

    private fun showPopularList(movies: ArrayList<Movie>){
        popularAdapter = MainAdapter(movies, this)
        popularAdapter.notifyDataSetChanged()
        layout_popular_movie.recycler_movie_card.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = popularAdapter
        }
    }

    override fun onItemClick(movie: Movie) {
        val detailIntent = Intent(context, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.MOVIE_ID_EXTRA, movie.id)
        startActivity(detailIntent)
    }
}
