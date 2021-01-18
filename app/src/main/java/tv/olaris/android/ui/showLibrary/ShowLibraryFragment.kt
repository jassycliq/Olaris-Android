package tv.olaris.android.ui.showLibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentShowLibraryBinding
import tv.olaris.android.repositories.MoviesRepository
import tv.olaris.android.repositories.ShowsRepository
import tv.olaris.android.ui.movieLibrary.movieGridSize

private const val ARG_SERVER_ID = "serverId"


class ShowLibrary : Fragment() {
    private var _binding: FragmentShowLibraryBinding? = null
    private val binding get() = _binding!!
    lateinit var showsRepository: ShowsRepository
    private var serverId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serverId = it.getInt(ARG_SERVER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
            showsRepository = ShowsRepository(server)

            val recyclerView = binding.recyclerShowLibrary
            recyclerView.adapter = ShowLibraryAdapter(context, showsRepository.getAllShows(), server)
            recyclerView.layoutManager = GridLayoutManager(context, movieGridSize)
            binding.progressBarShowLibrary.visibility = View.INVISIBLE
        }
    }
}