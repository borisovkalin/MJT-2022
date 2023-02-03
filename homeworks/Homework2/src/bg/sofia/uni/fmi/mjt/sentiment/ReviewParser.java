package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReviewParser {

    public static final String NOT_A_WORD_PATTERN = "[^A-Za-z0-9']";
    private final List<String> lineList;
    private final Set<String> stopWords;
    private Map<String, Double> wordSentiment;
    private Map<String, Integer> appearanceCount;


    public ReviewParser(Reader in, Set<String> stopWords) {
        this.stopWords = stopWords;
        this.lineList = new ArrayList<>();
        parseSentiments(in);
    }
    private void parseSentiments(Reader in) {
        extractLines(in);
        mapWordSentimentsStream(lineList);
    }

    private void extractLines(Reader in) {
        try (BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                this.lineList.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("An exception occurred while trying to extract lines from review file", e);
        }
    }

    private void mapWordSentimentsStream(List<String> lines) {
        this.appearanceCount = new HashMap<>();
        this.wordSentiment = new HashMap<>();
        Map<String, List<Integer>> appearanceSentiments = new HashMap<>();

        //4 Review, Review, top top top
        lines.stream()
                .map(str -> str.split(NOT_A_WORD_PATTERN))
                .forEach(array -> {
                    int sentiment = Integer.parseInt(array[0]);
                    Set<String> words = new HashSet<>(List.of(array));
                    words.stream()
                            .filter(str -> !stopWords.contains(str.toLowerCase()) && str.length() >= 2)
                            .forEach(str -> {
                                str = str.toLowerCase();
                                appearanceSentiments.putIfAbsent(str, new ArrayList<>());
                                appearanceSentiments.get(str).add(sentiment);
                            });
                });

        appearanceSentiments.forEach((key, value) -> {
            appearanceCount.put(key, appearanceSentiments.get(key).size());
            wordSentiment.put(key,
                    value.stream()
                            .mapToInt(Integer::valueOf)
                            .average()
                            .orElse(-1));
        });
    }

    public int countAppearances(String word) {
        return appearanceCount.get(word.toLowerCase());
    }


    public Map<String, Double> getMap() {
        return wordSentiment;
    }

    public Map<String, Integer> getAppearanceCount() {
        return appearanceCount;
    }

    public int getWordCountSize() {
        return appearanceCount.size();
    }

    public double getSentiment(String word) {
        return wordSentiment.get(word.toLowerCase());
    }

    public void updateReviews(String line) {
        lineList.add(line);
        mapWordSentimentsStream(lineList);
    }
}
