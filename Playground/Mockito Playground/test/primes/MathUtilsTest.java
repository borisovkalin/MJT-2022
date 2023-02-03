package primes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathUtilsTest {

    @Test
    void testIsPrimeOne() {
        assertFalse(MathUtils.isPrime(1),"One is not a prime from definition of prime");
    }

    @Test
    void testIsPrimeNegative() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.isPrime(-13)
                ,"Prime is not defined for negative numbers and should throw exception");

    }

    @Test
    void testIsPrimeZero() {
        assertFalse(MathUtils.isPrime(0),"Prime is not defined for Zero");
    }
    @Test
    void testIsPrimeFiveIsPrime() {
        assertTrue(MathUtils.isPrime(5),"Five is prime");
    }
    @Test
    void testIsPrimeSixteenIsNotPrime() {
        assertFalse(MathUtils.isPrime(16),"Sixteen is not prime");
    }

    @Test
    void testIsPrimeTenIsNotPrime() {
        assertFalse(MathUtils.isPrime(10),"Ten is not prime");
    }

    @Test
    void testIsPrimeFifteenIsNotPrime() {
        assertFalse(MathUtils.isPrime(15),"Fifteen is not prime");
    }

    @Test
    void testIsPrimeTwoIsPrime() {
        assertTrue(MathUtils.isPrime(2),"Two is prime");
    }
}
