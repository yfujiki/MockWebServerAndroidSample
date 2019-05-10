package com.yfujiki.wiremocksample

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    @GET("3/movie/now_playing")
    fun nowplaying(@Query("page") page: Int,
                 @Query("api_key") apiKey: String = "caf895c3e8d7b13a7a9b70ca2a9cfd6e"
    ): Flowable<MoviesPage>
}