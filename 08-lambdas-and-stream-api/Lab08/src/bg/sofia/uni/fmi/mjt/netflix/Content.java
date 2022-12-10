package bg.sofia.uni.fmi.mjt.netflix;

import java.util.ArrayList;
import java.util.List;

public record Content(String id, String title, ContentType type, String description, int releaseYear, int runtime,
                      List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes) {
    private static final String NETFLIX_ATTRIBUTE_DELIMITER = ",";
    private static final int SHOW_ID_IDX = 0;
    private static final int SHOW_TITLE_IDX = 1;
    private static final int SHOW_TYPE_IDX = 2;
    private static final int SHOW_DESCRIPTION_IDX = 3;
    private static final int SHOW_RELEASE_YEAR_IDX = 4;
    private static final int SHOW_RUNTIME_IDX = 5;
    private static final int SHOW_GENRES_IDX = 6;
    private static final int SHOW_SEASONS_IDX = 7;
    private static final int SHOW_IMDB_ID_IDX = 8;
    private static final int SHOW_IMDB_SCORE_IDX = 9;
    private static final int SHOW_IMDB_VOTES_IDX = 10;
    private static final int GENRE_FIRST_INDEX = 1;
    private static final double SENSITIVITY_THRESHOLD = 10_000;

    public static double calculateContentWeightedRating(double average, double imdbScore, double imdbVotes) {
        double tempScore = imdbScore;
        if (imdbVotes == 0) {
            tempScore = 0.d;
        }

        return (imdbVotes / (imdbVotes + SENSITIVITY_THRESHOLD)) * tempScore
                + (SENSITIVITY_THRESHOLD / (imdbVotes + SENSITIVITY_THRESHOLD) * average);
    }
    public int calculateAmountOfCommonGenres(Content content) {


        List<String> list = new ArrayList<>(genres);
        list.retainAll(content.genres);

        return list.size();
    }

    public boolean checkIfAllWordsAreContained(String... words) {
        String descLowerCaseCopy = description.toLowerCase();
        for (String word : words) {
            word = word.toLowerCase();
            if (!descLowerCaseCopy.matches(".*[\\p{IsPunctuation}\\s]+" +
                    word + "[\\p{IsPunctuation}\\s]+.*")) {
                return false;
            }
        }
        return true;
    }
    public static Content of(String line) {
        final String[] tokens = line.split(NETFLIX_ATTRIBUTE_DELIMITER);

        String contentId = tokens[SHOW_ID_IDX];
        String contentTitle = tokens[SHOW_TITLE_IDX];
        String contentDescription = tokens[SHOW_DESCRIPTION_IDX];
        String contentIMDBId = tokens[SHOW_IMDB_ID_IDX];

        ContentType contentType = ContentType.valueOf(tokens[SHOW_TYPE_IDX]);

        int contentReleaseYear =  Integer.parseInt(tokens[SHOW_RELEASE_YEAR_IDX]);
        int contentRunTime = Integer.parseInt(tokens[SHOW_RUNTIME_IDX]);
        int contentSeasons =  Integer.parseInt(tokens[SHOW_SEASONS_IDX]);

        double contentIMDBScore = Double.parseDouble(tokens[SHOW_IMDB_SCORE_IDX]);
        double contentIMDBVotes = Double.parseDouble(tokens[SHOW_IMDB_VOTES_IDX]);

        List<String> contentGenres = new ArrayList<>(List.of(tokens[SHOW_GENRES_IDX].substring(GENRE_FIRST_INDEX,
                tokens[SHOW_GENRES_IDX].length() - 1).trim().replaceAll("'", "").split("; ")));

        return new Content(contentId, contentTitle, contentType,  contentDescription
                , contentReleaseYear, contentRunTime, contentGenres
                , contentSeasons, contentIMDBId
                , contentIMDBScore, contentIMDBVotes);
    }
}
