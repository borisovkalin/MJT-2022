package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.util.MatchChecker;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchCheckerTest {
    private static final Mail DEFAULT_NULL_MAIL = new Mail(null, null, null, null, null);

    private static final Account DEFAULT_SENDER = new Account("bobo@abv.bg", "Boryan");
    private static final Set<String> DEFAULT_RECIPIENTS = new HashSet<>(List.of("Salim", "Efrem", "Bobi"));
    private static final String DEFAULT_SUBJECT = "DEFAULT";
    private static final String DEFAULT_BODY = "DEFAULT";
    private static final LocalDateTime DEFAULT_TIME = LocalDateTime.of(1000, 12, 30, 12, 0);

    private static final Mail DEFAULT_MAIL = new Mail(DEFAULT_SENDER, DEFAULT_RECIPIENTS, DEFAULT_SUBJECT, DEFAULT_BODY, DEFAULT_TIME);
    private static final String DEFAULT_RULE_LINE = "recipients-includes: ";

    //Account sender, Set<String> recipients, String subject, String body, LocalDateTime received
    @Test
    void testCheckMailByRecepientsRuleWithNull() {
        assertFalse(MatchChecker.checkMailByRecepientsRule(DEFAULT_NULL_MAIL, DEFAULT_RULE_LINE),
                "Expected it to return false when recepients are null");
    }

    @Test
    void testCheckMailByRecepientsRuleWithEmptyRuleLine() {
        assertThrows(IllegalArgumentException.class, () -> MatchChecker.checkMailByRecepientsRule(DEFAULT_MAIL, DEFAULT_RULE_LINE),
                "Expected it to throw exception when rule line keywords is null");
    }

    @Test
    void testCheckMailByRecepientsRuleWithRuleLineWithEmptyKeyWords() {
        assertFalse(MatchChecker.checkMailByRecepientsRule(DEFAULT_MAIL, DEFAULT_RULE_LINE + ", , Dab"),
                "Expected it to return false when recepients are null");
    }

    @Test
    void testCheckMailByRecepientsRuleWithOneCorrectKeyWord() {
        assertTrue(MatchChecker.checkMailByRecepientsRule(DEFAULT_MAIL, DEFAULT_RULE_LINE + ", , Salim"),
                "Expected it to return true when recepients expectation are met");
    }

    @Test
    void testCheckMailByFromRule() {
        assertFalse(MatchChecker.checkMailByFromRule(DEFAULT_NULL_MAIL, DEFAULT_RULE_LINE),
                "Expected it to return false when from is null");
    }

    @Test
    void testCheckMailByFromRuleWithBlankFromName() {
        assertTrue(MatchChecker.checkMailByFromRule(DEFAULT_MAIL, "from: "),
                "Expected it to return true when from is blank");
    }

    @Test
    void testCheckMailBySubjectOrBodyRuleNull() {
        assertFalse(MatchChecker.checkMailBySubjectOrBodyRule(DEFAULT_NULL_MAIL, DEFAULT_RULE_LINE),
                "Expected it to return false when subjectOrBody is null");
    }

    @Test
    void testCheckMailBySubjectOrBodyRuleWithBlankKeyString() {
        assertThrows(IllegalArgumentException.class,() ->MatchChecker.checkMailBySubjectOrBodyRule(DEFAULT_MAIL, "subject-or-body-includes: "),
                "Expected it to throw exception false when subjectOrBodyRule is blank");
    }

    @Test
    void testCheckMailBySubjectOrBodyRuleWithNoMatchingKeyWords() {
        assertFalse(MatchChecker.checkMailBySubjectOrBodyRule(DEFAULT_MAIL, "subject-or-body-includes: Flop"),
                "Expected it to return false when subject or body expectations are not met");
    }

    @Test
    void testCheckMailBySubjectRuleNull() {
        assertFalse(MatchChecker.checkMailBySubjectRule(DEFAULT_NULL_MAIL, DEFAULT_RULE_LINE),
                "Expected it to return false when subject is null");
    }

    @Test
    void testCheckMailBySubjectRuleLineIsBlank() {
        assertThrows(IllegalArgumentException.class,() ->MatchChecker.checkMailBySubjectRule(DEFAULT_MAIL, "subject-includes: "),
                "Expected it to return false when subject rule line keywords don't exist");
    }

    @Test
    void testCheckMailBySubjectRuleWithNoMatchingKeywords() {
        assertFalse(MatchChecker.checkMailBySubjectRule(DEFAULT_MAIL, "subject-includes: Skribidibobobobyesyesyesyesshtipshtibiiddib"),
                "Expected it to return false when subject expectations not met");
    }
}

