package tv.olaris.android.ui.movieLibrary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databinding.MovieLibraryFragmentBinding
import tv.olaris.android.repositories.MoviesRepository

private const val ARG_SERVER_ID = "server_id"
const val movieGridSize = 3

class MovieLibrary : Fragment() {
    private var _binding : MovieLibraryFragmentBinding? = null
    private val binding get() = _binding!!
    private var server_id : Int = 0
    lateinit var moviesRepository: MoviesRepository

    companion object {
        fun newInstance() = MovieLibrary()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            server_id = it.getInt(ARG_SERVER_ID).toInt()
            Log.d("server_id", server_id.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieLibraryFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recyclerView = binding.movieRecycleview
        val context = this.requireContext()

        lifecycleScope.launch {
            val server = OlarisApplication.applicationContext().serversRepository.getServerById(server_id)
            moviesRepository = MoviesRepository(server)

            recyclerView.adapter = MovieItemAdapter(context, moviesRepository.getAllMovies(), server)
            recyclerView.layoutManager = GridLayoutManager(context, movieGridSize)// spanCount.toInt())//LinearLayoutManager(this.requireContext()) //
        }

        /*recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL)
        )*/
    }

}