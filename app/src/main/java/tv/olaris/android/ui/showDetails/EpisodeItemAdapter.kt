package tv.olaris.android.ui.showDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fragment.EpisodeBase
import fragment.SeasonBase
import org.w3c.dom.Text
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.ui.showLibrary.ShowLibraryAdapter
import kotlin.random.Random

class EpisodeItemAdapter(context: Context, val seasonBase: SeasonBase) : RecyclerView.Adapter<EpisodeItemAdapter.EpisodeItemHolder>(){

    class EpisodeItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val episodeTitle: TextView = view.findViewById(R.id.text_episode_title)
        val episodeDescription: TextView = view.findViewById(R.id.text_episode_description)
        val episodeDetails: TextView = view.findViewById(R.id.text_episode_number)
        val episodeStillImage: ImageView = view.findViewById(R.id.image_view_episode_item_cover)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar_episode_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.season_list_item, parent, false)
        return EpisodeItemHolder(layout)
    }


    override fun getItemCount(): Int {
     return seasonBase.episodes.size
    }

    override fun onBindViewHolder(holder: EpisodeItemHolder, position: Int) {
        val episode = seasonBase.episodes[position]?.fragments?.episodeBase
        if(episode != null){
            holder.episodeTitle.text = episode.name
            holder.episodeDescription.text = episode.overview
            holder.progressBar.progress = Random.nextInt(0, 100)
            holder.episodeDetails.text = "Episode ${episode.episodeNumber.toString()} - ${episode.airDate}"
            Glide.with(holder.itemView.context).load("http://maran.atalanta.bysh.me/olaris/m/images/tmdb/w1280/${episode.stillPath}").placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED)).into(holder.episodeStillImage);
        }

    }

}