package tv.olaris.android.ui.showDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databases.Server
import tv.olaris.android.databinding.FragmentSeasonDetailsBinding
import tv.olaris.android.models.Season
import tv.olaris.android.repositories.ShowsRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SERVER_ID = "serverId"
private const val ARG_SEASON = "seasonUUID"

/**
 * A simple [Fragment] subclass.
 * Use the [SeasonDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SeasonDetailsFragment() : Fragment() {
    var seasonUUID : String = ""
    var serverId: Int = 0
    private var _binding : FragmentSeasonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serverId = it.getInt(ARG_SERVER_ID)
            seasonUUID = it.getString(ARG_SEASON).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeasonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = this.requireContext()

        lifecycleScope.launch {
            val server =
                OlarisApplication.applicationContext().serversRepository.getServerById(serverId)
            val season = ShowsRepository(server).findSeasonByUUID(seasonUUID)
            if(season != null) {
                binding.recyclerEpisodeList.adapter =
                    EpisodeItemAdapter(context, season, server)
                if(resources.getBoolean(R.bool.is_grid)){
                    binding.recyclerEpisodeList.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
                }else{
                    binding.recyclerEpisodeList.layoutManager = LinearLayoutManager(context)
                }

            }
        }
    }
}