package tv.olaris.android.ui.movieLibrary

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentMovieLibraryBinding

private const val ARG_SERVER_ID = "serverId"
const val movieGridSize = 3

class MovieLibrary : Fragment() {
    private var _binding: FragmentMovieLibraryBinding? = null
    private val binding get() = _binding!!
    private var serverId: Int = 0
    private val viewModel: MovieLibraryViewModel by viewModels()

    companion object {
        fun newInstance() = MovieLibrary()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serverId = it.getInt(ARG_SERVER_ID)
            Log.d("server_id", serverId.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = MovieItemAdapter(requireContext(), serverId)

        binding.movieRecycleview.adapter = adapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.movieRecycleview.layoutManager =
                GridLayoutManager(
                    context,
                    resources.getInteger(R.integer.landscape_library_column_count)
                )
        } else {
            binding.movieRecycleview.layoutManager =
                GridLayoutManager(context, resources.getInteger(R.integer.library_column_count))
        }

        viewModel.dataLoaded.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBarMovieLibrary.visibility = View.INVISIBLE
            } else {
                binding.progressBarMovieLibrary.visibility = View.VISIBLE
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadData(serverId)
    }

}