package bg.sofia.uni.fmi.mjt.feed.validator;

import bg.sofia.uni.fmi.mjt.feed.exception.IllegalParameterInputException;

public class ParameterValidator {

    private static final String DEFAULT_REGEX = ".*[?#$%^&*!@./,'; ]+.*";
    private static final String KEY_REGEX = ".*[?#$%^&*!@./,';]+.*";

    private static final String NULL_ERROR_MESSAGE = "Method doesn't accept null values";
    private static final String ILLEGAL_CHAR_MESSAGE = " contains illegal URI characters";
    private static final String KEY_NOT_SPECIFIED_MESSAGE = "Keys not specified";
    private static final String PAGE_NUMBER_MESSAGE = "Negative page number passed";

    private ParameterValidator() { }

    public static void checkIfNull(String str) {
        if (str == null) {
            throw new IllegalParameterInputException(NULL_ERROR_MESSAGE);
        }
    }

    public static void checkSpecialChars(String str) {
        if (str.matches(DEFAULT_REGEX)) {
            throw new IllegalParameterInputException(str + ILLEGAL_CHAR_MESSAGE);
        }
    }

    public static void checkKeys(String... keys) {
        if (keys == null) {
            throw new IllegalParameterInputException(NULL_ERROR_MESSAGE);
        }

        for (String str : keys) {
            if (str.matches(KEY_REGEX)) {
                throw new IllegalParameterInputException(str + ILLEGAL_CHAR_MESSAGE);
            }
        }

    }
    public static void checkRequestParams(String keys) {
        if (keys.isBlank()) {
            throw new IllegalParameterInputException(KEY_NOT_SPECIFIED_MESSAGE);
        }
    }

    public static void checkPageNum(int page) {
        if (page < 0) {
            throw new IllegalParameterInputException(PAGE_NUMBER_MESSAGE);
        }
    }

}
