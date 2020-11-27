//@formatter:off
/*
 * Palindrome Check Future - demonstrates MASTER SLAVE
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//@formatter:on
package de.calamanari.pk.masterslave;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Palindrome Check Future - allows the MASTER to poll for the result and the SLAVES to report their results.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PalindromeCheckFuture implements Future<PalindromeCheckResult> {

    /**
     * Constant {@value} for progress calculation
     */
    private static final double PROGRESS_100 = 100.0;

    /**
     * The combination of the results from different threads is complete after all results have been reported. This is controlled by a CountDownLatch
     */
    private final CountDownLatch countDownLatch;

    /**
     * number of partitions
     */
    private final int numberOfPartitions;

    /**
     * The result of the threaded operation (combination of all results)
     */
    private final AtomicReference<PalindromeCheckResult> result = new AtomicReference<>(PalindromeCheckResult.UNKNOWN);

    /**
     * The operation may be canceled from outside
     */
    private final AtomicBoolean canceled = new AtomicBoolean(false);

    /**
     * This flag will be set if the result has been computed and now all remaining threads shall terminate as fast as possible.
     */
    private final AtomicBoolean abortDueToCompletion = new AtomicBoolean(false);

    /**
     * Creates new Future combining results of several partition checks
     * 
     * @param numberOfPartitions number of checks to be awaited
     */
    public PalindromeCheckFuture(int numberOfPartitions) {
        this.countDownLatch = new CountDownLatch(numberOfPartitions);
        this.numberOfPartitions = numberOfPartitions;
    }

    /**
     * This method indicates that we want to tell the slaves to terminate their work as fast as possible.
     * 
     * @return true if execution has been canceled or is aborted due to completion
     */
    public boolean isAborted() {
        return isCancelled() || abortDueToCompletion.get();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean res = canceled.get();
        if (!res && !isDone()) {
            canceled.set(true);
            res = true;
        }
        return res;
    }

    @Override
    public boolean isCancelled() {
        return canceled.get();
    }

    @Override
    public boolean isDone() {
        return (countDownLatch.getCount() == 0);
    }

    @Override
    public PalindromeCheckResult get() throws InterruptedException, ExecutionException {
        if (isCancelled()) {
            throw new ExecutionException(new IllegalStateException("Operation has been cancelled."));
        }
        countDownLatch.await();
        return result.get();
    }

    @Override
    public PalindromeCheckResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (isCancelled()) {
            throw new ExecutionException(new IllegalStateException("Operation has been cancelled."));
        }
        if (!countDownLatch.await(timeout, unit)) {
            throw new TimeoutException("Unable to deliver result in time.");
        }
        return result.get();
    }

    /**
     * does a fast count down, internally used after we know the result, this avoids the need for the MASTER to wait for all SLAVES to complete
     */
    protected void countDownRemaining() {
        for (int i = 0; i < numberOfPartitions; i++) {
            countDownLatch.countDown();
        }
    }

    /**
     * Each thread reports his result, the first one finding a difference (no palindrome) wins and causes the whole check to complete as fast as possible.
     * 
     * @param partitionResult result for one partition
     */
    public void reportSlaveResult(PalindromeCheckResult partitionResult) {
        if (!isAborted() && !partitionResult.equals(PalindromeCheckResult.UNKNOWN) && !partitionResult.isPalindromeConfirmed()) {
            // a slave reported a proof that the examined partition was no palindrome
            // now check if this was the first one ("the winner") who recognized a proof
            boolean callerIsWinner = abortDueToCompletion.compareAndSet(false, true);
            if (callerIsWinner) {
                result.set(partitionResult);
                countDownRemaining();
            }
        }
        else {
            countDownLatch.countDown();
            if (!isAborted() && countDownLatch.getCount() == 0 && partitionResult.isPalindromeConfirmed()) {
                // (1) operation was not aborted
                // (2) operation is done (countdown finished)
                // (3) all threads confirmed palindrome status
                // conclusion: the source was a palindrome
                result.compareAndSet(PalindromeCheckResult.UNKNOWN, PalindromeCheckResult.CONFIRMED);
            }
        }
    }

    /**
     * Returns the progress as a percentage value
     * 
     * @return percentage value of progress
     */
    public double getProgressPerc() {
        double done = countDownLatch.getCount();
        return PROGRESS_100 - ((done / numberOfPartitions) * PROGRESS_100);
    }

}
