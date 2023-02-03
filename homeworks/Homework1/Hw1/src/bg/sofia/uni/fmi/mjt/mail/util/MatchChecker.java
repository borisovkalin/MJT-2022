package bg.sofia.uni.fmi.mjt.mail.util;

import bg.sofia.uni.fmi.mjt.mail.Mail;

public final class MatchChecker {

    public static final String MAIL_SENDER = "sender: ";
    public static final String MAIL_SUBJECT = "subject: ";
    public static final String MAIL_RECIPIENTS = "recipients: ";
    public static final String MAIL_TIME = "received: ";

    public static boolean checkMailByRecepientsRule(Mail mail, String line) {
        if (mail.recipients() == null || mail.recipients().isEmpty()) {
            return false;
        }

        String[] strings = StringExtractor.extractKeyStrings(line, StringExtractor.RULE_RECEPIENTS_INCLUDE);
        if (strings == null) {
            throw new IllegalArgumentException("No keywords found in recipients");
        }

        for (String key : strings) {
            if (!key.isBlank()) {
                if (!mail.recipients().contains(key)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkMailByFromRule(Mail mail, String line) {
        if (mail.sender() == null) {
            return false;
        }

        String substring = line.substring(StringExtractor.RULE_FROM_INCLUDE.length());
        substring = substring.trim();

        if (substring.isBlank()) {
            return true;
        }

        return mail.sender().name().equals(substring) || mail.sender().emailAddress().equals(substring);
    }

    public static boolean checkMailBySubjectOrBodyRule(Mail mail, String line) {
        if (mail.subject() == null || mail.body() == null) {
            return false;
        }

        return checkIfAllKeysAreContained(mail.subject(), line, StringExtractor.RULE_SUBJECT_OR_BODY_INCLUDE)
                || checkIfAllKeysAreContained(mail.body(), line, StringExtractor.RULE_SUBJECT_OR_BODY_INCLUDE);
    }

    public static boolean checkMailBySubjectRule(Mail mail, String line) {
        if (mail.subject() == null) {
            return false;
        }

        return checkIfAllKeysAreContained(mail.subject(), line, StringExtractor.RULE_SUBJECT_INCLUDE);
    }

    private static boolean checkIfAllKeysAreContained(String from, String line, String prefix) {
        String[] strings = StringExtractor.extractKeyStrings(line, prefix);
        if (strings == null) {
            throw new IllegalArgumentException("No keywords found in subject");
        }

        for (String key : strings) {
            if (!key.isBlank()) {
                if (!from.contains(key)) {
                    return false;
                }
            }
        }
        return true;
    }

}
