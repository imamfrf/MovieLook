package com.imamfrf.movielook.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class Cast(
    var id: String,
    var character: String,
    var name: String,
    var photo: String
) : Parcelable {

    constructor(`object`: JSONObject) : this(
        `object`.getString("cast_id"),
        `object`.getString("character"),
        `object`.getString("name"),
        `object`.getString("profile_path")
    )
}