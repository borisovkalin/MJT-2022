package bg.sofia.uni.fmi.mjt.mail.util;

public final class StringExtractor {

    public static final String RULE_SUBJECT_INCLUDE = "subject-includes: ";
    public static final String RULE_SUBJECT_OR_BODY_INCLUDE = "subject-or-body-includes: ";
    public static final String RULE_RECEPIENTS_INCLUDE = "recipients-includes: ";
    public static final String RULE_FROM_INCLUDE = "from: ";

    public static String extractStartPrefix(String line) {
        if (line.startsWith(RULE_SUBJECT_INCLUDE)) {
            return RULE_SUBJECT_INCLUDE;
        }

        if (line.startsWith(RULE_SUBJECT_OR_BODY_INCLUDE)) {
            return RULE_SUBJECT_OR_BODY_INCLUDE;
        }
        if (line.startsWith(RULE_FROM_INCLUDE)) {
            return RULE_FROM_INCLUDE;
        }

        if (line.startsWith(RULE_RECEPIENTS_INCLUDE)) {
            return RULE_RECEPIENTS_INCLUDE;
        }

        return null;
    }

    public static String[] extractKeyStrings(String line, String include) {
        String words = line.substring(include.length());
        words = words.trim();
        String[] result = words.split(" *, *");
        StringBuilder concat = new StringBuilder();

        for (String str : result) {
            concat.append(str);
        }

        if (concat.toString().isBlank()) {
            return null;
        }

        return result;
    }

    public static String[] extractRecipients(String metaData) {
        String[] lines = metaData.split(System.lineSeparator());
        for (String line : lines) {
            if (line.startsWith(MatchChecker.MAIL_RECIPIENTS)) {
                String recipientsString = line.substring(MatchChecker.MAIL_RECIPIENTS.length()).trim();
                return recipientsString.split(", ");
            }
        }
        return null;
    }
}
