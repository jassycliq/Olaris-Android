package tv.olaris.android.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.models.MediaItem

class MediaItemAdapter(context: Context) :

    ListAdapter<MediaItem, MediaItemAdapter.MediaItemHolder>(DiffCallback()) {

    var server: Server? = null

    class MediaItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val coverArt: ImageView = view.findViewById<ImageView>(R.id.movieCoverArtImage)
        val nameText: TextView = view.findViewById<TextView>(R.id.text_media_item_name)
        val subText: TextView = view.findViewById<TextView>(R.id.text_media_item_subtitle)
        val progress: ProgressBar = view.findViewById<ProgressBar>(R.id.progress_bar_media_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        Log.d("dashboard", "onCreate")

        return MediaItemHolder(layout)
    }

    override fun onBindViewHolder(holder: MediaItemHolder, position: Int) {
        val mi = getItem(position)
        Log.d("dashboard", "View holder ${mi.toString()}")
        holder.nameText.text = mi.name

        if(mi.hasStarted()) {
            val p = mi.playProgress()
            Log.d("dashboard", "Progress; $p : ${mi.uuid}")
            holder.progress.progress = p.toInt()
        }else{
            holder.progress.isVisible = false
        }

        holder.subText.text = mi.subTitle
        Glide.with(holder.itemView.context).load(mi.fullPosterUrl(server?.url))
            .placeholder(R.drawable.placeholder_coverart).error(ColorDrawable(Color.RED))
            .into(holder.coverArt);
        holder.itemView.setOnClickListener{
            val action = DashboardDirections.actionDashboardToFragmentFullScreenMediaPlayer(uuid =mi.fileUuid, serverId = server!!.id, playtime = mi.playtime.toInt())
            holder.view.findNavController().navigate(action)
        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}