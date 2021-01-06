//@formatter:off
/*
 * KeyCollisionDataPoint
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
package de.calamanari.pk.muhai;

import java.io.Serializable;

/**
 * {@link KeyCollisionDataPoint} is a VALUE OBJECT that represents a statistic data point, natural order is defined on {@link #getPosition()}.
 * <p>
 * If n generated keys are organized in m buckets, each contains k keys. Each data point represents a bucket B and tells us about collisions after generating B
 * * k keys.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyCollisionDataPoint implements Serializable, Comparable<KeyCollisionDataPoint> {

    private static final long serialVersionUID = -6428515723553324954L;

    /**
     * The upper bound of the bucket
     */
    private final long position;

    /**
     * Number of collisions so far (including earlier buckets)
     */
    private final long numberOfCollisionsDetected;

    /**
     * Number of collisions expected (estimation formula)
     */
    private final long numberOfCollisionsExpected;

    /**
     * @param position upper bound of the bucket
     * @param numberOfCollisionsDetected Number of collisions so far (including earlier buckets)
     * @param numberOfCollisionsExpected Number of collisions expected (estimation formula)
     */
    public KeyCollisionDataPoint(long position, long numberOfCollisionsDetected, long numberOfCollisionsExpected) {
        super();
        this.position = position;
        this.numberOfCollisionsDetected = numberOfCollisionsDetected;
        this.numberOfCollisionsExpected = numberOfCollisionsExpected;
    }

    /**
     * @return Upper bound of the bucket (key position)
     */
    public long getPosition() {
        return position;
    }

    /**
     * @return Number of detected collisions after generating {@link #getPosition()} keys
     */
    public long getNumberOfCollisionsDetected() {
        return numberOfCollisionsDetected;
    }

    /**
     * @return Number of expected collisions after generating {@link #getPosition()} keys
     */
    public long getNumberOfCollisionsExpected() {
        return numberOfCollisionsExpected;
    }

    @Override
    public String toString() {
        return KeyCollisionDataPoint.class.getSimpleName() + " [position=" + position + ", numberOfCollisionsDetected=" + numberOfCollisionsDetected
                + ", numberOfCollisionsExpected=" + numberOfCollisionsExpected + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (numberOfCollisionsDetected ^ (numberOfCollisionsDetected >>> 32));
        result = prime * result + (int) (numberOfCollisionsExpected ^ (numberOfCollisionsExpected >>> 32));
        result = prime * result + (int) (position ^ (position >>> 32));
        return result;
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
        KeyCollisionDataPoint other = (KeyCollisionDataPoint) obj;
        if (numberOfCollisionsDetected != other.numberOfCollisionsDetected) {
            return false;
        }
        if (numberOfCollisionsExpected != other.numberOfCollisionsExpected) {
            return false;
        }
        if (position != other.position) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(KeyCollisionDataPoint o) {
        int res = Long.compareUnsigned(this.position, o.position);
        if (res == 0) {
            res = Long.compareUnsigned(this.numberOfCollisionsDetected, o.numberOfCollisionsDetected);
        }
        if (res == 0) {
            res = Long.compareUnsigned(this.numberOfCollisionsExpected, o.numberOfCollisionsExpected);
        }
        return res;
    }

}