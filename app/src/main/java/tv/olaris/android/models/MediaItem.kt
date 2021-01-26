package tv.olaris.android.models

import java.util.*

open class  MediaItem(val uuid: String, var posterUrl: String, val name: String, var subTitle: String = "", var fileUuid: String = ""){
    fun fullPosterUrl(baseUrl: String?) : String {
        return if(baseUrl != null) {
            "${baseUrl}/${this.posterUrl}"
        }else{
            ""
        }
    }

}
