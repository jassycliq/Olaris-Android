package tv.olaris.android.models

import android.os.Parcel
import android.os.Parcelable

data class Season(val name: String, val overview: String, val seasonNumber: Int, val airDate: String, val posterPath: String, val uuid: String, val unwatchedEpisodesCount: Int) {
    val episodes: MutableList<Episode> = mutableListOf()
}

