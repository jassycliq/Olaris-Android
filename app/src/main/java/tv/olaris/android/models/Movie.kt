package tv.olaris.android.models

data class Movie(val title: String, val uuid: String, val year: Int, val overview: String, val posterUrl: String){
    fun fullPosterPath() : String {
        return "http://192.168.178.64:4321/${this.posterUrl}"
    }
}

