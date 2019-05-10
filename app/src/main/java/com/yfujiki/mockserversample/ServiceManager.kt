package com.yfujiki.mockserversample

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceManager {
    companion object {
        fun tmdbService(): TMDBService {
            val retrofit = Retrofit.Builder()
                .client(client())
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

            return retrofit.create<TMDBService>(TMDBService::class.java)
        }

        fun tmdbServiceForUITest(): TMDBService {
            val retrofit = Retrofit.Builder()
                .client(client())
                .baseUrl("http://127.0.0.1:8000") // Beware of the port number that is occupied on CI etc.
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

            return retrofit.create<TMDBService>(TMDBService::class.java)
        }

        private fun client(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }
    }
}