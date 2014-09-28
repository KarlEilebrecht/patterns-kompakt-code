//@formatter:off
/*
 * Load State
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
//@formatter:on
package de.calamanari.pk.util.tpl;

import java.util.concurrent.CountDownLatch;

import de.calamanari.pk.util.MiscUtils;

/**
 * A {@link OverloadState} instance represents the overload state of the {@link ThroughputLimiter} including an overload start time and a latch (where
 * requesters can wait for).
 */
public final class OverloadState {

    /**
     * Dummy indicating {@link ThroughputLimiter} is currently not overloaded.
     */
    static final OverloadState NOT_OVERLOADED;
    static {
        OverloadState dummy = new OverloadState(-1);
        dummy.waitLatch.countDown(); // never wait for this dummy
        NOT_OVERLOADED = dummy;
    }

    /**
     * system when overload started
     */
    final long overloadStartTimeNanos;

    /**
     * Threads use this latch to wait for the end of the overload state.
     */
    final CountDownLatch waitLatch = new CountDownLatch(1);

    /**
     * Creates new overload state using the given start time
     * 
     * @param startTimeNanos time in nanoseconds when overload started, see {@linkplain MiscUtils#getSystemUptimeNanos()}
     */
    OverloadState(long startTimeNanos) {
        this.overloadStartTimeNanos = startTimeNanos;
    }

}