//@formatter:off
/*
 * KeyCollisionCollectionPolicies
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

/**
 * This class provides convenient access to the standard policies for key collision collection.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyCollisionCollectionPolicies {

    /**
     * This collection mode tracks the keys but not all positions (only the first collision position) and the count, so we still get insides about the key's
     * nature but no longer about the position details (histogram data unavailable)
     */
    public static final KeyCollisionCollectionPolicy<CountingKeyCollision> COUNT_POSITIONS = new KeyCollisionCollectionPolicy<CountingKeyCollision>() {

        @Override
        public CountingKeyCollision createKeyCollision(long key, long... positions) {
            return new CountingKeyCollision(key, positions);
        }

        @Override
        public ItemStringCodec<CountingKeyCollision> getLineCodec() {
            return CountingKeyCollision.LINE_CODEC;
        }

    };

    /**
     * This light-weight collection mode only the first collision position and the count. The key will be discarded after detecting the collision. <br/>
     * This way we can get statistics, but neither of a single key not of its concrete collision distribution (histogram data unavailable).
     */
    public static final KeyCollisionCollectionPolicy<AnonymousCountingKeyCollision> COUNT_POSITIONS_AND_DISCARD_KEYS = new KeyCollisionCollectionPolicy<AnonymousCountingKeyCollision>() {

        @Override
        public AnonymousCountingKeyCollision createKeyCollision(long key, long... positions) {
            return new AnonymousCountingKeyCollision(positions);
        }

        @Override
        public ItemStringCodec<AnonymousCountingKeyCollision> getLineCodec() {
            return AnonymousCountingKeyCollision.LINE_CODEC;
        }

    };

    /**
     * This heavy collection mode tracks the key and all positions, it is made for runs where we are interested in the nature of key occurrences
     */
    public static final KeyCollisionCollectionPolicy<TrackingKeyCollision> TRACK_POSITIONS = new KeyCollisionCollectionPolicy<TrackingKeyCollision>() {

        @Override
        public TrackingKeyCollision createKeyCollision(long key, long... positions) {
            return new TrackingKeyCollision(key, positions);
        }

        @Override
        public ItemStringCodec<TrackingKeyCollision> getLineCodec() {
            return TrackingKeyCollision.LINE_CODEC;
        }

    };

    /**
     * This collection mode tracks all positions and discards the keys, it is made for runs where we are interested in collision statistics (histogram) but not
     * in the nature of the keys involved in collisions.
     * <p>
     * Recommended default
     */
    public static final KeyCollisionCollectionPolicy<AnonymousTrackingKeyCollision> TRACK_POSITIONS_AND_DISCARD_KEYS = new KeyCollisionCollectionPolicy<AnonymousTrackingKeyCollision>() {

        @Override
        public AnonymousTrackingKeyCollision createKeyCollision(long key, long... positions) {
            return new AnonymousTrackingKeyCollision(positions);
        }

        @Override
        public ItemStringCodec<AnonymousTrackingKeyCollision> getLineCodec() {
            return AnonymousTrackingKeyCollision.LINE_CODEC;
        }

    };

    /**
     * Static
     */
    private KeyCollisionCollectionPolicies() {
        // no instances
    }

}
