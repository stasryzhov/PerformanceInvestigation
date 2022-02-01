import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeCalculator {
    public static void main(String[] args) throws InterruptedException {
        for (Integer prime : getPrimes(Integer.parseInt(args[0]))) {
            System.out.print(prime + "\n");
        }
    }

    private static List<Integer> getPrimes(int maxPrime) throws InterruptedException {
        boolean[] isPrimeMarks = new boolean[maxPrime];
        CountDownLatch latch = new CountDownLatch(maxPrime - 1);
        ExecutorService executors = Executors.newFixedThreadPool(50);
        for (int i = 2; i <= maxPrime; i++) {
            int candidate = i;
            executors.submit(() -> {
                if (isPrime(candidate)) {
                    isPrimeMarks[candidate] = true;
                }
                latch.countDown();
            });
        }
        latch.await();
        executors.shutdownNow();
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

    private static boolean isPrime(int candidate) {
        for (int i = 2; i * i <= candidate; i++) {
            if (candidate % i == 0) {
                return false;
            }
        }
        return true;
    }
}
