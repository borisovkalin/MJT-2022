package bg.sofia.uni.fmi.mjt.feed.util;

import bg.sofia.uni.fmi.mjt.feed.validator.ParameterValidator;

public class Parameters {

    private static final String DEFAULT = "";
    private String category;
    private String country;
    private StringBuilder keys;
    private int page;

    public Parameters() {
        clearParameters();
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public StringBuilder getKeys() {
        return keys;
    }

    public int getPage() {
        return page;
    }

    public void setCategory(String category) {
        ParameterValidator.checkIfNull(category);
        ParameterValidator.checkSpecialChars(category);
        this.category = category;
    }

    public void setCountry(String country) {
        ParameterValidator.checkIfNull(country);
        ParameterValidator.checkSpecialChars(country);
        this.country = country;
    }

    public void setKeys(String... keys) {
        ParameterValidator.checkKeys(keys);
        StringBuilder newKeys = new StringBuilder();

        for (String str : keys) {
            newKeys.append(str).append(URICreator.DELIMITER);
        }

        this.keys = newKeys;
    }

    public void setPage(int page) {
        ParameterValidator.checkPageNum(page);
        this.page = page;
    }

    public void incrementPage() {
        this.page++;
    }
    public void clearParameters() {
        this.category = DEFAULT;
        this.country = DEFAULT;
        this.keys = new StringBuilder(DEFAULT);
        this.page = 0;
    }

}
