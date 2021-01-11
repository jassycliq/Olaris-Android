package tv.olaris.android.repositories

import tv.olaris.android.models.Movie
import javax.inject.Inject

class MoviesRepository @Inject constructor(){
    fun findMovieByUUID(uuid: String): Movie? {
        return getAllMovies().find {
            it.uuid == uuid
        }
    }

    fun getAllMovies() : List<Movie>{
        return listOf(
            Movie("The Matrix", "1234", 1999, "Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"),
            Movie("The Matrix Reloaded", "1235", 2003, "Second, not so Awesome,  movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/aA5qHS0FbSXO8PxcxUIHbDrJyuh.jpg"),
            Movie("Weekend at Bernies", "1236", 1989, "Two friends are invited for a weekend to a luxury island with their boss. The boss gets shot and nobody seems to notice, except for the two friends. In order not to become suspects of murder they treat the body as a puppet and make people believe he's still alive. The killer wants to do his job so when he is informed that the stiff is still alive he's got to shoot him again, and again, and again.", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/ym09EHiQYtwYnLqTv38KMjwwabc.jpg"),
            Movie("Pretty Woman", "1237", 1990, "When a millionaire wheeler-dealer enters a business contract with a Hollywood hooker Vivian Ward, he loses his heart in the bargain. ", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hMVMMy1yDUvdufpTl8J8KKNYaZX.jpg")
        )
    }
}