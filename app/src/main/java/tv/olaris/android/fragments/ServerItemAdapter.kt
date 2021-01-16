package tv.olaris.android.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databases.Server

class ServerItemAdapter(context: Context) : ListAdapter<Server, ServerItemAdapter.ServerItemHolder>(DiffCallback()) {
    class ServerItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val serverUrl = view.findViewById<TextView>(R.id.text_server_label)
        val deleteServerIcon = view.findViewById<ImageView>(R.id.icon_delete_server)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerItemHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.server_card, parent, false)
        return ServerItemHolder(layout)
    }

    override fun onBindViewHolder(holder: ServerItemHolder, position: Int) {
        holder.serverUrl.text = getItem(position).name
        holder.deleteServerIcon.setOnClickListener{
            OlarisApplication.applicationContext().applicationScope.launch {
                OlarisApplication.applicationContext().serversRepository.deleteServer(getItem(position))
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Server>() {
    override fun areItemsTheSame(oldItem: Server, newItem:Server): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItem:Server, newItem: Server): Boolean {
        return oldItem == newItem
    }
}