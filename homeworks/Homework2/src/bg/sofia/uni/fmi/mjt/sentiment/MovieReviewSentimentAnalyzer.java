package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private final Set<String> stopWords;
    private final Writer reviewOut;
    private final ReviewParser reviews;

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {
        this.reviewOut = reviewsOut;
        this.stopWords = new HashSet<>();
        initializeStopWords(stopwordsIn);
        this.reviews = new ReviewParser(reviewsIn, stopWords);
    }

    @Override
    public double getReviewSentiment(String review) {
        return calculateSentiment(review);
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        return SentimentCaption.getCaption(calculateSentiment(review));
    }

    @Override
    public double getWordSentiment(String word) {
        if (!isContainedInTheParser(word)) {
            return -1;
        }
        return reviews.getSentiment(word);
    }

    @Override
    public int getWordFrequency(String word) {
        if (!isContainedInTheParser(word)) {
            return 0;
        }
        return reviews.countAppearances(word);
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        validateInput(n);
        return reviews.getAppearanceCount()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(n)
                .toList();
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        validateInput(n);
        return reviews.getMap()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(n)
                .toList();

    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        validateInput(n);
        return reviews.getMap()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .limit(n)
                .toList();
    }

    @Override
    public boolean appendReview(String review, int sentiment) {
        SentimentCaption.isValid(sentiment);
        validateString(review);

        StringBuilder reviewBuilder = new StringBuilder();
        reviewBuilder.append(sentiment).append(" ").append(review).append(System.lineSeparator());

        try (BufferedWriter writer = new BufferedWriter(reviewOut)) {
            writer.append(reviewBuilder);
        } catch (IOException e) {
            return false;
        }

        reviews.updateReviews(reviewBuilder.toString());
        return true;
    }

    @Override
    public int getSentimentDictionarySize() {
        return reviews.getWordCountSize();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopWords.contains(word.toLowerCase());
    }

    private void initializeStopWords(Reader rd) {
        try (BufferedReader stopWordsStream = new BufferedReader(rd)) {
            String word;
            while ((word = stopWordsStream.readLine()) != null) {
                stopWords.add(word.toLowerCase());
            }

        } catch (IOException e) {
            throw new RuntimeException("An exception occurred when trying to initialize the stopwords", e);
        }
    }

    private boolean isContainedInTheParser(String word) {
        return reviews.getAppearanceCount().containsKey(word.toLowerCase());
    }

    private double calculateSentiment(String review) {
        double sentiment = 0.0d;
        String[] words = review.split(ReviewParser.NOT_A_WORD_PATTERN);

        int counter = 0;
        for (String str : words) {
            if (isContainedInTheParser(str)) {
                counter++;
                sentiment += reviews.getSentiment(str);
            }
        }

        if (counter == 0) {
            return -1;
        }

        return sentiment / (double) counter;
    }

    private void validateInput(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Expected Input > 0");
        }
    }

    private void validateString(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("Illegal String passed to method");
        }
    }
}
