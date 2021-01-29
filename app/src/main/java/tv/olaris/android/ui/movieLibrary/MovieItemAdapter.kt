package tv.olaris.android.ui.movieLibrary

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.R
import tv.olaris.android.models.Movie

class MovieItemAdapter(context: Context, movies: List<Movie>, serverId: Int) : RecyclerView.Adapter<MovieItemAdapter.MovieItemHolder>(){
    private val movies = movies
    private val serverId: Int = serverId

    class MovieItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        val textEpisodeCounter: TextView = view.findViewById<TextView>(R.id.text_episode_count)
        val progressBar: ProgressBar = view.findViewById<ProgressBar>(R.id.progress_bar_cover_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.media_list_item_cover_only, parent, false)
        return MovieItemHolder(layout)
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        val m = movies[position]

        if(m.hasStarted()){
            holder.progressBar.progress = m.playProgress().toInt()
        }else{
            holder.progressBar.visibility = View.INVISIBLE
        }

        holder.textEpisodeCounter.visibility = View.INVISIBLE
        Glide.with(holder.itemView.context).load(m.fullPosterUrl()).placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED)).into(holder.movieCoverArt);
        holder.movieCoverArt.transitionName = m.fullPosterUrl()
        holder.movieCoverArt.setOnClickListener{
            val uuid = movies[position].uuid
            val extras = FragmentNavigatorExtras(holder.movieCoverArt to uuid)
            val action = MovieLibraryDirections.actionMovieLibraryFragmentToMovieDetailsFragment(uuid = uuid, serverId = serverId)
            holder.view.findNavController().navigate(action, extras)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}