package tv.olaris.android.ui.showDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.R
import tv.olaris.android.databinding.FragmentShowDetailsBinding
import tv.olaris.android.repositories.ShowsRepository

private const val ARG_UUID = "uuid"
private const val ARG_SERVER_ID = "serverId"

class ShowDetailsFragment : Fragment() {
    private var _binding : FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private var uuid: String? = null
    private var serverId: Int = 0

    private lateinit var seasonPageAdapter: SeasonPagerAdapter
    private lateinit var viewPager: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(ARG_UUID).toString()
            serverId = it.getInt(ARG_SERVER_ID).toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = this

        lifecycleScope.launch{

            val server = OlarisApplication.applicationContext().serversRepository.getServerById(serverId)

            if(uuid != null) {
               val show = ShowsRepository(server).findShowByUUID(uuid!!)

                if(show != null) {
                    seasonPageAdapter = SeasonPagerAdapter(fragment, show)
                    viewPager = view.findViewById(R.id.pager_show_detail_seasons)
                    viewPager.adapter = seasonPageAdapter

                    val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_season_list)

                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        tab.text = show.seasons[position].name
                    }.attach()

                    binding.progressBarShowItem.visibility = View.INVISIBLE
                    binding.textShowDetailsAirDate.text = show.firstAirDate
                    binding.textShowDetailsShowName.text = show.name
                    binding.textShowDetailsOverview.text = show.overview
                    val imageUrl = show.fullPosterUrl(server.url)
                    Glide.with(view.context).load(imageUrl).into(binding.imageViewShowPoster)
                    Glide.with(view.context).load(show.fullCoverArtUrl(server.url)).into(binding.imageViewCoverArt
                    )

                }
            }
        }
    }

}