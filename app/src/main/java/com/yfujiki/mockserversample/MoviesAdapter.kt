package com.yfujiki.mockserversample

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_holder.view.*
import kotlin.properties.Delegates

class MoviesAdapter(val posterImageSize: Size): RecyclerView.Adapter<CardViewHolder>() {
    private var movies = mutableListOf<Movie>()

    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_holder, parent, false)
        configureImageViewSize(itemView)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies.count()
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.movie = movies[position]
    }

    private fun configureImageViewSize(itemView: View) {
        val layoutParams = itemView.imageView.layoutParams

        layoutParams.width = posterImageSize.width
        layoutParams.height = posterImageSize.height

        itemView.imageView.layoutParams = layoutParams
    }
}

class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var movie: Movie by Delegates.observable(Movie.default()) { _, _, newValue ->
        val movie = newValue
        itemView.textView.text = movie.title
        Picasso.get().load(movie.posterFullPath()).into(itemView.imageView)
    }
}