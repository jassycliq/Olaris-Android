package tv.olaris.android.ui.showLibrary

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Show

class ShowLibraryAdapter(context: Context, private val shows: List<Show>, private val server: Server) : RecyclerView.Adapter<ShowLibraryAdapter.ShowItemHolder>(){
    class ShowItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val movieCoverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        val episodeCount: TextView = view.findViewById<TextView>(R.id.text_episode_count)
        val progressBar: ProgressBar = view.findViewById<ProgressBar>(R.id.progress_bar_cover_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.media_list_item_cover_only, parent, false)
        return ShowItemHolder(layout)
    }

    override fun onBindViewHolder(holder: ShowItemHolder, position: Int) {
        val s = shows[position]
        holder.progressBar.visibility = View.INVISIBLE
        holder.episodeCount.text = s.unwatchedEpisodeCount.toString()
        Glide.with(holder.itemView.context).load(s.fullPosterUrl()).placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED)).into(holder.movieCoverArt);

        holder.movieCoverArt.setOnClickListener{
            val uuid = s.uuid
            val action = ShowLibraryDirections.actionFragmentShowLibraryToFragmentShowDetails(uuid = uuid, serverId = server.id)
            holder.view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return shows.size
    }
}