package com.imamfrf.movielook.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
@Entity(tableName = "favorite_movie")
data class Movie(
    @ColumnInfo(name = "id") @PrimaryKey var id: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: String = "",
    @ColumnInfo(name = "score") var score: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "poster") var poster: String = "",
    @ColumnInfo(name = "backdrop") var backdrop: String = "",
    @Ignore var genre: String = "",
    @Ignore var director: String = "",
    @Ignore var casts: List<Cast> = arrayListOf(),
    @Ignore var trailerVideoId: String = ""
) : Parcelable {

    constructor(`object`: JSONObject) : this(
        `object`.getString("id"),
        `object`.getString("title"),
        `object`.getString("release_date"),
        `object`.getString("vote_average"),
        `object`.getString("overview"),
        `object`.getString("poster_path"),
        `object`.getString("backdrop_path")
    )
}