package bg.sofia.uni.fmi.mjt.mail.util;

public class StringExtractor {
    public static final String SUBJECT_INCLUDE = "subject-includes: ";
    public static final String SUBJECT_OR_BODY_INCLUDE = "subject-or-body-includes: ";
    public static final String RECEPIENTS_INCLUDE = "recipients-includes: ";
    public static final String FROM_INCLUDE = "from: ";

    public static String extractStartPrefix(String line) {
        if (line.startsWith(SUBJECT_INCLUDE)) {
            return SUBJECT_INCLUDE;
        }

        if (line.startsWith(SUBJECT_OR_BODY_INCLUDE)) {
            return SUBJECT_OR_BODY_INCLUDE;
        }
        if (line.startsWith(FROM_INCLUDE)) {
            return FROM_INCLUDE;
        }

        if (line.startsWith(RECEPIENTS_INCLUDE)) {
            return RECEPIENTS_INCLUDE;
        }

        return null;
    }

    public static String[] extractKeyStrings(String line, String include) {
        String words = line.substring(include.length());
        words = words.trim();
        return words.split(",");
    }

    public static String[] extractRecipients(String metaData) {
        String[] lines = metaData.split(System.lineSeparator());
        for (String line : lines) {
            if (line.startsWith(RECEPIENTS_INCLUDE)) {
                String recipientsString = line.substring(RECEPIENTS_INCLUDE.length()).trim();
                return recipientsString.split(",");
            }
        }
        return null;
    }
}
