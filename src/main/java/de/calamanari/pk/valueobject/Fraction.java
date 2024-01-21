//@formatter:off
/*
 * Fraction - demonstrates a VALUE OBJECT.
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
package de.calamanari.pk.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Objects;

/**
 * Fraction demonstrates a VALUE OBJECT, fractions are immutable. When converted to BigDecimal, a scale of 20 will be used.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Fraction implements Comparable<Fraction>, Serializable {

    /**
     * Null-safe comparator for views (null to the bottom). If the values of the two fractions are the same, this comparator orders them according to their
     * numerators: 1/3, 2/6, 4/12
     * <p>
     * This <i>view behavior</i> must not be implemented in {@link Fraction#compareTo(Fraction)} due to the <i><b>{@linkplain Comparable} interface
     * contract</b></i> which requires <code>equals()</code> and <code>compareTo()</code> to be implemented <i>consistently</i>.
     */
    public static final Comparator<Fraction> VIEW_COMPARATOR = (Fraction o1, Fraction o2) -> {
        int res = 0;
        if (o1 != null || o2 != null) {
            if (o1 == null) {
                res = 1;
            }
            else if (o2 == null) {
                res = -1;
            }
            else {
                res = o1.compareTo(o2);
                if (res == 0 && !o1.denominator.equals(o2.denominator)) {
                    res = o1.numerator.compareTo(o2.numerator);
                }
            }
        }
        return res;
    };

    /**
     * for serialization
     */
    private static final long serialVersionUID = -8085870257814104721L;

    /**
     * Maximum scaling: {@value}
     */
    private static final int MAX_SCALING = 20;

    /**
     * When creating fractions from double or big decimal we respect {@value #MAX_SCALING} decimals max This will be used for calculation.
     */
    private static final BigDecimal MAX_SCALED_VALUE = new BigDecimal("100000000000000000000");

    /**
     * When creating fractions from double or big decimal this will be the default denominator.
     */
    private static final BigInteger DEFAULT_DENOMINATOR = MAX_SCALED_VALUE.toBigInteger();

    /**
     * Nominator of fraction
     */
    private final BigInteger numerator;

    /**
     * Denominator of fraction
     */
    private final BigInteger denominator;

    /**
     * Nominator of reduced fraction
     */
    private final BigInteger reducedNumerator;

    /**
     * Denominator of reduced fraction
     */
    private final BigInteger reducedDenominator;

    /**
     * String representation
     */
    private final String fractionAsString;

    /**
     * Creates a fraction from two BigInteger values
     * 
     * @param numerator fraction's numerator, NOT NULL
     * @param denominator fraction's denominator, NOT NULL, NOT ZERO
     * @param reduce if true, the result fraction will be reduced automatically
     */
    public Fraction(BigInteger numerator, BigInteger denominator, boolean reduce) {
        if (numerator == null || denominator == null) {
            throw new IllegalArgumentException("Arguments must not be null (given numerator=" + numerator + ", denominator=" + denominator + ")");
        }
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator 0 (division by zero)");
        }

        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        BigInteger[] numeratorAndDenominator = reduce(numerator, denominator);

        if (reduce) {
            numerator = numeratorAndDenominator[0];
            denominator = numeratorAndDenominator[1];
        }
        this.numerator = numerator;
        this.denominator = denominator;
        this.reducedNumerator = numeratorAndDenominator[0];
        this.reducedDenominator = numeratorAndDenominator[1];
        this.fractionAsString = "(" + this.numerator + "/" + this.denominator + ")";
    }

    /**
     * Creates a fraction from two BigInteger values
     * 
     * @param numerator fraction's numerator, NOT NULL
     * @param denominator fraction's denominator, NOT NULL, NOT ZERO
     */
    public Fraction(BigInteger numerator, BigInteger denominator) {
        this(numerator, denominator, false);
    }

    /**
     * Creates a fraction from two Strings (integer values)
     * 
     * @param numerator fraction's numerator, correctly formatted integer number, NOT NULL
     * @param denominator fraction's denominator, correctly formatted integer number, NOT NULL, NOT ZERO
     */
    public Fraction(String numerator, String denominator) {
        this(new BigInteger(numerator), new BigInteger(denominator));
    }

    /**
     * Recreates the fraction from its string representation, see {@link #toString()} For convenience this constructor also accepts properly formatted integer
     * and double values
     * 
     * @param fraction stringified fraction, NOT NULL
     */
    public Fraction(String fraction) {
        this(parseNumerator(fraction), parseDenominator(fraction), true);
    }

    /**
     * Creates a fraction from two long values
     * 
     * @param numerator fraction's numerator
     * @param denominator fraction's denominator, NOT ZERO
     */
    public Fraction(long numerator, long denominator) {
        this(new BigInteger("" + numerator), new BigInteger("" + denominator));
    }

    /**
     * Supplementary constructor
     * 
     * @param numeratorAndDemoninator
     * @param reduce if true, the result fraction will be reduced automatically
     */
    private Fraction(BigInteger[] numeratorAndDemoninator, boolean reduce) {
        this(numeratorAndDemoninator[0], numeratorAndDemoninator[1], reduce);
    }

    /**
     * Creates a fraction from the given BigDecimal, preserving 20 decimals
     * 
     * @param value NOT NULL
     */
    public Fraction(BigDecimal value) {
        this(convertBigDecimalToBigIntegers(value), true);
    }

    /**
     * Creates a fraction from the given double, preserving decimals as possible
     * 
     * @param value number to be converted into a fraction
     */
    public Fraction(double value) {
        this(BigDecimal.valueOf(value).setScale(MAX_SCALING, RoundingMode.HALF_EVEN));
    }

    /**
     * Helper method to parse the numerator from stringified fraction
     * 
     * @param fraction string representation of a {@link Fraction}
     * @return numerator
     */
    private static final BigInteger parseNumerator(String fraction) {
        int start = fraction.indexOf('(') + 1;
        if (start == 0) {
            return (new BigDecimal(fraction)).multiply(MAX_SCALED_VALUE).toBigInteger();
        }
        int end = fraction.indexOf('/', start);
        return new BigInteger(fraction.substring(start, end));
    }

    /**
     * Helper method to parse the numerator from stringified fraction
     * 
     * @param fraction string representation of a {@link Fraction}
     * @return denominator
     */
    private static final BigInteger parseDenominator(String fraction) {
        int start = fraction.indexOf('/') + 1;
        if (start == 0) {
            return DEFAULT_DENOMINATOR;
        }

        int end = fraction.indexOf(')');
        return new BigInteger(fraction.substring(start, end));
    }

    /**
     * Returns a two-values array, used during construction
     * 
     * @param value big decimal value
     * @return array[2] of big integer
     */
    private static final BigInteger[] convertBigDecimalToBigIntegers(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Argument value must not be null");
        }
        BigInteger numerator = value.multiply(MAX_SCALED_VALUE).toBigInteger();
        BigInteger denominator = DEFAULT_DENOMINATOR;
        return new BigInteger[] { numerator, denominator };
    }

    /**
     * Returns the numerator
     * 
     * @return numerator
     */
    public BigInteger getNumerator() {
        return this.numerator;
    }

    /**
     * Returns the denominator
     * 
     * @return denominator
     */
    public BigInteger getDenominator() {
        return this.denominator;
    }

    /**
     * Returns a negative copy of this fraction
     * 
     * @return fraction * -1
     */
    public Fraction negate() {
        return new Fraction(this.numerator.negate(), this.denominator);
    }

    /**
     * Returns the inverse of this fraction
     * 
     * @return (denomninator / numerator)
     */
    public Fraction getInverse() {
        return new Fraction(this.denominator, this.numerator);
    }

    /**
     * Returns the sum of this fraction and the given one
     * 
     * @param fraction value to be added
     * @return sum fraction
     */
    // ignore SonaLint Rule false-positive ("!fraction.numerator.equals(BigInteger.ZERO)" not always evaluates to true)
    @SuppressWarnings("java:S2589")
    public Fraction add(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Argument must not be null.");
        }

        Fraction result = this;
        if (!fraction.numerator.equals(BigInteger.ZERO)) {
            BigInteger numeratorLocal;
            BigInteger denominatorLocal;

            if (this.denominator != fraction.denominator) {
                Fraction[] fractions = toCommonDenominator(this, fraction);
                numeratorLocal = fractions[0].numerator.add(fractions[1].numerator);
                denominatorLocal = fractions[0].denominator;
            }
            else {
                numeratorLocal = this.numerator.add(fraction.numerator);
                denominatorLocal = this.denominator;
            }
            if (numeratorLocal.equals(BigInteger.ZERO)) {
                denominatorLocal = BigInteger.ONE;
            }
            result = (new Fraction(numeratorLocal, denominatorLocal)).reduce();
        }
        return result;
    }

    /**
     * Returns this fraction minus the given one
     * 
     * @param fraction value to be subtracted
     * @return this - fraction
     */
    public Fraction subtract(Fraction fraction) {
        return add(fraction.negate());
    }

    /**
     * Determines if the value of this fractions represents the value (1/1)
     * 
     * @return true, if this fraction is equal to <code>1</code>
     */
    public boolean isOne() {
        return this.reducedDenominator.equals(BigInteger.ONE) && this.reducedNumerator.equals(BigInteger.ONE);
    }

    /**
     * Returns the product of the two fraction
     * 
     * @param fraction value to be multiplied with this fraction
     * @return this * fraction
     */
    public Fraction multiply(Fraction fraction) {
        if (fraction.isOne()) {
            return this;
        }
        return (new Fraction(this.numerator.multiply(fraction.numerator), this.denominator.multiply(fraction.denominator))).reduce();
    }

    /**
     * Returns the result of the division of this fraction by the given one
     * 
     * @param fraction divisor
     * @return this / fraction
     */
    public Fraction divide(Fraction fraction) {
        if (fraction.isOne()) {
            return this;
        }
        return (new Fraction(this.numerator.multiply(fraction.denominator), this.denominator.multiply(fraction.numerator))).reduce();
    }

    /**
     * Determines the signum of this fraction
     * 
     * @return -1, 0 or 1 for negative, zero or positive values
     */
    public int getSignum() {
        return this.numerator.signum();
    }

    /**
     * Returns a reduced version of this fraction
     * 
     * @return reduced fraction
     */
    public Fraction reduce() {
        Fraction res = this;
        if (!this.numerator.equals(this.reducedNumerator)) {
            res = new Fraction(this.reducedNumerator, this.reducedDenominator);
        }
        return res;
    }

    /**
     * finds the greatest common divisor
     * 
     * @param val1 first value
     * @param val2 second value
     * @return greatest common divisor
     */
    protected static final BigInteger gcd(BigInteger val1, BigInteger val2) {
        val1 = val1.abs();
        val2 = val2.abs();

        do {
            BigInteger r = val1.remainder(val2);
            val1 = val2;
            val2 = r;
        } while (!val2.equals(BigInteger.ZERO));
        return val1;
    }

    /**
     * finds the smallest common multiple
     * 
     * @param val1 first value
     * @param val2 second value
     * @return smallest common multiple
     */
    protected static final BigInteger scm(BigInteger val1, BigInteger val2) {

        BigInteger res = val1.multiply(val2).divide(gcd(val1, val2));

        if (res.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Zero is no common multiple.");
        }
        return res;
    }

    /**
     * Returns an array with the numerator and denominator after reduction (division by greatest common divisor)
     * 
     * @param numerator fraction's numerator, NOT NULL
     * @param denominator fraction's denominator, NOT NULL, NOT ZERO
     * @return array with numerator and denominator
     */
    protected static final BigInteger[] reduce(BigInteger numerator, BigInteger denominator) {
        if (!numerator.equals(BigInteger.ZERO)) {
            BigInteger gcd = gcd(numerator, denominator);
            if (gcd.compareTo(BigInteger.ONE) > 0) {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
        }
        else {
            denominator = BigInteger.ONE;
        }
        return new BigInteger[] { numerator, denominator };
    }

    /**
     * Brings the two fractions on a common denominator (smallest common multiple) and returns the result
     * 
     * @param fraction1 first fraction
     * @param fraction2 second fraction
     * @return array of two fractions with the same denominator
     */
    public Fraction[] toCommonDenominator(Fraction fraction1, Fraction fraction2) {
        if (fraction1 == null || fraction2 == null) {
            throw new IllegalArgumentException("Arguments must not be null (given: fraction1=" + fraction1 + ", fraction2=" + fraction2 + ")");
        }
        Fraction[] fractions = new Fraction[2];

        BigInteger scm = scm(fraction1.reducedDenominator, fraction2.reducedDenominator);

        BigInteger factor1 = scm.divide(fraction1.reducedDenominator);
        BigInteger factor2 = scm.divide(fraction2.reducedDenominator);

        fractions[0] = new Fraction(fraction1.reducedNumerator.multiply(factor1), scm);
        fractions[1] = new Fraction(fraction2.reducedNumerator.multiply(factor2), scm);
        return fractions;
    }

    /**
     * Returns the BigDecimal value of this fraction, assuming a scale of 20, rounding mode will be half-even
     * 
     * @return BigDecimal representation of this fraction
     */
    public BigDecimal getBigDecimalValue() {
        BigDecimal bd = new BigDecimal(this.reducedNumerator);
        return bd.divide(new BigDecimal(this.reducedDenominator), MAX_SCALING, RoundingMode.HALF_EVEN);
    }

    /**
     * Returns the corresponding double value with maximum precision
     * 
     * @return double value
     */
    public double getDoubleValue() {
        return getBigDecimalValue().doubleValue();
    }

    /**
     * Two fractions are equal if their corresponding reduced fractions are equal
     * 
     * @param obj another fraction
     * @return true if both fractions represent the same value, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof Fraction other) {
            res = (this.reducedDenominator.equals(other.reducedDenominator) && this.reducedNumerator.equals(other.reducedNumerator));
        }
        return res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reducedNumerator, this.reducedDenominator);
    }

    /**
     * Returns the string representation of this fraction of the form '(numerator/denominator)', the returned string may be used with the corresponding
     * constructor to recreate the fraction
     * 
     * @return fraction as String
     */
    @Override
    public String toString() {
        return this.fractionAsString;
    }

    @Override
    public int compareTo(Fraction other) {
        int res = 0;
        if (this.denominator.equals(other.denominator)) {
            res = this.numerator.compareTo(other.numerator);
        }
        else if (!this.reducedNumerator.equals(other.reducedNumerator) || !this.reducedDenominator.equals(other.reducedDenominator)) {
            Fraction[] fractions = toCommonDenominator(this, other);
            res = fractions[0].numerator.compareTo(fractions[1].numerator);
        }
        return res;
    }

}
