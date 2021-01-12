package tv.olaris.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentMovieDetailsBinding
import tv.olaris.android.models.Movie
import tv.olaris.android.repositories.MoviesRepository
import javax.inject.Inject


private const val ARG_UUID = "uuid"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieDetails : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var uuid: String? = null

    private var _movie: Movie? = null
    private val movie get() = _movie!!

    @Inject
    lateinit var moviesRepository: MoviesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(ARG_UUID).toString()
            Log.d("uuid", uuid!!)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Rewrite to use binding into resource file


        if(uuid != null) {
            lifecycleScope.launch {
                _movie = moviesRepository.findMovieByUUID(uuid!!)
                binding.textMovieDetailsMovieName.text = movie.title
                binding.textMovieDetailsYearAndRuntime.text = getString(R.string.movie_year_and_runtime, movie.year, movie.uuid)
                binding.textMovieDetailsOverview.text = movie.overview
                Glide.with(view.context).load(movie.fullPosterPath()).into(binding.imageMovieDetailsCover)

                binding.btnPlayContent.setOnClickListener{
                    lifecycleScope.launch{
                        val streamingURL = moviesRepository.getStreamingUrl(movie.fileUUIDs.first())
                        if(streamingURL != null) {
                            val action = MovieDetailsDirections.actionMovieDetailsFragmentToMediaPlayerFragment(
                                    streamingURL = streamingURL
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}