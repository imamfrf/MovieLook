package com.imamfrf.movielook.data.local

import androidx.lifecycle.LiveData
import com.imamfrf.movielook.data.local.room.MovieLookDao
import com.imamfrf.movielook.data.model.Movie

class LocalRepository(val movieLookDao: MovieLookDao) {
    companion object {
        private var INSTANCE: LocalRepository? = null
        fun getInstance(movieLookDao: MovieLookDao): LocalRepository {
            if (INSTANCE == null) {
                INSTANCE = LocalRepository(movieLookDao)
            }
            return INSTANCE!!
        }
    }

    fun getAllFavoriteMovie(): LiveData<List<Movie>> {
        return movieLookDao.getAllFavoriteMovie()
    }

    suspend fun getFavoriteMovie(id: String): List<Movie> {
        return movieLookDao.getFavoriteMovie(id)
    }

    suspend fun addFavoriteMovie(movie: Movie) {
        movieLookDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieLookDao.deleteMovie(movie)
    }

}