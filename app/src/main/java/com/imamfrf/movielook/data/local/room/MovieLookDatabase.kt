package com.imamfrf.movielook.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.imamfrf.movielook.data.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieLookDatabase : RoomDatabase(){
    companion object {
        @Volatile
        private var INSTANCE: MovieLookDatabase? = null

        fun getInstance(context: Context): MovieLookDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieLookDatabase::class.java,
                    "favorite_db"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun getMovieLookDao(): MovieLookDao
}