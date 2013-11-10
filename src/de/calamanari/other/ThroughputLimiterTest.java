package de.calamanari.other;

import static org.junit.Assert.assertEquals;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.calamanari.other.ThroughputLimiter.ThroughputEvent;
import de.calamanari.other.ThroughputLimiter.ThroughputListener;

public class ThroughputLimiterTest {

    
    
    
    
    
    
    public void testLongEncoding() throws Exception {

        long val = (long)Math.pow(2, 50);
        //System.out.println(val);
        
        
        int testOld = 8191;
        for (int i = 0; i < 16384; i++) {
            int test = i - 8192;
            
            int delta = test - testOld;
            
            assertEquals(1, delta);
            
            
        }
        
        for (int i = 0; i < 16384; i++) {
            int collisionCounter = i - 8192;
            for (int j = 1; j <= 2592; j++) {
                
                long time = 100_000_000_000L * j;
                long code = (((long)collisionCounter) << 50) + time;
                long time2 = (code & 72_057_594_037_927_936L);

//                System.out.println(Long.toBinaryString(collisionCounter));
//                System.out.println(Long.toBinaryString(time));
//                System.out.println(Long.toBinaryString(code));
//                System.out.println(Long.toBinaryString(ThroughputLimiter.MAX_INTERVAL_NANOS));
//                System.out.println(Long.toBinaryString(time2));
                
                int collisionCounter2 = (int)(code >> 50);
//                System.out.println(Long.toBinaryString(collisionCounter2));

                assertEquals(time, time2);
                assertEquals(collisionCounter, collisionCounter2);
                
                
            }
            
        }
        
    }
    
    
    @Test
    public void testDelta() throws Exception {
        
        assertEquals(3L, computeDeltaNanos(Long.MAX_VALUE - 1,  Long.MIN_VALUE + 1));
        assertEquals(3L, computeDeltaNanos(50L,  53L));
        assertEquals(103L, computeDeltaNanos(-50L,  53L));
        assertEquals(1L, computeDeltaNanos(Long.MAX_VALUE - 1,  Long.MAX_VALUE));
        assertEquals(1L, computeDeltaNanos(Long.MIN_VALUE,  Long.MIN_VALUE + 1));
            
        //long time = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
        //assertEquals(time, ThroughputLimiter.getSystemTimeNanos()/1_000_000);
        
        int i = Integer.MAX_VALUE;
        i = i + 10;
        i = i + Integer.MIN_VALUE;
        System.out.println("---> " + i);
        System.out.println("----> " + (long)Math.pow(2, 56));
        System.out.println("----> " + (long)Math.pow(2, 54));
        
        
        
    }


    private long computeDeltaNanos(long startTime, long nowTime) {
        long time = 0;
        if (startTime > 0 && nowTime < 0) {
            startTime = startTime - Long.MAX_VALUE;
            nowTime = nowTime - Long.MAX_VALUE;
        }
        
        if (startTime <= 0) {
            time = startTime - nowTime;
            if (time < 0) {
                time = time * (-1);
            }
        }
        else {
            time = nowTime - startTime;
        }
        return time;
    }
    
    @Test
    public void testLoad() throws Exception {
        
        
        final int numberOfThreads = 100;
        final int numberOfCallsPerThread = 1000000;

        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishedLatch = new CountDownLatch(numberOfThreads);
        
        final ThroughputLimiter limiter = new ThroughputLimiter(60000, 10, TimeUnit.MICROSECONDS);
        
        limiter.registerThroughputListener(new ThroughputListener() {
            
            @Override
            public void handleThroughputData(ThroughputEvent event) {
                System.out.println(event);
                
            }
            
            @Override
            public void handleListenerDied(Throwable problem) {
                // TODO Auto-generated method stub
                
            }
        }, 1000);
        
        List<LoadThread> threads = new ArrayList<>(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            LoadThread thread = new LoadThread(limiter, startLatch, numberOfCallsPerThread, finishedLatch);
            thread.start();
            threads.add(thread);
        }
        
        Thread.sleep(1000);
        long time = System.nanoTime();
        startLatch.countDown();
        finishedLatch.await();
        time = System.nanoTime() - time;
        double timeSeconds = ((double)time) / 1_000_000_000;
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(4);
        nf.setMinimumFractionDigits(4);
        System.out.println("Operation took " + nf.format(timeSeconds) + " seconds.");

        long sumPassed = 0;
        long sumDenied = 0;
        for (LoadThread thread : threads) {
            sumDenied = sumDenied + thread.numberOfDenied;
            sumPassed = sumPassed + thread.numberOfPassed;
        }
        
        System.out.println("Passed: " + sumPassed + ", denied: " + sumDenied);
        
        
        
    }
    
    
    public static class LoadThread extends Thread {
        
        private final ThroughputLimiter limiter;
        
        private final CountDownLatch startLatch;
        
        private final int numberOfCalls;
        
        private final CountDownLatch finishedLatch;

        volatile long numberOfPassed = 0;
        volatile long numberOfDenied = 0;
        
        public LoadThread(ThroughputLimiter limiter, CountDownLatch startLatch, int numberOfCalls,
                CountDownLatch finishedLatch) {
            this.limiter = limiter;
            this.startLatch = startLatch;
            this.numberOfCalls = numberOfCalls;
            this.finishedLatch = finishedLatch;
        }
        

        public void run() {
            try {
                startLatch.await();
                for (int i = 0; i < numberOfCalls; i++) {
                    boolean res = true;
                    if (limiter != null) {
                        res = limiter.tryGetPermission(); //(1000, TimeUnit.MILLISECONDS);
                        //limiter.getPermission();
                        Thread.yield();
//                        try {
//                            if (i % 17 == 0) {
//                                Thread.sleep(0, 1);
//                            }
//                        }
//                        catch (InterruptedException ex) {
//                            break;
//                        }  
                    }
                    if (res) {
                        numberOfPassed++;
                    }
                    else {
                        numberOfDenied++;
                        //i--;
                    }
                }
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            finally {
                finishedLatch.countDown();
            }
        }
        
    }
    
    
}
