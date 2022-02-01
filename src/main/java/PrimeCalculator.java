import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrimeCalculator {
    public static void main(String[] args) throws InterruptedException {
        for (Integer prime : getPrimes(Integer.parseInt(args[0]))) {
            System.out.print(prime + "\n");
        }
    }

    private static List<Integer> getPrimes(int maxPrime) throws InterruptedException {
        List<Integer> numbers = Stream.generate(new Supplier<Integer>() {
            int i = 2;

            @Override
            public Integer get() {
                return i++;
            }
        }).limit(maxPrime).collect(Collectors.toList());
        List<Integer> primeNumbers = Collections.synchronizedList(new LinkedList<>());
        CountDownLatch latch = new CountDownLatch(maxPrime - 1);
        ExecutorService executors = Executors.newFixedThreadPool(50);
        synchronized (primeNumbers) {
            for (int i = 2; i <= maxPrime; i++) {
                int candidate = i;
                executors.submit(() -> {
                    if (isPrime(numbers, candidate)) {
                        primeNumbers.add(candidate);
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();
        executors.shutdownNow();

        Collections.sort(primeNumbers);
        return primeNumbers;
    }

    private static boolean isPrime(List<Integer> numbers, int candidate) {
        for (Integer j : numbers.subList(0, (int) Math.sqrt(candidate - 2))) {
            if (candidate % j == 0) {
                return false;
            }
        }
        return true;
    }
}
