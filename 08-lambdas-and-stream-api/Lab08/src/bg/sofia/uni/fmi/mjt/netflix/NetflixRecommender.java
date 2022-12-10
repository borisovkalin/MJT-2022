package bg.sofia.uni.fmi.mjt.netflix;

import bg.sofia.uni.fmi.mjt.netflix.comparator.ContentByCommonGenresComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class NetflixRecommender {

    private static final double SENSITIVITY_THRESHOLD = 10_000;

    private final List<Content> netflixShows;

    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {

        try (BufferedReader input = new BufferedReader(reader)) {
            netflixShows = new ArrayList<>(input.lines()
                    .skip(1)
                    .map(Content::of)
                    .toList());
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from CSV input", e);
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return Collections.unmodifiableList(netflixShows);
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    public List<String> getAllGenres() {
        return netflixShows.stream()
                .flatMap(c -> c.genres().stream())
                .distinct()
                .toList();
    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        if (netflixShows.size() == 0) {
            throw new NoSuchElementException("No movies in dataset");
        }
        double longestMovieTime = netflixShows.stream()
                .filter(c -> c.type().equals(ContentType.MOVIE))
                .mapToDouble(Content::runtime)
                .max()
                .orElseThrow(() -> new NoSuchElementException("Couldn't find maximum movieTime"));

        return netflixShows.stream()
                .filter(c -> c.runtime() == longestMovieTime)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Couldn't find Content with such movieTime"));
    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */
    public Map<ContentType, Set<Content>> groupContentByType() {
        Map<ContentType, Set<Content>> groupedMap = new HashMap<>();

        groupedMap.put(ContentType.MOVIE, netflixShows.stream()
                .filter(c -> c.type().equals(ContentType.MOVIE))
                .collect(Collectors.toSet()));
        groupedMap.put(ContentType.SHOW, netflixShows.stream()
                .filter(c -> c.type().equals(ContentType.SHOW))
                .collect(Collectors.toSet()));

        return groupedMap;
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     *
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     *
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument sent to method should be positive or == 0");
        }
        if (n == 0) {
            return new ArrayList<>();
        }

        double averageRating = netflixShows.stream()
                .mapToDouble(Content::imdbScore)
                .average()
                .orElse(0);

        List<Content> sortedContent = new ArrayList<>(netflixShows.stream()
                .sorted(Comparator.comparingDouble(c ->
                        Content.calculateContentWeightedRating(averageRating, c.imdbScore(), c.imdbVotes())))
                .toList());
        Collections.reverse(sortedContent);
        if (n >= sortedContent.size()) {
            return sortedContent;
        }

        return sortedContent.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual oder
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */
    public List<Content> getSimilarContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("content can't be null");
        }
        ContentByCommonGenresComparator.anchor = content;
        List<Content> list = new ArrayList<>(netflixShows.stream()
                .filter(c -> c.type() == content.type())
                .sorted(new ContentByCommonGenresComparator())
                .toList());
        Collections.reverse(list);
        return list;
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    public Set<Content> getContentByKeywords(String... keywords) {
        return netflixShows.stream()
                .filter(c -> c.checkIfAllWordsAreContained(keywords)).collect(Collectors.toUnmodifiableSet());
    }

}