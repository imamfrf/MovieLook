package com.imamfrf.movielook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imamfrf.movielook.data.local.LocalRepository
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.data.remote.RemoteRepository

class MovieLookRepository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    companion object {
        @Volatile
        private var INSTANCE: MovieLookRepository? = null

        fun getInstance(remoteRepository: RemoteRepository, localRepository: LocalRepository) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieLookRepository(remoteRepository, localRepository).also {
                    INSTANCE = it
                }
            }

    }

    fun getNowPlaying(): LiveData<List<Movie>> {
        val moviesLiveData = MutableLiveData<List<Movie>>()
        remoteRepository.getNowPlaying(object : RemoteRepository.GetNowPlayingCallback {
            override fun onSuccess(movies: List<Movie>) {
                moviesLiveData.postValue(movies)
            }
        })

        return moviesLiveData
    }

    fun getPopular(): LiveData<List<Movie>> {
        val moviesLiveData = MutableLiveData<List<Movie>>()
        remoteRepository.getPopular(object : RemoteRepository.GetPopularCallback {
            override fun onSuccess(movies: List<Movie>) {
                moviesLiveData.postValue(movies)
            }
        })

        return moviesLiveData
    }

    fun getMovieDetails(id: String): LiveData<Movie> {
        val movieLiveData = MutableLiveData<Movie>()
        remoteRepository.getMovieDetails(id, object : RemoteRepository.GetMovieDetailsCallback {
            override fun onSuccess(movie: Movie) {
                movieLiveData.postValue(movie)
            }
        })

        return movieLiveData
    }

    fun getMovieCredits(movie: Movie): LiveData<Movie> {
        val movieLiveData = MutableLiveData<Movie>()
        remoteRepository.getMovieCredits(movie, object : RemoteRepository.GetMovieCreditsCallback {
            override fun onSuccess(movie: Movie) {
                movieLiveData.postValue(movie)
            }
        })

        return movieLiveData
    }

    fun getMovieTrailer(movie: Movie): LiveData<Movie> {
        val movieLiveData = MutableLiveData<Movie>()
        remoteRepository.getMovieTrailer(movie, object : RemoteRepository.GetMovieTrailerCallback {
            override fun onSuccess(movie: Movie) {
                movieLiveData.postValue(movie)
            }
        })

        return movieLiveData
    }

    fun getAllFavoriteMovie(): LiveData<List<Movie>> {
        return localRepository.getAllFavoriteMovie()
    }

    suspend fun getFavoriteMovie(id: String): List<Movie> {
        return localRepository.getFavoriteMovie(id)
    }

    suspend fun insertFavoriteMovie(movie: Movie) {
        localRepository.addFavoriteMovie(movie)
    }

    suspend fun deleteFavoriteMovie(movie: Movie) {
        localRepository.deleteMovie(movie)
    }
}