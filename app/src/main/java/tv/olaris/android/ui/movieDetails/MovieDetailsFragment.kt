package tv.olaris.android.ui.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentMovieDetailsBinding
import tv.olaris.android.models.Movie
import tv.olaris.android.repositories.MoviesRepository


private const val ARG_UUID = "uuid"
private const val ARG_SERVER_ID = "server_id"

class MovieDetails : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var uuid: String? = null

    private var _movie: Movie? = null
    private val movie get() = _movie!!

    private var server_id: Int = 0

    lateinit var moviesRepository: MoviesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(ARG_UUID).toString()
            server_id = it.getInt(ARG_SERVER_ID).toInt()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Rewrite to use binding into resource file

        if(uuid != null) {
            lifecycleScope.launch {
                val server = OlarisApplication.applicationContext().serversRepository.getServerById(server_id)
                moviesRepository = MoviesRepository(server)
                _movie = moviesRepository.findMovieByUUID(uuid!!)

                binding.textMovieDetailsMovieName.text = movie.title
                binding.textMovieDetailsYearAndRuntime.text = getString(R.string.movie_year_and_runtime, movie.year, movie.uuid)
                binding.textMovieDetailsOverview.text = movie.overview

                val imageUrl = movie.fullPosterPath(server.url)
                Glide.with(view.context).load(imageUrl).into(binding.imageMovieDetailsCover)

                binding.btnPlayContent.setOnClickListener{
                    lifecycleScope.launch{
                        val streamingURL = moviesRepository.getStreamingUrl(movie.fileUUIDs.first())
                        if(streamingURL != null) {
                            val action =
                                MovieDetailsDirections.actionMovieDetailsFragmentToMediaPlayerFragment(
                                    streamingURL = streamingURL,
                                    imageUrl = imageUrl
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