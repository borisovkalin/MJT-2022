package bg.sofia.uni.fmi.mjt.mail.util;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MailMatchesRuleChecker {
    public static final String MAIL_SENDER = "sender: ";
    public static final String MAIL_SUBJECT = "subject: ";
    public static final String MAIL_RECIPIENTS = "recipients: ";
    public static final String MAIL_TIME = "received: ";

    public static boolean checkMailByRecepientsRule(Mail mail, String line) {
        if (mail.recipients() == null || mail.recipients().isEmpty()) {
            return true;
        }

        String[] strings = StringExtractor.extractKeyStrings(line, StringExtractor.RECEPIENTS_INCLUDE);
        for (String key : strings) {
            if (key.isBlank()) {
                return true;
            }

            if (!mail.recipients().contains(key)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkMailByFromRule(Mail mail, String line) {
        if (mail.sender() == null) {
            return true;
        }
        String substring = line.substring(StringExtractor.FROM_INCLUDE.length());
        substring = substring.trim();
        if (substring.isBlank()) {
            return true;
        }
        return mail.sender().name().equals(substring) || mail.sender().emailAddress().equals(substring);
    }

    public static boolean checkMailBySubjectOrBodyRule(Mail mail, String line) {
        if (mail.subject() == null || mail.body() == null) {
            return true;
        }

        String[] strings = StringExtractor.extractKeyStrings(line, StringExtractor.SUBJECT_OR_BODY_INCLUDE);
        for (String key : strings) {
            if (key.isBlank()) {
                return true;
            }
            if (!mail.subject().contains(key) && !mail.body().contains(key)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkMailBySubjectRule(Mail mail, String line) {
        if (mail.subject() == null) {
            return true;
        }
        String[] strings = StringExtractor.extractKeyStrings(line, StringExtractor.SUBJECT_INCLUDE);
        for (String key : strings) {
            if (key.isBlank()) {
                return true;
            }
            if (!mail.subject().contains(key)) {
                return false;
            }
        }
        return true;
    }

}
