//@formatter:off
/*
 * DistributionCodecRandomGenerator
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2024 Karl Eilebrecht
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
package de.calamanari.pk.drhe;

import de.calamanari.pk.drhe.util.BitUtils;
import de.calamanari.pk.drhe.util.CommonBitMasks;

/**
 * Random number generator based on {@link DistributionCodec}.
 * <p/>
 * This implementation creates random numbers of type long based on repeated application of a distribution on the full long range.
 * <p/>
 * Be aware that this is solely meant for demonstration purposes. The code runs rather slow due to the number of cycles which are required to make the result
 * look random.
 * <p/>
 * Instances are stateful and must not be accessed concurrently by multiple threads.
 * <p/>
 * At <a href="https://mzsoltmolnar.github.io/random-bitstream-tester/">https://mzsoltmolnar.github.io/random-bitstream-tester/</a> you can check generated
 * output for randomness based on a <a href="https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software">NIST test suite</a>. I found the
 * <a href="https://en.wikipedia.org/wiki/Wald%E2%80%93Wolfowitz_runs_test">Runs Test</a> to be the worst enemy.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DistributionCodecRandomGenerator {

    /**
     * The last value returned by this instance which serves as the basis for the next value
     */
    private long lastValue;

    /**
     * We apply a noise during every round. Without the noise the chance to get into a (short) cycle is too high. I decided to use int here because this way I
     * can better tell that the "noise cycle" has a suffient length.
     */
    private int noise = 0;

    /**
     * @return long noise value
     */
    private long createNoise() {
        int i0 = DistributionCodec.encode(noise);
        int i1 = DistributionCodec.encode(i0);

        noise = i1;
        return DistributionCodec.encode(BitUtils.composeLong(i0, i1));
    }

    /**
     * @return next long value (full range)
     */
    public long nextValue() {
        long res = 0;

        long noiseValue = createNoise();
        res = DistributionCodec.encode(lastValue ^ noiseValue);
        lastValue = res;
        return res;

    }

    /**
     * Returns a long value in the range 0 .. limit (incl.). If limit is negative, only negative values will be generated.
     * 
     * @param limit range bound value
     * @return long value in range
     */
    public long nextValue(long limit) {
        if (limit == -1 || limit == 0 || limit == 1) {
            return 0;
        }
        long res = nextValue();
        if (res == 0) {
            return res;
        }
        else if (limit < 0) {
            if (limit == Long.MIN_VALUE) {
                return (res | CommonBitMasks.SET_FIRST_BIT_MASK_LONG);
            }
            else {
                return (res | CommonBitMasks.SET_FIRST_BIT_MASK_LONG) % (limit * -1);
            }
        }
        else {
            return (res & CommonBitMasks.UNSET_FIRST_BIT_MASK_LONG) % limit;
        }
    }

    /**
     * Initializes the new instance with <code>System.nanoTime()</code>
     */
    public DistributionCodecRandomGenerator() {
        lastValue = System.nanoTime();
    }

    /**
     * @param seed initialization value
     */
    public DistributionCodecRandomGenerator(long seed) {
        lastValue = seed;
    }

}