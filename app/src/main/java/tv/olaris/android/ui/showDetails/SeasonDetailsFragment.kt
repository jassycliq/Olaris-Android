package tv.olaris.android.ui.showDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentSeasonDetailsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
 const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SeasonDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SeasonDetailsFragment(season: fragment.SeriesBase.Season) : Fragment() {
    private val season = season
    private var _binding : FragmentSeasonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeasonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.recyclerEpisodeList.adapter = EpisodeItemAdapter(this.requireContext(), season.fragments.seasonBase)
        binding.recyclerEpisodeList.layoutManager = LinearLayoutManager(this.requireContext())
    }
}