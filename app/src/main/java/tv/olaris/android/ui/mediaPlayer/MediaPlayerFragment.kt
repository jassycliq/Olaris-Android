package tv.olaris.android.ui.mediaPlayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.*
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
private const val ARG_MEDIA_UUID = "mediaUuid"

class MediaPlayerFragment : Fragment() {
    private var currentWindow = 0
    private var serverId: Int = 0
    private var uuid: String = ""
    private var mediaUuid: String = ""
    private var playbackPosition: Int = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private val viewModel: MediaPlayerViewModel by viewModels()

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
            mediaUuid = it.getString(ARG_MEDIA_UUID).toString()
            playbackPosition = it.getInt(ARG_PLAYTIME)
        }
        activity?.actionBar?.setDisplayShowTitleEnabled(false)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        activity?.actionBar?.hide()

        _binding = FragmentFullScreenMediaPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //This can force landscape
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        viewModel.player.observe(this) { player ->
            Log.d("mediaplyer", "Got a player!")
            if (player == null) parentFragmentManager.popBackStack()

            binding.exoPlayerFullScreen.player = player
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.player.observe(viewLifecycleOwner, {
            Log.d("mediaplyer", "Got a player part two")
            binding.exoPlayerFullScreen.player = it
            binding.exoPlayerFullScreen.player!!.addListener(PlayerListener(this.requireActivity().window))
        })

        viewModel.getStreamingUrl(serverId, uuid).observe(viewLifecycleOwner, { streamingUrl ->
            Log.d("mediaplayer", streamingUrl)
            viewModel.play(streamingUrl, mediaUuid, playbackPosition.toLong())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.exoPlayerFullScreen.player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        with(requireActivity()) {
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

}