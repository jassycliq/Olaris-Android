package tv.olaris.android.ui.movieDetails

import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
    private var server_id: Int = 0
    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(ARG_UUID).toString()
            server_id = it.getInt(ARG_SERVER_ID).toInt()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
         postponeEnterTransition()

        viewModel.coverArtUrl.observe(viewLifecycleOwner){
            Glide.with(view.context).load(it).into(binding.imageMovieDetailsCovertArt)
        }

        viewModel.posterUrl.observe(viewLifecycleOwner){
            Glide.with(view.context).load(it).dontAnimate().listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(binding.imageMovieDetailsPostertArt)
        }

        viewModel.movie.observe(viewLifecycleOwner){
            val movie = it!!

            binding.progressBarMovieItem.visibility = View.INVISIBLE

            binding.textMovieDetailsMovieName.text = movie.title
            binding.textMovieDetailsYearAndRuntime.text = getString(R.string.movie_year_and_runtime, movie.year, movie.getRuntime().toString(), movie.getResolution())
            binding.textMovieDetailsOverview.text = movie.overview
            binding.textMovieDetailsFileName.text = movie.getFileName()
            binding.imageMovieDetailsPostertArt.transitionName = movie.uuid


            binding.btnPlayContent.setOnClickListener{
                val uuid = movie.fileUUIDs.first()

                val action =
                    MovieDetailsDirections.actionMovieDetailsFragmentToFragmentFullScreenMediaPlayer(
                        uuid,
                        server_id,
                        movie.playtime.toInt()

                    )
                findNavController().navigate(action)
            }
        }

        viewModel.getMovie(uuid = uuid!!, server_id = server_id)
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