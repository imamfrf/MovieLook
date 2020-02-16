package com.imamfrf.movielook.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imamfrf.movielook.R
import com.imamfrf.movielook.data.model.Cast
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.utils.Constants.URL_GET_IMAGE
import com.imamfrf.movielook.utils.ViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.runBlocking

class DetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID_EXTRA = "movie_id"
    }

    private lateinit var movie: Movie
    private lateinit var movieId: String
    private lateinit var viewModel: DetailViewModel
    private lateinit var castAdapter: CastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycle.addObserver(youtube_player_view)

        viewModel = obtainViewModel(this)

        if (intent.getStringExtra(MOVIE_ID_EXTRA) != null) {
            movieId = intent.getStringExtra(MOVIE_ID_EXTRA)!!
        }
        subscribeMovieDetail()
    }

    private fun obtainViewModel(activity: FragmentActivity): DetailViewModel {
        // Use a Factory to inject dependencies into the ViewModel
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun subscribeMovieDetail() {
        progress_bar_detail.show()
        val movieObserver = Observer<Movie> {
            movie = it
            subscribeMovieCredits()
        }

        viewModel.getMovieDetails(movieId).observe(this, movieObserver)
    }

    private fun subscribeMovieCredits() {
        val movieObserver = Observer<Movie> {
            movie = it
            subscribeMovieTrailer()
        }
        viewModel.getMovieCredits(movie).observe(this, movieObserver)
    }

    private fun subscribeMovieTrailer() {
        val movieObserver = Observer<Movie> {
            movie = it
            setupUI()
        }
        viewModel.getMovieTrailer(movie).observe(this, movieObserver)
    }

    private fun setupUI() {
        showMovieDetails()
        showMovieCast()
        showTrailer()
        updateFavoriteButton()
        progress_bar_detail.hide()
    }

    private fun showMovieDetails() {
        Glide.with(this).load(URL_GET_IMAGE + movie.backdrop)
            .into(image_backdrop)
        Glide.with(this).load(URL_GET_IMAGE + movie.poster)
            .into(image_poster_detail)
        text_rating_value.text = "${movie.score}/10"
        text_release_date_value.text = movie.releaseDate
        text_overview_value.text = movie.description
        text_title.text = movie.title
        text_genre_value.text = movie.genre
        text_director_value.text = movie.director
    }

    private fun showMovieCast() {
        val castList = arrayListOf<Cast>()
        castList.addAll(movie.casts)
        castAdapter = CastAdapter(castList)
        castAdapter.notifyDataSetChanged()
        recycler_casts.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, RecyclerView.HORIZONTAL, false)
            adapter = castAdapter
        }
    }

    private fun showTrailer() {
        if (movie.trailerVideoId != "null") {
            youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(movie.trailerVideoId, 0f)
                }
            })
        } else {
            text_trailer_title.visibility = View.GONE
            youtube_player_view.visibility = View.GONE
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorited()) {
            button_favorite.setText(R.string.remove_from_favorite)
            button_favorite.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_delete_black_24dp
                ), null, null, null
            )

            button_favorite.setOnClickListener {
                runBlocking {
                    viewModel.deleteFromFavorite(movie)
                }
                Toast.makeText(this, getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT)
                    .show()
                updateFavoriteButton()
            }
        } else {
            button_favorite.setText(R.string.add_to_favorite)
            button_favorite.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_add_black_24dp
                ), null, null, null
            )

            button_favorite.setOnClickListener {
                runBlocking {
                    viewModel.addToFavorite(movie)
                }
                Toast.makeText(this, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT)
                    .show()
                updateFavoriteButton()
            }

        }
    }

    private fun isFavorited(): Boolean {
        var movieList = listOf<Movie>()
        runBlocking {
            movieList = viewModel.getFavoriteMovie(movieId)
        }
        if (movieList.isNotEmpty()) {
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home
            -> {
                finish()
                return true
            }
        }

        return false
    }
}
