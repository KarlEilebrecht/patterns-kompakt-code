//@formatter:off
/*
 * BloomFilterConfig 
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
package de.calamanari.pk.ohbf;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A {@link BloomFilterConfig} consists of the best estimates for the parameters m, n, k and epsilon given two of them.<br/>
 * The remaining settings will be computed (derived). The parameter 'k' cannot be chosen and will always be estimated.
 * <p>
 * See <a href=
 * "https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions">https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions</a>
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomFilterConfig implements Serializable {

    private static final long serialVersionUID = -2854337179667029945L;

    /**
     * length of the bloom filter vector
     */
    private final long requiredNumberOfBitsM;

    /**
     * Expected number of inserts
     */
    private final long numberOfInsertedElementsN;

    /**
     * Desired or computed false-positive rate of the filter
     */
    private final double falsePositiveRateEpsilon;

    /**
     * Number of hashes (hash functions) required for the filter
     */
    private final int numberOfHashesK;

    /**
     * @param numberOfInsertedElementsN expected number of elements that will be inserted (max)
     * @param falsePositiveRateEpsilon desired false-positive rate
     */
    public BloomFilterConfig(long numberOfInsertedElementsN, double falsePositiveRateEpsilon) {

        double mApprox = -(((numberOfInsertedElementsN) * Math.log(falsePositiveRateEpsilon)) / Math.pow(Math.log(2), 2));
        double kApprox = -(Math.log(falsePositiveRateEpsilon) / Math.log(2));

        this.requiredNumberOfBitsM = (long) Math.ceil(mApprox);
        this.numberOfInsertedElementsN = numberOfInsertedElementsN;
        this.falsePositiveRateEpsilon = falsePositiveRateEpsilon;
        this.numberOfHashesK = (int) Math.max(1, Math.ceil(kApprox));

    }

    /**
     * @param falsePositiveRateEpsilon desired false-positive rate
     * @param requiredNumberOfBitsM size limitation for the bloom filter vector
     */
    public BloomFilterConfig(double falsePositiveRateEpsilon, long requiredNumberOfBitsM) {

        double kApprox = -(Math.log(falsePositiveRateEpsilon) / Math.log(2));
        double mDivByNApprox = -(Math.log(falsePositiveRateEpsilon) / Math.pow(Math.log(2), 2));
        double oneDivByNApprox = mDivByNApprox / requiredNumberOfBitsM;
        double nApprox = 1 / oneDivByNApprox;

        this.requiredNumberOfBitsM = requiredNumberOfBitsM;
        this.numberOfInsertedElementsN = (long) Math.ceil(nApprox);
        this.falsePositiveRateEpsilon = falsePositiveRateEpsilon;
        this.numberOfHashesK = (int) Math.max(1, Math.ceil(kApprox));

    }

    /**
     * @param requiredNumberOfBitsM size limitation for the bloom filter vector
     * @param numberOfInsertedElementsN expected number of elements that will be inserted (max)
     */
    public BloomFilterConfig(long requiredNumberOfBitsM, long numberOfInsertedElementsN) {
        double kApprox = (((double) requiredNumberOfBitsM) / numberOfInsertedElementsN) * Math.log(2);
        double lnEpsilon = -(((double) requiredNumberOfBitsM) / numberOfInsertedElementsN) * Math.pow(Math.log(2), 2);
        this.requiredNumberOfBitsM = requiredNumberOfBitsM;
        this.numberOfInsertedElementsN = numberOfInsertedElementsN;
        this.falsePositiveRateEpsilon = Math.pow(Math.E, lnEpsilon);
        this.numberOfHashesK = (int) Math.max(1, Math.ceil(kApprox));
    }

    /**
     * @return specified or computed size of the filter vector
     */
    public long getRequiredNumberOfBitsM() {
        return requiredNumberOfBitsM;
    }

    /**
     * @return specified or estimated maximum number of unique items that can be inserted into the filter
     */
    public long getNumberOfInsertedElementsN() {
        return numberOfInsertedElementsN;
    }

    /**
     * @return desired or estimated false-positive rate of the filter
     */
    public double getFalsePositiveRateEpsilon() {
        return falsePositiveRateEpsilon;
    }

    /**
     * @return computed number of hashes (hash-functions) required for this filter setup
     */
    public int getNumberOfHashesK() {
        return numberOfHashesK;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(falsePositiveRateEpsilon);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + numberOfHashesK;
        result = prime * result + (int) (numberOfInsertedElementsN ^ (numberOfInsertedElementsN >>> 32));
        result = prime * result + (int) (requiredNumberOfBitsM ^ (requiredNumberOfBitsM >>> 32));
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
        BloomFilterConfig other = (BloomFilterConfig) obj;
        return (Double.doubleToLongBits(falsePositiveRateEpsilon) == Double.doubleToLongBits(other.falsePositiveRateEpsilon))
                && (numberOfHashesK == other.numberOfHashesK) && (numberOfInsertedElementsN == other.numberOfInsertedElementsN)
                && (requiredNumberOfBitsM == other.requiredNumberOfBitsM);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(15);
        nf.setMaximumFractionDigits(15);
        return this.getClass().getSimpleName() + " [requiredNumberOfBitsM=" + requiredNumberOfBitsM + ", numberOfInsertedElementsN=" + numberOfInsertedElementsN
                + ", falsePositiveRateEpsilon=" + nf.format(falsePositiveRateEpsilon) + ", numberOfHashesK=" + numberOfHashesK + "]";
    }

}