package tv.olaris.android

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.databases.Server
import tv.olaris.android.fragments.MovieLibraryDirections
import tv.olaris.android.fragments.movieGridSize
import tv.olaris.android.models.Movie
import kotlin.math.floor

class MovieItemAdapter(context: Context, movies: List<Movie>, server: Server) : RecyclerView.Adapter<MovieItemAdapter.MovieItemHolder>(){
    private val movies = movies
    private val server = server

    class MovieItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item_cover_only, parent, false)
        return MovieItemHolder(layout)
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        holder.movieCoverArt.layoutParams.width = floor((Resources.getSystem().displayMetrics.widthPixels / movieGridSize.toFloat())).toInt()

        Glide.with(holder.itemView.context).load(movies[position].fullPosterPath(server.url)).placeholder(ColorDrawable(Color.YELLOW)).error(ColorDrawable(Color.RED)).into(holder.movieCoverArt);

        holder.movieCoverArt.setOnClickListener{
            val uuid = movies[position].uuid
            val action = MovieLibraryDirections.actionMovieLibraryFragmentToMovieDetailsFragment(uuid = uuid, serverId = server.id)
            holder.view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}