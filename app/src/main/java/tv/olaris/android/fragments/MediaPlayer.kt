package tv.olaris.android.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import tv.olaris.android.databinding.FragmentMediaPlayerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_STREAMINGURL = "streamingURL"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayer.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayer : Fragment() {
    // TODO: Rename and change types of parameters
    private var streamingURL: String? = null
    private var param2: String? = null
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private lateinit var dataSourceFactory: DataSource.Factory
    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!



    private lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            streamingURL = it.getString(ARG_STREAMINGURL)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ua = Util.getUserAgent(view.context, "olaris-android")
        initPlayer(view.context)
    }
    private fun initPlayer(context: Context){
       val mi = MediaItem.Builder()
            .setUri(streamingURL)
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()

        exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
            playWhenReady = isPlayerPlaying

            setMediaItem(mi, false)
            seekTo(currentWindow, playbackPosition)
            prepare()
        }

        binding.simpleExoPlayerView.player = exoPlayer
    }
}