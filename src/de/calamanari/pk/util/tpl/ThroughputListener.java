/*
 * Throughput Listener
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

/**
 * Clients may register a {@link ThroughputListener} to observe a {@link ThroughputLimiter}.<br>
 * The listener will be called periodically in a separate thread.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>>
 */
public interface ThroughputListener {

    /**
     * Notifies the listener about the current state of the {@link ThroughputLimiter}.<p>
     * <b>Note:</b> This method runs synchronously related to the checker thread. On the one hand there
     * cannot be a second call in parallel. On the other hand a blocking or overly long executing listener method
     * impacts the monitoring as it delays the next notification. Thus concrete implementations should run fast.
     * @param event current status of the observed {@link ThroughputLimiter}
     */
    public void handleThroughputData(ThroughputEvent event);
    
    /**
     * This method will be called if the periodically running checker thread has died for an unknown reason.
     * @param problem throwable which might give a hint why the listener died
     */
    public void handleListenerDied(Throwable problem);
    
}