package tv.olaris.android.repositories

import tv.olaris.android.models.Movie
import javax.inject.Inject

class MoviesRepository @Inject constructor(){
    fun getAllMovies() : List<Movie>{
        return listOf(
            Movie("The Matrix", 1999, "Awesome movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"),
            Movie("The Matrix Reloaded", 2003, "Second, not so Awesome,  movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/aA5qHS0FbSXO8PxcxUIHbDrJyuh.jpg"),
            Movie("Weekend at Bernies", 1989, "Awesome movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/ym09EHiQYtwYnLqTv38KMjwwabc.jpg"),
            Movie("Pretty Woman", 1990, "Awesome movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hMVMMy1yDUvdufpTl8J8KKNYaZX.jpg")
        )
    }
}