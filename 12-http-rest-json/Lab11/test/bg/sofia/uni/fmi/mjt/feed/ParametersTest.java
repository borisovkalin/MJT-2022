package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.exception.IllegalParameterInputException;
import bg.sofia.uni.fmi.mjt.feed.util.Parameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParametersTest {

    private final Parameters params = new Parameters();

    @Test
    void testSetParametersWithNegativePageNum() {
        assertThrows(IllegalParameterInputException.class, ()-> params.setPage(-1),
                "Expected an exception when trying to pass negative page number");
    }

    @Test
    void testSetCategoryWithNullObject() {
        assertThrows(IllegalParameterInputException.class, ()-> params.setCategory(null),
                "Expected an exception when trying to pass null");
    }

    @Test
    void testSetCategoryWithSpecialSymbols() {

        assertThrows(IllegalParameterInputException.class, ()-> params.setCategory("$#/"),
                "Expected an exception when trying to pass illegal Symbol");
    }

    @Test
    void testSetCountryWithNullObject() {
        assertThrows(IllegalParameterInputException.class, ()-> params.setCountry(null),
                "Expected an exception when trying to pass null");
    }

    @Test
    void testSetCountryWithSpecialSymbols() {

        assertThrows(IllegalParameterInputException.class, ()-> params.setCountry("$#/"),
                "Expected an exception when trying to pass illegal Symbol");
    }

    @Test
    void testSetKeysWithNullPassed() {
        assertThrows(IllegalParameterInputException.class, ()-> params.setKeys(null),
                "Expected an exception when trying to pass null");
    }

    @Test
    void testSetKeysWithIllegalSymbols() {
        assertThrows(IllegalParameterInputException.class, ()-> params.setKeys("d","#","$"),
                "Expected an exception when trying to illegal Symbol");
    }

}
