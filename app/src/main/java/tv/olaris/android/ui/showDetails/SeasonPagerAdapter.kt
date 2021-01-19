package tv.olaris.android.ui.showDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tv.olaris.android.models.Show


class SeasonPagerAdapter(fragment: Fragment, show: Show) : FragmentStateAdapter(fragment) {
    val show : Show = show

    override fun getItemCount(): Int {
            return show.seriesBase.seasons.size
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val season = show.seriesBase.seasons[position]
        val fragment = SeasonDetailsFragment(season!!)
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_PARAM1, position + 1)
        }
        return fragment
    }
}
