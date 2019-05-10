package com.yfujiki.mockserversample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable = CompositeDisposable()

    private lateinit var adapter: MoviesAdapter

    override fun onDestroy() {
        super.onDestroy()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidthPixels = displayMetrics.widthPixels
        val density = getResources().getDisplayMetrics().density
        val marginPixels = (16 * density).toInt()
        val posterWidthPixels = screenWidthPixels / 2 - marginPixels
        val posterHeightPixels = (posterWidthPixels / 0.675).toInt() // TMDB specifies poster image in 0.675 x 1 (width x height)

        adapter = MoviesAdapter(Size(posterWidthPixels, posterHeightPixels))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        disposable.add(MockWebServerSampleApp.getInstance().service.nowplaying(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesPage ->
                adapter.addMovies(moviesPage.results)
                adapter.notifyDataSetChanged()
            }, { error ->
                Log.e("", "Failed to obtain movies : $error")
            }))
    }
}
