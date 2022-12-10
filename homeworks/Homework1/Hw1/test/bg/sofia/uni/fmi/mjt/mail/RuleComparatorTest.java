package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.comparator.RuleComparator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RuleComparatorTest {
    private static final RuleComparator comparator = new RuleComparator();
    private static final Rule DEFAULT_RULE_1 = new Rule("/inbox","r",1);
    private static final Rule DEFAULT_RULE_2 = new Rule("/inbox","r",2);

    @Test
    void testComparatorWithBiggerPriorityFirst() {
        assertFalse(isPositive(comparator.compare(DEFAULT_RULE_1,DEFAULT_RULE_2)),
                "Expected comparator values not valid");
    }

    @Test
    void testComparatorWithSmallerPriorityFirst() {
        assertTrue(isPositive(comparator.compare(DEFAULT_RULE_2,DEFAULT_RULE_1)),
                "Expected comparator values not valid");
    }

    private boolean isPositive(int number) {
        return number > 0;
    }
}
