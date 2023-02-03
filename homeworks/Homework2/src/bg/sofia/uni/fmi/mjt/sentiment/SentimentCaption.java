package bg.sofia.uni.fmi.mjt.sentiment;

import java.util.HashMap;
import java.util.Map;


public final class SentimentCaption {

    public static final String UNKNOWN_SENTIMENT = "unknown";
    public static final String NEGATIVE_SENTIMENT = "negative";
    public static final String SOMEWHAT_NEGATIVE_SENTIMENT = "somewhat negative";
    public static final String NEUTRAL_SENTIMENT = "neutral";
    public static final String SOMEWHAT_POSITIVE_SENTIMENT = "somewhat positive";
    public static final String POSITIVE_SENTIMENT = "positive";
    private static final int MAX_SENTIMENT_VALUE = 4;
    private static final int MIN_SENTIMENT_VALUE = 0;
    private static final Map<Integer, String> SENTIMENTS;

    private SentimentCaption() { }

    static {
        SENTIMENTS = new HashMap<>();
        int sentimentValue = -1;
        addSentiment(sentimentValue, UNKNOWN_SENTIMENT);
        addSentiment(++sentimentValue, NEGATIVE_SENTIMENT);
        addSentiment(++sentimentValue, SOMEWHAT_NEGATIVE_SENTIMENT);
        addSentiment(++sentimentValue, NEUTRAL_SENTIMENT);
        addSentiment(++sentimentValue, SOMEWHAT_POSITIVE_SENTIMENT);
        addSentiment(++sentimentValue, POSITIVE_SENTIMENT);
    }

    public static void addSentiment(double value, String caption) {
        SENTIMENTS.putIfAbsent((int)value, caption);
    }

    public static String getCaption(double value) {
        Integer key = Math.toIntExact(Math.round(value));
        return SENTIMENTS.get(key);
    }

    public static void isValid(int sentiment) {
        if (sentiment < MIN_SENTIMENT_VALUE || sentiment > MAX_SENTIMENT_VALUE) {
            throw new IllegalArgumentException("Illegal sentiment passed to method");
        }
    }
}
