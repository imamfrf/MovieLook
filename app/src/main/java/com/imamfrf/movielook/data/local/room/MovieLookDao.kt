package com.imamfrf.movielook.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imamfrf.movielook.data.model.Movie

@Dao
interface MovieLookDao {
    @Insert
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM favorite_movie")
    fun getAllFavoriteMovie(): LiveData<List<Movie>>

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    suspend fun getFavoriteMovie(id: String): List<Movie>

}