package tv.olaris.android.ui.showLibrary

import tv.olaris.android.ui.movieLibrary.movieGridSize

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Show

class ShowLibraryAdapter(context: Context, shows: List<Show>, server: Server) : RecyclerView.Adapter<ShowLibraryAdapter.ShowItemHolder>(){
    private val shows = shows
    private val server = server

    class ShowItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        val episodeCount: TextView = view.findViewById<TextView>(R.id.text_episode_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.media_list_item_cover_only, parent, false)
        return ShowItemHolder(layout)
    }

    override fun onBindViewHolder(holder: ShowItemHolder, position: Int) {
        holder.movieCoverArt.layoutParams.width = ((Resources.getSystem().displayMetrics.widthPixels / movieGridSize.toFloat())).toInt() - (12* movieGridSize)
        holder.movieCoverArt.layoutParams.height = (holder.movieCoverArt.layoutParams.width.toFloat() * 1.5).toInt()
        holder.episodeCount.text = shows[position].unwatchedEpisodeCount.toString()
        Glide.with(holder.itemView.context).load(shows[position].fullPosterUrl(server.url)).placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED)).into(holder.movieCoverArt);

        holder.movieCoverArt.setOnClickListener{
            val uuid = shows[position].uuid
            val action = ShowLibraryDirections.actionFragmentShowLibraryToFragmentShowDetails(uuid = uuid, serverId = server.id)
            holder.view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return shows.size
    }
}