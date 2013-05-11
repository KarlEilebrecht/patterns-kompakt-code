/*
 * Request - Supplementary class representing objects using a Universally Unique ID (UUID/GUID)
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.uuid;

/**
 * Request - Supplementary class representing objects using a Universally Unique ID (UUID/GUID)
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Request implements Comparable<Request> {

    /**
     * Identifier
     */
    private final String id;

    /**
     * Time when request occurred (System.nanoTime())
     */
    private final long requestTimeNanos;

    /**
     * some request data
     */
    private String payload;

    /**
     * Creates new request object
     * @param id unique identifier
     * @param requestTimeNanos time request occurred (System.currentTimeNanos())
     * @param payload some data related to this request
     */
    public Request(String id, long requestTimeNanos, String payload) {
        this.id = id;
        this.requestTimeNanos = requestTimeNanos;
        this.payload = payload;
    }

    /**
     * Returns the payload associated to this request
     * @return payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets the payload associated to this request
     * @param payload request payload
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * Returns the uique ID of this request
     * @return unique id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the time this request occurred (System.nanoTime())
     * @return nanoTime
     */
    public long getRequestTimeNanos() {
        return requestTimeNanos;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Request other = (Request) obj;
        return other.id.equals(this.id);
    }

    @Override
    public int compareTo(Request o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({id=" + id + ", requestTimeNanos=" + requestTimeNanos + ", payload="
                + payload + "})";
    }

}
