/*
 * Throughput Event
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.util.tpl;

import java.text.NumberFormat;
import java.util.Locale;

import de.calamanari.pk.util.MiscUtils;

/**
 * A {@link ThroughputEvent} contains some KPIs about a {@link ThroughputLimiter}'s current state.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public final class ThroughputEvent {

    /**
     * clock UTC-time (System.currentTimeMillis())
     */
    final long eventTimeMillis = System.currentTimeMillis();

    /**
     * elapsed time (total) in nanoseconds
     */
    final long sysTimeNanos;

    /**
     * duration of the past interval in nanoseconds
     */
    final long intervalNanos;

    /**
     * estimated time in nanoseconds the system was overloaded during the past interval
     */
    final long intervalOverloadNanos;

    /**
     * number of granted permissions since limiter started to work
     */
    final long passedCount;

    /**
     * number of denied permissions since limiter started to work
     */
    final long deniedCount;

    /**
     * true if the limiter is currently overloaded, otherwise false
     */
    final boolean overloaded;

    /**
     * average throughput during the past interval (granted permissions per second)
     */
    final double intervalPerSecondThroughput;

    /**
     * average throughput since measurement started (granted permissions per second)
     */
    final double totalPerSecondThroughput;

    /**
     * Creates a new throughput event to be reported
     * @param sysTimeNanos time in nanoseconds when the event occurred, see also
     *            {@linkplain MiscUtils#getSystemUptimeNanos()}
     * @param intervalNanos exact length of the past interval in nanoseconds
     * @param intervalOverloadNanos time in nanoseconds within the interval, the limiter was overloaded
     * @param passedCount number of granted permissions during the past interval
     * @param deniedCount number of denied permissions during the past interval
     * @param currentPerSecondThroughput calculated throughput for this interval (permissions granted per second)
     * @param totalPerSecondThroughput calculated throughput throughout the listeners lifetime (permissions granted per
     *            second)
     * @param overloaded true if the limiter is currently overloaded, otherwise false
     */
    public ThroughputEvent(long sysTimeNanos, long intervalNanos, long intervalOverloadNanos, long passedCount,
            long deniedCount, double currentPerSecondThroughput, double totalPerSecondThroughput, boolean overloaded) {
        this.sysTimeNanos = sysTimeNanos;
        this.intervalNanos = intervalNanos;
        this.intervalOverloadNanos = intervalOverloadNanos;
        this.passedCount = passedCount;
        this.deniedCount = deniedCount;
        this.overloaded = overloaded;
        this.intervalPerSecondThroughput = currentPerSecondThroughput;
        this.totalPerSecondThroughput = totalPerSecondThroughput;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return this.getClass().getSimpleName() + "[eventTimeMillis=" + eventTimeMillis + ", sysTimeNanos="
                + sysTimeNanos + ", intervalNanos=" + intervalNanos + ", intervalOverloadNanos="
                + intervalOverloadNanos
                + ", passedCount=" + passedCount + ", deniedCount=" + deniedCount + ", overloaded=" + overloaded
                + ", totalThroughput=" + nf.format(totalPerSecondThroughput) + "/s, intervalThroughput="
                + nf.format(intervalPerSecondThroughput) + "/s]";
    }

    /**
     * Returns clock UTC-time in milliseconds, the event was created (System.currentTimeMillis())
     * @return time in milliseconds
     */
    public long getEventTimeMillis() {
        return eventTimeMillis;
    }

    /**
     * @return elapsed time (total) in nanoseconds
     */
    public long getSysTimeNanos() {
        return sysTimeNanos;
    }

    /**
     * @return duration of the past interval in nanoseconds
     */
    public long getIntervalNanos() {
        return intervalNanos;
    }

    /**
     * @return estimated time in nanoseconds the system was overloaded during the past interval
     */
    public long getIntervalOverloadNanos() {
        return intervalOverloadNanos;
    }

    /**
     * @return number of granted permissions since limiter started to work
     */
    public long getPassedCount() {
        return passedCount;
    }

    /**
     * @return number of denied permissions since limiter started to work
     */
    public long getDeniedCount() {
        return deniedCount;
    }

    /**
     * @return true if the {@link ThroughputLimiter} is currently overloaded, otherwise false
     */
    public boolean isOverloaded() {
        return overloaded;
    }

    /**
     * @return average throughput during the past interval (granted permissions per second)
     */
    public double getIntervalPerSecondThroughput() {
        return intervalPerSecondThroughput;
    }

    /**
     * @return average throughput since measurement started (granted permissions per second)
     */
    public double getTotalPerSecondThroughput() {
        return totalPerSecondThroughput;
    }

}