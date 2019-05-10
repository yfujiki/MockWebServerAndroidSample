package com.yfujiki.mockserversample

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

private val gson = Gson()

data class MoviesPage (
    val page: Int,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    val results: List<Movie>
) {
    fun toJson() = gson.toJson(this)

    companion object {
        fun fromJson(json: String) = gson.fromJson<MoviesPage>(json, object: TypeToken<MoviesPage>(){}.type)
    }

    val resultsCount: Int
        get() = results.count()
}