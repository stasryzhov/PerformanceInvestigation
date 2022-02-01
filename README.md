# Prime numbers calculation: Profiling and Optimization

The optimization is done sequentially as specified in the solutions below. Check commits sequentially to see the atomic
optimizations.

## Optimization stages

### First stage

CPU:

- isPrime method uses ineffective prime number search algorithm
- remove by valye action on the List is an O(n) action and slows down the last part of the algorithm
- too much context switching/thread management, hence additional load to CPU

Memory

- heap usage is high due to lots of unnecessary objects creation

Solutions:

1. Remove unnecessary objects creations: class `BigIntegerIterator` does gives nothing but load.

- Tested with x=100_000
- Before: 29.299ms
- After: 7.356ms

2. Get rid of expensive (overall O(n*n)) removals from the collection; write only prime numbers to the resulting
   collection.

- Tested with x=100_000
- Before: 7.356ms
- After: 606ms

3. Optimize the isPrime method: checking numbers more than sqr(x) is pointless.

- This is tested with x=1m.
- Before: 9.581ms
- After: 817ms

### Second stage

Executions with x=1m load showed that the thread creation consumes so much memory that the `OutOfMemoryError` errors
start appearing.

Solutions:

4. Reduce the amount of threads to 50. After experimenting with the different number of threads, 50 seems to be the best
   option on this load.

- Tested with x=10m
- Before: 24.326ms
- After: 20.473ms

Alternative: usage of a `WorkStealingPool` with automatically calculated available processors

5. Remove second collection synchronization; we don’t iterate through the collection, hence the additional
   synchronization is not beneficial. Operations on the collection are already synchronized via the
   Collections.synchronizedList wrapper.

- Tested with x=10m
- Before: 20.473ms
- After: 16.094ms

### Third stage

One of the remaining hotspots is multiple add operations to collections. And we’ve ended up sorting the resulting
collection. Both influence performance badly.

Solutions:

6. Use boolean array to mark prime numbers concurrently and collect them into resulting list

- Tested with x=10m
- Before: 16.094ms
- After: 4.340ms

### Fourth stage (optional)

Most probably, this stage should be out of scope of the multithreading related assignment. However, further optimization
is possible with the usage of pre-calculated prime numbers, e.g. via the Sieve of Eratosthenes algorithm. This would
help to not over-engineer the solution prematurely without knowing specific load requirements and save resources.

Solution:

7. Precalculate primes using the Sieve of Eratosthenes algorithm.

- Tested with x=10m
- Before: 4.340ms
- After: 894ms



