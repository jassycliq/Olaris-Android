package tv.olaris.android.ui.mediaPlayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.MimeTypes
import tv.olaris.android.MainActivity
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentFullScreenMediaPlayerBinding
import tv.olaris.android.databinding.FragmentMediaPlayerBinding
import tv.olaris.android.util.disableFullscreen
import tv.olaris.android.util.enableFullscreen

private const val ARG_UUID = "uuid"
private const val ARG_SERVERID = "serverId"
private const val ARG_PLAYTIME = "playtime"

class MediaPlayerFragment : Fragment() {
       private var currentWindow = 0
    private var serverId: Int = 0
    private var uuid: String  = ""
    private var playbackPosition: Int = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private val viewModel : MediaPlayerViewModel by viewModels()

    private lateinit var dataSourceFactory: DataSource.Factory
    private var _binding: FragmentFullScreenMediaPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            serverId = it.getInt(ARG_SERVERID)
            uuid = it.getString(ARG_UUID).toString()
            playbackPosition = it.getInt(ARG_PLAYTIME)
        }


        _binding = FragmentFullScreenMediaPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //This can force landscape
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        viewModel.player.observe(this) { player ->
            Log.d("mediaplyer", "Got a player!")
            binding.exoPlayerFullScreen.player = player
            if (player == null) parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.exoPlayerFullScreen.player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        with(requireActivity()){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            disableFullscreen(true)
        }
    }

    override fun onPause() {
        viewModel.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        requireActivity().enableFullscreen()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.player.observe(viewLifecycleOwner, {
            Log.d("mediaplyer", "Got a player part two")
            binding.exoPlayerFullScreen.player = it
        })

        viewModel.getStreamingUrl(serverId, uuid).observe(viewLifecycleOwner, { streamingUrl ->
            Log.d("mediaplayer", streamingUrl)
            viewModel.play(streamingUrl, playbackPosition.toLong())
        })
    }

/*
    private fun initPlayer(context: Context, streamingUrl: String){
        val mi = MediaItem.Builder()
                .setUri(streamingUrl)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()

        val exoPlayer = SimpleExoPlayer.Builder(context).setTrackSelector(DefaultTrackSelector(context)).build().apply {
            playWhenReady = isPlayerPlaying
            setHasOptionsMenu(true)
            setMediaItem(mi, false)
            seekTo(currentWindow, playbackPosition)
            videoScalingMode = Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            prepare()
        }
        binding.exoPlayerFullScreen.player = exoPlayer
    }*/
}