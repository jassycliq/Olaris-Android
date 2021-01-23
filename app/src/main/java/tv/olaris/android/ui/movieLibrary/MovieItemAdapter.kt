package tv.olaris.android.ui.movieLibrary

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Movie

class MovieItemAdapter(context: Context, movies: List<Movie>, server: Server) : RecyclerView.Adapter<MovieItemAdapter.MovieItemHolder>(){
    private val movies = movies
    private val server = server

    class MovieItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        val textEpisodeCounter: TextView = view.findViewById<TextView>(R.id.text_episode_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.media_list_item_cover_only, parent, false)
        return MovieItemHolder(layout)
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        holder.movieCoverArt.layoutParams.width = ((Resources.getSystem().displayMetrics.widthPixels / movieGridSize.toFloat())).toInt() - (12* movieGridSize)
        holder.movieCoverArt.layoutParams.height = (holder.movieCoverArt.layoutParams.width.toFloat() * 1.5).toInt()
        holder.textEpisodeCounter.visibility = View.INVISIBLE
        Glide.with(holder.itemView.context).load(movies[position].fullPosterUrl(server.url)).placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED)).into(holder.movieCoverArt);
        holder.movieCoverArt.transitionName = movies[position].fullPosterUrl(server.url)
        holder.movieCoverArt.setOnClickListener{
            val uuid = movies[position].uuid
            val extras = FragmentNavigatorExtras(holder.movieCoverArt to uuid)
            val action = MovieLibraryDirections.actionMovieLibraryFragmentToMovieDetailsFragment(uuid = uuid, serverId = server.id)
            holder.view.findNavController().navigate(action, extras)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}