package bg.sofia.uni.fmi.mjt.netflix;

import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetflixRecommenderTest {
    private static final NetflixRecommender EMPTY_NETFLIX = new NetflixRecommender(Reader.nullReader());
    private static final NetflixRecommender NETFLIX_RECOMMENDER = new NetflixRecommender(
            new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                    "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                    "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0\n"));
    private static final Content DEFAULT_CONTENT = new Content("tm84618", "Taxi Driver", ContentType.MOVIE, "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.", 1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0);

    @Test
    void testGetAllContentModifiability() {
        assertThrows(UnsupportedOperationException.class, () -> NETFLIX_RECOMMENDER.getAllContent().add(DEFAULT_CONTENT),
                "Expected getAllContent to return an unmodifiable collection");

    }

    @Test
    void testGetAllContentModifiabilityWithNoContent() {
        assertThrows(UnsupportedOperationException.class, () -> new NetflixRecommender(Reader.nullReader()).getAllContent().add(DEFAULT_CONTENT),
                "Expected getAllContent to return an empty unmodifiable collections");

    }

    @Test
    void testGetTheLongestMovie() {
        assertEquals(NETFLIX_RECOMMENDER.getAllContent().get(0), NETFLIX_RECOMMENDER.getTheLongestMovie()
                , "Unexpected longest movie ->" + NETFLIX_RECOMMENDER.getTheLongestMovie().runtime()
                        + " : Expected ->" + NETFLIX_RECOMMENDER.getAllContent().get(0).runtime());
    }

    @Test
    void testGetTheLongestMovieWithNoMovies() {
        assertThrows(NoSuchElementException.class, () -> new NetflixRecommender(Reader.nullReader()).getTheLongestMovie(),
                "expected an exception when trying to get longest movie with no movies");
    }


    @Test
    void testGetAllGenresWithDefaultThreeUniqueGenres() {
        int expectedGenres = 5;
        assertEquals(expectedGenres, NETFLIX_RECOMMENDER.getAllGenres().size(), "Expected 5 unique genres");
    }

    @Test
    void testGroupContentByTypeWithMoviesOnly() {
        int expectedMovies = 2;
        int expectedShow = 0;
        assertEquals(expectedMovies, NETFLIX_RECOMMENDER.groupContentByType().get(ContentType.MOVIE).size(),
                "Expected two movies in map");
        assertEquals(expectedShow, NETFLIX_RECOMMENDER.groupContentByType().get(ContentType.SHOW).size(),
                "Expected no shows");
    }

    @Test
    void testGetAllGenresWithNoContent() {
        assertTrue(new NetflixRecommender(Reader.nullReader()).getAllGenres().isEmpty(),
                "Expected Empty List but got something unexpected");
    }

    @Test
    void testGetTopNRatedContentWithNegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> EMPTY_NETFLIX.getTopNRatedContent(-1));
    }

    @Test
    void testGetTopNRatedContentWithZeroInput() {
        assertTrue(EMPTY_NETFLIX.getTopNRatedContent(0).isEmpty(),
                "Expected Empty list with Arg == 0");
    }

    @Test
    void testGetTopNRatedContentWithExceedingCapacityInput() {
        List<Content> sortedList = NETFLIX_RECOMMENDER.getTopNRatedContent(3);
        int expectedAmount = 2;
        assertEquals(expectedAmount, sortedList.size(), "Expected two elements in list");
    }

    @Test
    void testGetTopNRatedContentWithLowerThanCapInput() {
        List<Content> sortedList = NETFLIX_RECOMMENDER.getTopNRatedContent(1);
        int expectedAmount = 1;
        assertEquals(expectedAmount, sortedList.size(), "Expected two elements in list");
    }

    @Test
    void testGetTopNRatedContent() {
        String expectedTitle = "Taxi Driver";
        assertEquals(expectedTitle, NETFLIX_RECOMMENDER.getTopNRatedContent(1).get(0).title(),
                "Unexpected Title at top of rating");
    }

     @Test
     void  testGetSimilarContentWithNullContent() {
         assertThrows(IllegalArgumentException.class,() -> EMPTY_NETFLIX.getSimilarContent(null),
                 "Expected IllegalArgumentException when passing null to method");
     }
     @Test
     void  testGetSimilarContentWithContentEqualToOneInList() {
         int start = 0;
         assertEquals(DEFAULT_CONTENT, NETFLIX_RECOMMENDER.getSimilarContent(DEFAULT_CONTENT).get(start),
                 "Expected Matching content to be first");
     }

    @Test
    void testGetContentByKeywordsWithNoWordsMatching() {
        assertTrue(NETFLIX_RECOMMENDER.getContentByKeywords("filialitiatieten").isEmpty(), "Expected empty set");
    }

    @Test
    void testGetContentByKeywordsWithWordMatching() {
        assertTrue(NETFLIX_RECOMMENDER.getContentByKeywords("DriVer").contains(DEFAULT_CONTENT), "Expected empty set");
    }

    @Test
    void testGetContentByKeywordsWithOneWordMatchingAndOneWordNonMatching() {
        assertTrue(NETFLIX_RECOMMENDER.getContentByKeywords("DriVer, stable").isEmpty(), "Expected empty set");
    }

/*    @Test
    void testSetupDatasetCSVLocal() {
        int start = 0;
        Content search = new Content("1","1",ContentType.MOVIE,"1",1,111,List.of("'drama'", "'comedy'", "'romance'", "'thriller'","'crime'","'family'","'european'","'animation'"),-1,"tt1639084",6.7,20208.0);
        File init = new File("dataset.csv");
        try (Reader targetReader = new FileReader("dataset.csv")) {
            NetflixRecommender test = new NetflixRecommender(targetReader);
            for (Content iter : test.getSimilarContent(search)) {
                System.out.println(iter.title() + " : " + iter.genres());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}

