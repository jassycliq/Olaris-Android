package tv.olaris.android.ui.showLibrary

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentShowLibraryBinding
import tv.olaris.android.repositories.OlarisGraphQLRepository

private const val ARG_SERVER_ID = "serverId"


class ShowLibrary : Fragment() {
    private var _binding: FragmentShowLibraryBinding? = null
    private val binding get() = _binding!!
    lateinit var OlarisGraphQLRepository: OlarisGraphQLRepository
    private var serverId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serverId = it.getInt(ARG_SERVER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentShowLibraryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
       
        val context = this.requireContext()
        lifecycleScope.launch {
            val server =
                OlarisApplication.applicationContext().serversRepository.getServerById(serverId)

            // TODO: Inject this somehow and keep a singleton if that's possible?
            OlarisGraphQLRepository = OlarisGraphQLRepository(server)

            val recyclerView = binding.recyclerShowLibrary

            recyclerView.adapter = ShowLibraryAdapter(context, OlarisGraphQLRepository.getAllShows(), server)

            var spanCount = resources.getInteger(R.integer.library_column_count)
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = resources.getInteger(R.integer.landscape_library_column_count)
            }

            recyclerView.layoutManager =
                GridLayoutManager(context, spanCount)

            binding.progressBarShowLibrary.visibility = View.INVISIBLE
        }
    }
}