import java.util.ArrayList;
import java.util.List;

public class PrimeCalculator {
    public static void main(String[] args) {
        for (Integer prime : getPrimes(Integer.parseInt(args[0]))) {
            System.out.print(prime + "\n");
        }
    }

    private static List<Integer> getPrimes(int maxPrime) {
        if (maxPrime <= 1) {
            throw new IllegalArgumentException("Max prime number must be > 1");
        }
        boolean[] isPrimeMarks = new boolean[maxPrime + 1];
        fillWithInitialValues(isPrimeMarks);
        applySieveOfEratosthenes(isPrimeMarks);
        return collectPrimes(isPrimeMarks);
    }

    private static List<Integer> collectPrimes(boolean[] isPrimeMarks) {
        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = 0; i < isPrimeMarks.length; i++) {
            if (isPrimeMarks[i]) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    private static void applySieveOfEratosthenes(boolean[] isPrimeMarks) {
        for (int factor = 2; factor * factor < isPrimeMarks.length; factor++) {
            if (isPrimeMarks[factor]) {
                for (int j = factor; factor * j < isPrimeMarks.length; j++) {
                    isPrimeMarks[factor * j] = false;
                }
            }
        }
    }

    private static void fillWithInitialValues(boolean[] isPrimeMarks) {
        for (int i = 2; i < isPrimeMarks.length; i++) {
            isPrimeMarks[i] = true;
        }
    }
}
