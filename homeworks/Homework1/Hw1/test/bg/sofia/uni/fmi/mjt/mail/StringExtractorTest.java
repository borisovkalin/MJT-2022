package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.util.StringExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringExtractorTest {
    private static final String DEFAULT_LINE = "recipients-includes: blob, frob, clob";

    @Test
    void testExtractStartPrefixRecipients() {
        assertEquals(StringExtractor.RULE_RECEPIENTS_INCLUDE, StringExtractor.extractStartPrefix(DEFAULT_LINE),
                "Expected correct Rule prefix extract");
    }

    @Test
    void testExtractStartPrefixFrom() {
        assertEquals(StringExtractor.RULE_FROM_INCLUDE, StringExtractor.extractStartPrefix(StringExtractor.RULE_FROM_INCLUDE),
                "Expected correct Rule prefix extract");
    }

    @Test
    void testExtractStartPrefixSubjectOrBody() {
        assertEquals(StringExtractor.RULE_SUBJECT_OR_BODY_INCLUDE, StringExtractor.extractStartPrefix(StringExtractor.RULE_SUBJECT_OR_BODY_INCLUDE),
                "Expected correct Rule prefix extract");
    }

    @Test
    void testExtractStartPrefixSubject() {
        assertEquals(StringExtractor.RULE_SUBJECT_INCLUDE, StringExtractor.extractStartPrefix(StringExtractor.RULE_SUBJECT_INCLUDE),
                "Expected correct Rule prefix extract");
    }

    @Test
    void testExtractStartPrefixUnknown() {
        assertNull(StringExtractor.extractStartPrefix("OOGA BOOGA :"),"Expected unknown prefix to be null");
    }

    @Test
    void testExtractKeyStringsWithBlankStrings() {
        assertNull(StringExtractor.extractKeyStrings(StringExtractor.RULE_FROM_INCLUDE + ", , ", StringExtractor.RULE_FROM_INCLUDE)
        ,"Expected " + StringExtractor.RULE_FROM_INCLUDE + " , , " + "; to return null");
    }

}
