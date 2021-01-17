package tv.olaris.android.ui.movieDetails

import androidx.lifecycle.ViewModel
import tv.olaris.android.models.Movie

class MovieDetailsViewModel(movie: Movie) : ViewModel() {
    lateinit var movie : Movie
}