import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeCalculator {
    public static void main(String[] args) throws InterruptedException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Instant startTime = Instant.now();
        List<Integer> primes = getPrimes(Integer.parseInt(args[0]));
        Instant endTime = Instant.now();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            for (Integer prime : primes) {
                stringBuilder.append(prime).append(System.lineSeparator());
            }
            writer.write(stringBuilder.toString());
            writer.write("Primes calculation time: " + Duration.between(startTime, endTime));
        }
    }

    private static List<Integer> getPrimes(int maxPrime) throws InterruptedException {
        boolean[] isPrimeMarks = new boolean[maxPrime];
        ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Boolean>> isPrimeTasks = new ArrayList<>();
        for (int i = 2; i <= maxPrime; i++) {
            int candidate = i;
            isPrimeTasks.add(() -> {
                final boolean isPrime = isPrime(candidate);
                if (isPrime) {
                    isPrimeMarks[candidate] = true;
                }
                return isPrime;
            });
        }
        executors.invokeAll(isPrimeTasks);
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
        if (candidate < 2) {
            return false;
        }
        if (candidate % 2 == 0) {
            return candidate == 2;
        }
        if (candidate % 3 == 0) {
            return candidate == 3;
        }
        for (int i = 5; i * i <= candidate; i += 6) {
            if (candidate % i == 0 || candidate % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
