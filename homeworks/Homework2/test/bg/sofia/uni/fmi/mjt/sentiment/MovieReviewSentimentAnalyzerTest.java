package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.sentiment.SentimentCaption.SOMEWHAT_POSITIVE_SENTIMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieReviewSentimentAnalyzerTest {

    private static MovieReviewSentimentAnalyzer DEFAULT_FILE_TEST_ANALYZER;
    private MovieReviewSentimentAnalyzer STRING_TEST_ANALYZER;

    private static final double UNKNOWN = -1;

    private static final String FIKI = "Skrrrt Shtibidi dop dop dop yes yes yes yes " +
            "shtip shtibididip shtibidi w w w w yes yes yes yes";

    private static final String DEFAULT_REVIEW = "Fadjit's father had the greatest film idea," +
            " so the movie was an efficient breakdown";

    private static final StringWriter DEFAULT_NO_WRITER = new StringWriter();

    private static final String CUSTOM_STOP_WORDS = "A" + System.lineSeparator()
            + "an" + System.lineSeparator()
            + "of" + System.lineSeparator();

    private static final String CUSTOM_STRING_REVIEWS =
            "4 A major, major breakdown of an efficient movie" + System.lineSeparator()
            + "2 A major flop breakdown" + System.lineSeparator()
            + "0 Bad film" + System.lineSeparator();



    @BeforeAll
    public static void init() {
        try (Reader stopWords = new FileReader("stopwords.txt");
             Reader reviewReader = new FileReader("movieReviews.txt");
             Writer reviewWriter = new FileWriter("movieReviews.txt",true )) {
            DEFAULT_FILE_TEST_ANALYZER = new MovieReviewSentimentAnalyzer(stopWords, reviewReader, reviewWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        StringReader stopWords = new StringReader(CUSTOM_STOP_WORDS);
        StringReader reviews = new StringReader(CUSTOM_STRING_REVIEWS);
        STRING_TEST_ANALYZER = new MovieReviewSentimentAnalyzer(stopWords, reviews, DEFAULT_NO_WRITER);
    }

    @Test
    void testDefaultStopWords() {
        assertTrue(DEFAULT_FILE_TEST_ANALYZER.isStopWord("And")," 'And' should be a stop word");
        assertTrue(DEFAULT_FILE_TEST_ANALYZER.isStopWord("ANd")," 'And' should be a stop word");
        assertTrue(DEFAULT_FILE_TEST_ANALYZER.isStopWord("and")," 'And' should be a stop word");
    }

    @Test
    void testisStopWords() {
        assertTrue(STRING_TEST_ANALYZER.isStopWord("A"),"'A' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("a"),"'a' should be a stop word");

        assertTrue(STRING_TEST_ANALYZER.isStopWord("An"),"'An' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("AN"),"'AN' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("aN"),"'aN' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("an"),"'an' should be a stop word");

        assertTrue(STRING_TEST_ANALYZER.isStopWord("of"),"'oNLYfANS' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("Of"),"'ONLYfANS' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("oF"),"'oNLYFANS' should be a stop word");
        assertTrue(STRING_TEST_ANALYZER.isStopWord("OF"),"'ONLYFANS' should be a stop word");
    }

    @Test
    void testReviewGetSentimentDictionarySize() {
        int expected = 7;
        assertEquals(expected, STRING_TEST_ANALYZER.getSentimentDictionarySize()
                , "Expected wordCount of known sentiments 7 but got"
                + STRING_TEST_ANALYZER.getSentimentDictionarySize());
    }

    @Test
    void testKnownWordGetWordSentiments() {
        double expectedTwoAsSentiment = 2;
        double expectedThreeAsSentiment = 3;
        double expectedFourAsSentiment = 4;

        assertEquals(expectedThreeAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("mAjoR")
                , "Expected 'mAjoR' to have 3 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("mAjoR"));
        assertEquals(expectedThreeAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("MAJOR")
                , "Expected 'MAJOR' to have 3 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("MAJOR"));


        assertEquals(expectedTwoAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("flop")
                , "Expected 'flop' to have 2 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("flop"));
        assertEquals(expectedTwoAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("fLoP")
                , "Expected 'fLoP' to have 2 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("fLoP"));


        assertEquals(expectedFourAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("Movie")
                , "Expected 'breakDown' to have 4 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("Movie"));
        assertEquals(expectedFourAsSentiment, STRING_TEST_ANALYZER.getWordSentiment("mOvIe")
                , "Expected 'bReAkDowN' to have 4 as sentiment but got"
                + STRING_TEST_ANALYZER.getWordSentiment("mOvIe"));

    }

    @Test
    void testAnalyzerUnknownWordGetWordSentiments() {
        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("LMAO")
                , "Expected unknown LMAO to have -1 sentiment");

        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("Stoyo Boyo")
                , "Expected unknown 'Stoyo Boyo' to have  -1 sentiment");

        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("aeaea")
                , "Expected 'aeaea' to have -1 sentiment");

    }

    @Test
    void testEmptyAnalyzerUnknownWordGetWordSentiments() {
        customSetup("","", DEFAULT_NO_WRITER);
        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("Something Inside")
                , "Expected unknown 'Something Inside' to have -1 sentiment");

        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("f.().''\"ssa")
                , "Expected unknown 'f.().''\"ssa' to have  -1 sentiment");

        assertEquals(UNKNOWN, STRING_TEST_ANALYZER.getWordSentiment("   ")
                , "Expected empty string to have -1 sentiment");
    }

    @Test
    void testGetReviewSentimentUnknownReviewWords() {
        double expectedReviewSentiment = -1;
        assertEquals(expectedReviewSentiment, STRING_TEST_ANALYZER.getReviewSentiment(FIKI)
                ,"Unexpected review sentiment calculation");
    }

    @Test
    void testGetReviewSentimentAsNameWithUnknownReviewWords() {
        assertEquals(SentimentCaption.UNKNOWN_SENTIMENT, STRING_TEST_ANALYZER.getReviewSentimentAsName(FIKI)
                ,"Unexpected review sentiment calculation");
    }

    @Test
    void testGetReviewSentiment() {
        double expectedReviewSentiment = 2.75;
        assertEquals(expectedReviewSentiment,STRING_TEST_ANALYZER.getReviewSentiment(DEFAULT_REVIEW)
                ,"Unexpected review sentiment calculation");
    }

    @Test
    void testGetReviewSentimentAsName() {
        assertEquals(SOMEWHAT_POSITIVE_SENTIMENT, STRING_TEST_ANALYZER.getReviewSentimentAsName(DEFAULT_REVIEW),
                "Unexpected review sentiment calculation or sentiment caption gathering");
    }

    @Test
    void testGetWordFrequencyWithUnknownWord() {
        int expectedFrequency = 0;
        assertEquals(expectedFrequency, STRING_TEST_ANALYZER.getWordFrequency("Messi10"),
                "Expected unknown word passed to show 0 frequency");
    }

    @Test
    void testGetWordFrequency() {
        int expectedFrequency = 2;
        assertEquals(expectedFrequency, STRING_TEST_ANALYZER.getWordFrequency("mAjor"),
                "Expected frequency not shown");

        assertEquals(expectedFrequency, STRING_TEST_ANALYZER.getWordFrequency("mAjoR"),
                "Expected frequency not shown");


        assertEquals(expectedFrequency, STRING_TEST_ANALYZER.getWordFrequency("BreakDown"),
                "Expected frequency not shown");

        assertEquals(expectedFrequency - 1 , STRING_TEST_ANALYZER.getWordFrequency("efficient"),
                "Expected frequency not shown");
    }

    @Test
    void testGetMostFrequentWordsInvalidInput() {
        assertThrows(IllegalArgumentException.class,() -> STRING_TEST_ANALYZER.getMostFrequentWords(-1)
                , "Expected exception when IllegalArgumentPassedToMethod");
    }

    @Test
    void testGetMostFrequentWordsWithEmptyReviews() {
        customSetup(CUSTOM_STOP_WORDS,"",DEFAULT_NO_WRITER);
        assertTrue(STRING_TEST_ANALYZER.getMostFrequentWords(13).isEmpty(), "Expected empty list");
    }

    @Test
    void testGetMostFrequentWords() {
        List<String> list = STRING_TEST_ANALYZER.getMostFrequentWords(3);
        int oldFreq = Integer.MAX_VALUE;
        for (String str : list) {
            assertTrue(STRING_TEST_ANALYZER.getWordFrequency(str) <= oldFreq
                    , "Frequent words Not sorted properly");

            oldFreq = STRING_TEST_ANALYZER.getWordFrequency(str);
        }
    }

    @Test
    void testGetMostPositiveWordsInvalidInput() {
        assertThrows(IllegalArgumentException.class,() -> STRING_TEST_ANALYZER.getMostPositiveWords(-1)
                , "Expected exception when IllegalArgumentPassedToMethod");
    }

    @Test
    void testGetMostPositiveWordsWithEmptyReviews() {
        customSetup(CUSTOM_STOP_WORDS,"",DEFAULT_NO_WRITER);
        assertTrue(STRING_TEST_ANALYZER.getMostPositiveWords(13).isEmpty(), "Expected empty list");
    }

    @Test
    void testGetMostPositiveWords() {
        List<String> list = STRING_TEST_ANALYZER.getMostPositiveWords(3);
        double oldSentiment = Double.MAX_VALUE;
        for (String str : list) {
            assertTrue(STRING_TEST_ANALYZER.getWordSentiment(str) <= oldSentiment
                    , "Positive words Not sorted properly");

            oldSentiment = STRING_TEST_ANALYZER.getWordSentiment(str);
        }
    }

    @Test
    void testGetMostNegativeWordsInvalidInput() {
        assertThrows(IllegalArgumentException.class,() -> STRING_TEST_ANALYZER.getMostNegativeWords(-1)
                , "Expected exception when IllegalArgumentPassedToMethod");
    }

    @Test
    void testGetMostNegativeWordsWithEmptyReviews() {
        customSetup(CUSTOM_STOP_WORDS,"",DEFAULT_NO_WRITER);
        assertTrue(STRING_TEST_ANALYZER.getMostNegativeWords(13).isEmpty(), "Expected empty list");
    }

    @Test
    void testGetMostNegativeWords() {
        List<String> list = STRING_TEST_ANALYZER.getMostNegativeWords(3);
        double oldSentiment = 0;
        for (String str : list) {
            assertTrue(STRING_TEST_ANALYZER.getWordSentiment(str) >= oldSentiment
                    , "Negative words Not sorted properly");
            oldSentiment = STRING_TEST_ANALYZER.getWordSentiment(str);
        }
    }

    @Test
    void testAppendReviewWithNullReview() {
        assertThrows(IllegalArgumentException.class, () -> STRING_TEST_ANALYZER.appendReview(null,3),
                "Expected exception when passing null review to append method");
    }

    @Test
    void testAppendReviewWithBlankOrEmptyReview() {
        assertThrows(IllegalArgumentException.class, () -> STRING_TEST_ANALYZER.appendReview("",3),
                "Expected exception when passing empty review to append method");

        assertThrows(IllegalArgumentException.class, () -> STRING_TEST_ANALYZER.appendReview("      ",3),
                "Expected exception when passing blank review to append method");
    }

    @Test
    void testAppendReviewWithNegativeSentiment() {
        int illegalSentiment = -1;
        assertThrows(IllegalArgumentException.class, () -> STRING_TEST_ANALYZER.appendReview("Expensive chocolate"
                        , illegalSentiment),
                "Expected exception when passing negative argument for sentiment to append method");
    }

    @Test
    void testAppendReviewWithIllegalSentiment() {
        int illegalSentiment = -1;
        assertThrows(IllegalArgumentException.class, () -> STRING_TEST_ANALYZER.appendReview("Expensive chocolate"
                        , illegalSentiment),
                "Expected exception when passing argument > 4.0 for sentiment to append method");
    }


    @Test
    void testAppendReviewTwice() {
        customSetup(CUSTOM_STOP_WORDS,"4 aN A", DEFAULT_NO_WRITER);
        String finalReview = "4 New Review" + System.lineSeparator() + "2 Newer Review" + System.lineSeparator();

        assertTrue(STRING_TEST_ANALYZER.appendReview("New Review", 4));
        assertTrue(STRING_TEST_ANALYZER.appendReview("Newer Review", 2));

        assertEquals(finalReview, DEFAULT_NO_WRITER.toString(), "Expected append to append review twice");
        int wordAppearance = 2;

        assertEquals(wordAppearance, STRING_TEST_ANALYZER.getWordFrequency("Review")
                , "Expected correct word appearance after 2 appends");
        assertEquals(wordAppearance - 1, STRING_TEST_ANALYZER.getWordFrequency("New"),
                "Expected correct word appearance after 2 appends");

        int sentiment = 3;
        assertEquals(sentiment, STRING_TEST_ANALYZER.getWordSentiment("Review")
                , "Expected correct review eval after 2 appends");

        assertEquals(sentiment - 1, STRING_TEST_ANALYZER.getWordSentiment("Newer")
                , "Expected correct review eval after 2 appends");

    }


    private void customSetup(String stopWords, String reviews, StringWriter writer) {
        StringReader stopWordsReader = new StringReader(stopWords);
        StringReader reviewsReader = new StringReader(reviews);
        STRING_TEST_ANALYZER = new MovieReviewSentimentAnalyzer(stopWordsReader, reviewsReader, writer);
    }
}
