package tv.olaris.android.models

open class MediaItem(
    val uuid: String,
    var posterUrl: String,
    val name: String,
    var runtime: Double = 0.0,
    var subTitle: String = "",
    var fileUuid: String = "",
    var playtime: Double = 0.0,
    var finished: Boolean = false
) {
    fun playProgress(): Double{
       return ((100 * playtime) / runtime)
    }

    fun hasStarted(): Boolean{
        if(playtime != 0.0){
            return true
        }

        return false
    }
    fun fullPosterUrl(baseUrl: String?): String {
        return if (baseUrl != null) {
            "${baseUrl}/${this.posterUrl}"
        } else {
            ""
        }
    }
}
