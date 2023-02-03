package primes;

public class MathUtils {

    public static boolean isPrime(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Not defined for negative numbers");
        }

        if (n==0 || n == 1) {
            return false;
        }
        if (n % 2 == 0 && n !=2) {
            return false;
        }
        double squareRoot = Math.sqrt(n);

        for (int i = 3; i <= squareRoot; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
