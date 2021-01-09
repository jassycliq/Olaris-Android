package tv.olaris.android

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.fragments.movieGridSize
import tv.olaris.android.models.Movie
import kotlin.math.floor

class MovieItemAdapter(context: Context, movies: List<Movie>) : RecyclerView.Adapter<MovieItemAdapter.MovieItemHolder>(){
    private val movies = movies

    class MovieItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        //val movieTitle: TextView = view.findViewById<TextView>(R.id.movieTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item_cover_only, parent, false)
        return MovieItemHolder(layout)
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        holder.movieCoverArt.layoutParams.width = floor((Resources.getSystem().displayMetrics.widthPixels / movieGridSize.toFloat())).toInt()
        Glide.with(holder.itemView.context).load(movies[position].posterUrl).into(holder.movieCoverArt);
        //holder.movieCoverArt.setImageURI(movies[position].posterUrl.toUri())
      //  holder.movieTitle.text = "The Matrix"//movies[position]
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}