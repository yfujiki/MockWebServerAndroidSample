package com.yfujiki.mockserversample

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

private val gson = Gson()

data class Movie(
    val id: Int,

    @SerializedName("poster_path")
    val posterPath: String?,

    val title: String
) {
    fun toJson() = gson.toJson(this)

    companion object {
        fun fromJson(json: String) = gson.fromJson<Movie>(json, object: TypeToken<Movie>(){}.type)

        fun default() = Movie(
            id = -1,
            posterPath = null,
            title = ""
        )
    }

    fun posterFullPath() : String? {
        posterPath?.let {
            return "https://image.tmdb.org/t/p/w500/${it}"
        } ?: run {
            return null
        }
    }
}