package tv.olaris.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tv.olaris.android.MovieItemAdapter
import tv.olaris.android.databinding.MovieLibraryFragmentBinding
import tv.olaris.android.repositories.MoviesRepository
import javax.inject.Inject

const val movieGridSize = 3

@AndroidEntryPoint
class MovieLibrary : Fragment() {
    private var _binding : MovieLibraryFragmentBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var moviesRepository: MoviesRepository

    companion object {
        fun newInstance() = MovieLibrary()
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
            recyclerView.adapter = MovieItemAdapter(context, moviesRepository.getAllMovies())
            recyclerView.layoutManager = GridLayoutManager(context, movieGridSize)// spanCount.toInt())//LinearLayoutManager(this.requireContext()) //
        }

        /*recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL)
        )*/
    }

}