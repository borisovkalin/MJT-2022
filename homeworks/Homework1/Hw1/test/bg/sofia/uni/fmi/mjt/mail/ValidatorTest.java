package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.util.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void testValidatePriorityBoundsWithZeroPriority() {
        assertThrows(IllegalArgumentException.class, () ->Validator.validatePriorityBounds(0),
                "Expected exception with illegal priority added to method");
    }

    @Test
    void testValidatePriorityBoundsWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () ->Validator.validatePriorityBounds(-10),
                "Expected exception with illegal priority added to method");
    }

    @Test
    void testValidatePriorityBoundsWithMoreThanTenPriority() {
        assertThrows(IllegalArgumentException.class, () ->Validator.validatePriorityBounds(130),
                "Expected exception with illegal priority added to method");
    }

    @Test
    void testValidateObjectsWithNull(){
        assertThrows(IllegalArgumentException.class, () ->Validator.validateObject("String", null),
                "Expected exception with illegal priority added to method");
    }
    @Test
    void testValidateStringWithEmptyString(){
        assertThrows(IllegalArgumentException.class, () ->Validator.validateString("String", ""),
                "Expected exception with illegal priority added to method");
    }

    @Test
    void testValidateStringWithBlankString(){
        assertThrows(IllegalArgumentException.class, () ->Validator.validateString("String", "  "),
                "Expected exception with illegal priority added to method");
    }
}
