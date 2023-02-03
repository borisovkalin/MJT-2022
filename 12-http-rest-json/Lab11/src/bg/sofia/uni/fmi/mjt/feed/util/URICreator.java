package bg.sofia.uni.fmi.mjt.feed.util;

import java.net.URI;

public class URICreator {
    private static final String SCHEMA = "https://";
    private static final String HOST = "newsapi.org";
    private static final String API_VERSION = "/v2";
    private static final String PATH = "/top-headlines?";

    private static final String PAGE_SIZE = "pageSize=50&";

    private static final String KEY_PARAMETER_START  = "&q=";
    private static final String CATEGORY_PARAMETER_START  = "&category=";
    private static final String COUNTRY_PARAMETER_START  = "&country=";

    private static final String PAGE_START = "page=";

    private static final String URI_DELIMITER = "+";
    public static final String DELIMITER = " ";

    private static final String API_KEY = "Your+API+KEY+HERE";

    private final Parameters previousParams;

    public URICreator() {
        previousParams = new Parameters();
    }

    public URI nextPage() {
        previousParams.incrementPage();
        String pageParameter = PAGE_START + previousParams.getPage();
        String str = SCHEMA + HOST + API_VERSION + PATH + PAGE_SIZE + pageParameter + CATEGORY_PARAMETER_START
                + previousParams.getCategory() + COUNTRY_PARAMETER_START +  previousParams.getCountry()
                + KEY_PARAMETER_START +  previousParams.getKeys().substring(0,
                previousParams.getKeys().toString().length() - 1) + "&apiKey=" + API_KEY;
        return URI.create(str);
    }

    public URI create(String category, String country, String keys, int page) {
        previousParams.setCategory(category);
        previousParams.setCountry(country);
        previousParams.setPage(page);
        String pageParameter = PAGE_START + page;
        String formattedKeys = keys.replaceAll(DELIMITER, URI_DELIMITER);
        previousParams.setKeys(formattedKeys);

        return URI.create(SCHEMA + HOST + API_VERSION + PATH + PAGE_SIZE + pageParameter + CATEGORY_PARAMETER_START
                + category + COUNTRY_PARAMETER_START + country + KEY_PARAMETER_START + formattedKeys + "&apiKey="
                + API_KEY);
    }
}
