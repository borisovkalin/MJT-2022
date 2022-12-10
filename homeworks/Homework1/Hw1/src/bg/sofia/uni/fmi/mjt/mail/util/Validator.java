package bg.sofia.uni.fmi.mjt.mail.util;

public class Validator {
    private static final int MAX_PRIORITY = 10;
    private static final int MIN_PRIORITY = 1;

    public static void validatePriorityBounds(int priority) {

        if (priority > MAX_PRIORITY || priority < MIN_PRIORITY) {
            throw new IllegalArgumentException("Priority value incorrect");
        }
    }

    public static void validateObject(Object... obj) {
        for (Object o : obj) {
            if (o == null) {
                throw new IllegalArgumentException("Can't pass null argument to method");
            }
        }
    }

    public static void validateString(String... msg) {
        for (String str : msg) {
            if (str.isBlank()) {
                throw new IllegalArgumentException("Can't pass Blank/Empty String to method");
            }
        }

    }
}
