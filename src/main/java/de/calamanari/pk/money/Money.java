//@formatter:off
/*
 * Money - a MONEY demo implementation
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
package de.calamanari.pk.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Currency;

/**
 * Money - a MONEY demo implementation<br>
 * Please be aware that this implementation has absolutely nothing to do with high performance or optimized memory consumption. :-) <br>
 * You'll find full fledged and well tested Money libraries in the web.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Money implements Serializable, Comparable<Money> {

    /**
     * for serialization
     */
    private static final long serialVersionUID = -3239270263397346399L;

    /**
     * USA-Dollar
     */
    public static final Currency CURRENCY_USD = Currency.getInstance("USD");

    /**
     * Euro
     */
    public static final Currency CURRENCY_EUR = Currency.getInstance("EUR");

    /**
     * Value 100
     */
    private static final BigDecimal BD_100 = BigDecimal.valueOf(100);

    /**
     * Maximum scale: {@value}
     */
    private static final int MAX_SCALE = 20;

    /**
     * The currency of the value
     */
    private final Currency currency;

    /**
     * Value stored using the smallest unit
     */
    private final BigInteger valueOfSmallestUnit;

    /**
     * String representation, also used for hashcode and equals
     */
    private final String stringValue;

    /**
     * value as big decimal
     */
    private final BigDecimal bigDecimalValue;

    /**
     * Helper method to get the factor for a currency
     * 
     * @param currency the currency for the {@link Money}-instance
     * @return factor multiplication factor to calculate smallest unit of currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    private static final BigDecimal determineDefaultUnitFactor(Currency currency) {
        if (CURRENCY_EUR.equals(currency) || CURRENCY_USD.equals(currency)) {
            return BD_100;
        }
        throw new IllegalArgumentException("Unsupported currency: " + currency);
    }

    /**
     * Parses the currency code from the string representation of a Money instance
     * 
     * @param value string representation of Money
     * @return currency
     */
    private static final Currency parseCurrency(String value) {
        int pos = value.lastIndexOf(' ');
        String currencyCode = value.substring(pos + 1);
        return Currency.getInstance(currencyCode);
    }

    /**
     * Parses the monetary value from the string representation of a Money instance
     * 
     * @param value string representation of Money
     * @return monetary value
     * @throws IllegalArgumentException if the currency is not supported
     */
    private static final BigDecimal parseValue(String value) {
        int pos = value.lastIndexOf(' ');
        String sValue = value.substring(0, pos);
        return new BigDecimal(sValue);
    }

    /**
     * Creates a Money instance from a BigDecimal with the given currency
     * 
     * @param value instance's value
     * @param currency instance's currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(BigDecimal value, Currency currency) {
        this.currency = currency;
        BigDecimal defaultUnitFactor = determineDefaultUnitFactor(currency);
        this.valueOfSmallestUnit = value.multiply(defaultUnitFactor).setScale(0, RoundingMode.HALF_EVEN).toBigInteger();
        this.bigDecimalValue = new BigDecimal(valueOfSmallestUnit).divide(defaultUnitFactor, 2, RoundingMode.HALF_EVEN);
        this.stringValue = this.bigDecimalValue.toString() + " " + this.currency.getCurrencyCode();
    }

    /**
     * Creates a Money instance from a BigDecimal with the given currency
     * 
     * @param value instance's value
     * @param currencyCode identifier for currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(BigDecimal value, String currencyCode) {
        this(value, Currency.getInstance(currencyCode));
    }

    /**
     * Creates a Money instance from a double value with the given currency
     * 
     * @param value instance's value
     * @param currency the value's currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(double value, Currency currency) {
        this(BigDecimal.valueOf(value), currency);
    }

    /**
     * Creates a Money instance from a double value with the given currency
     * 
     * @param value instance's value
     * @param currencyCode identifier for currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(double value, String currencyCode) {
        this(BigDecimal.valueOf(value), currencyCode);
    }

    /**
     * Creates a Money instance from a long value with the given currency
     * 
     * @param value instance's value
     * @param currency instance's currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(long value, Currency currency) {
        this(BigDecimal.valueOf(value), currency);
    }

    /**
     * Creates a Money instance from a long value with the given currency
     * 
     * @param value instance's value
     * @param currencyCode identifier for currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(long value, String currencyCode) {
        this(BigDecimal.valueOf(value), currencyCode);
    }

    /**
     * Creates a Money instance from a BigInteger with the given currency
     * 
     * @param valueOfSmallestUnit instance's value in smallest units
     * @param currency instance's currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    private Money(BigInteger valueOfSmallestUnit, Currency currency) {
        this.currency = currency;
        BigDecimal defaultUnitFactor = determineDefaultUnitFactor(currency);
        this.valueOfSmallestUnit = valueOfSmallestUnit;
        this.bigDecimalValue = new BigDecimal(valueOfSmallestUnit).divide(defaultUnitFactor, 2, RoundingMode.HALF_EVEN);
        this.stringValue = this.bigDecimalValue.toString() + " " + this.currency.getCurrencyCode();
    }

    /**
     * Creates a Money instance from a String with the given currency
     * 
     * @param value stringified value (double or long)
     * @param currency instance's currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(String value, Currency currency) {
        this(new BigDecimal(value), currency);
    }

    /**
     * Creates a Money instance from a String with the given currency
     * 
     * @param value stringified value (double or long)
     * @param currencyCode identifier for currency
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(String value, String currencyCode) {
        this(new BigDecimal(value), Currency.getInstance(currencyCode));
    }

    /**
     * This constructor accepts the output of the toString()-method
     * 
     * @param value stringified Money instance
     * @throws IllegalArgumentException if the currency is not supported
     */
    public Money(String value) {
        this(parseValue(value), parseCurrency(value));
    }

    /**
     * Returns the corresponding BigDecimal value
     * 
     * @return big decimal value, precision limited related to currencies smallest unit
     */
    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    /**
     * Returns the corresponding double value
     * 
     * @return double value, precision limited related to currencies smallest unit
     */
    public double getDoubleValue() {
        return bigDecimalValue.doubleValue();
    }

    /**
     * Returns the value represented using the smallest unit corresponding to the currency, i.e. 100 Cents for 1 Dollar
     * 
     * @return value using smallest unit
     */
    public BigInteger getSmallestUnitValue() {
        return this.valueOfSmallestUnit;
    }

    /**
     * Determines the signum of this money instance
     * 
     * @return -1, 0 or 1 for negative, zero or positive values
     */
    public int getSignum() {
        return this.valueOfSmallestUnit.signum();
    }

    /**
     * Returns a new money instance as the negative value of this one
     * 
     * @return negative money instance
     */
    public Money negate() {
        return new Money(this.valueOfSmallestUnit.negate(), this.currency);
    }

    /**
     * Creates a new money instance as the sum of this one plus the given one
     * 
     * @param value money to add (must have the same currency)
     * @return new money instance
     */
    public Money add(Money value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot add null to Money instance.");
        }
        if (!this.currency.equals(value.currency)) {
            throw new IllegalArgumentException("Currency mismatch: cannot add " + value + " to " + this);
        }
        Money res = this;
        if (value.getSignum() != 0) {
            res = new Money(this.valueOfSmallestUnit.add(value.valueOfSmallestUnit), this.currency);
        }
        return res;
    }

    /**
     * Creates a new money instance as this one minus the given one
     * 
     * @param value money to subtract (must have the same currency)
     * @return new money instance
     */
    public Money substract(Money value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot subtract null from Money instance.");
        }
        return add(value.negate());
    }

    /**
     * Multiplies this instance with the given factor and returns a new Money instance for the result.
     * 
     * @param factor value to be multiplied
     * @return result
     */
    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Cannot multiply with null.");
        }
        return new Money(factor.multiply(this.bigDecimalValue).setScale(2, RoundingMode.HALF_EVEN), this.currency);
    }

    /**
     * Multiplies this instance with the given factor and returns a new Money instance for the result.
     * 
     * @param factor value to be multiplied
     * @return result
     */
    public Money multiply(double factor) {
        return this.multiply(BigDecimal.valueOf(factor));
    }

    /**
     * Multiplies this instance with the given factor and returns a new Money instance for the result.
     * 
     * @param factor value to be multiplied
     * @return result
     */
    public Money multiply(BigInteger factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Cannot multiply with null.");
        }
        return new Money(factor.multiply(this.valueOfSmallestUnit), this.currency);
    }

    /**
     * Multiplies this instance with the given factor and returns a new Money instance for the result.
     * 
     * @param factor value to be multiplied
     * @return result
     */
    public Money multiply(long factor) {
        return this.multiply(BigInteger.valueOf(factor));
    }

    /**
     * Divides this instance by the given divisor and returns a new Money instance for the result.
     * 
     * @param divisor value to be used for division
     * @return result
     */
    public Money divide(BigDecimal divisor) {
        if (divisor == null) {
            throw new IllegalArgumentException("Cannot divide by null.");
        }
        if (divisor.signum() == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return new Money(this.bigDecimalValue.divide(divisor, 2, RoundingMode.HALF_EVEN), this.currency);
    }

    /**
     * Divides this instance by the given divisor and returns a new Money instance for the result.
     * 
     * @param divisor value to be used for division
     * @return result
     */
    public Money divide(double divisor) {
        return this.divide(BigDecimal.valueOf(divisor));
    }

    /**
     * Divides this instance by the given divisor and returns a new Money instance for the result.
     * 
     * @param divisor value to be used for division
     * @return result
     */
    public Money divide(BigInteger divisor) {
        if (divisor == null) {
            throw new IllegalArgumentException("Cannot divide by null.");
        }
        return this.divide(new BigDecimal(divisor));
    }

    /**
     * Divides this instance by the given divisor and returns a new Money instance for the result.
     * 
     * @param divisor value to be used for division
     * @return result
     */
    public Money divide(long divisor) {
        return this.divide(BigDecimal.valueOf(divisor));
    }

    /**
     * Distributes <i>this</i> value according to the given weights, means [1,1,1] will have the same effect as [10,10,10].<br>
     * A potential remainder will be distributed to the items from the highest weight to the lowest, ignoring 0.
     * 
     * @param weights used to distribute <i>this</i> value
     * @return array of money instances (distributed value of this instance)
     */
    public Money[] distribute(BigDecimal[] weights) {
        Money[] res = null;
        if (weights.length == 0) {
            res = new Money[0];
        }
        else {
            BigDecimal sumOfWeights = computeSumOfWeights(weights);
            if (sumOfWeights.signum() == 0) {
                // no weights given, so result is a all zero
                res = new Money[weights.length];
                Arrays.fill(res, new Money(BigInteger.ZERO, this.currency));
            }
            else {
                res = distribute(weights, sumOfWeights);
            }
        }
        return res;
    }

    /**
     * Computes the distribution values and returns the created array.
     * 
     * @param weights used to distribute <i>this</i> value
     * @param sumOfWeights sum of all weights
     * @return distributed values
     */
    private Money[] distribute(BigDecimal[] weights, BigDecimal sumOfWeights) {

        Money[] res = new Money[weights.length];

        // first step: distribute according to weights
        // therefor calculate the basic amount (to be multiplied by each weight)
        BigDecimal basicAmount = this.bigDecimalValue.divide(sumOfWeights, MAX_SCALE, RoundingMode.DOWN);

        // calculate the amounts, but so that we do not distribute more than we have
        BigInteger sumOfSmallestUnit = BigInteger.ZERO;
        for (int i = 0; i < weights.length; i++) {
            Money amount = new Money(basicAmount.multiply(weights[i]).setScale(2, RoundingMode.DOWN), this.currency);
            res[i] = amount;
            sumOfSmallestUnit = sumOfSmallestUnit.add(amount.valueOfSmallestUnit);
        }

        // now it may happen that there are some pennies left
        BigInteger remainder = this.valueOfSmallestUnit.subtract(sumOfSmallestUnit);

        if (remainder.signum() != 0) {
            distributeRemainder(remainder, weights, res);
        }
        return res;
    }

    /**
     * Distributed the remainder value to the already distributed values to correct the total sum.
     * 
     * @param remainder amount not yet distributed
     * @param weights only add a part of the remainder if weight was positive
     * @param distributionValues the distribution to be modified
     */
    private void distributeRemainder(BigInteger remainder, BigDecimal[] weights, Money[] distributionValues) {
        // now let's distribute the remainder
        // we follow a very simple approach: round-robin, exclude 0-weights (0 remains 0)
        // Martin Fowler suggests the introduction of a random momentum
        // another idea was to privilege the bigger weights (or the smaller ones)
        // when distributing the remainder. Whatever you may decide, document
        // your decision and evtl. make the behaviour configurable.

        BigInteger delta = BigInteger.ONE;
        if (remainder.signum() < 0) {
            delta = delta.negate();
        }

        int pos = 0;
        int max = weights.length;
        while (remainder.signum() != 0) {
            int idx = pos % max;
            if (weights[idx].signum() > 0) {
                // add one to this result
                distributionValues[idx] = new Money(distributionValues[idx].valueOfSmallestUnit.add(delta), this.currency);
                remainder = remainder.subtract(delta);
            }
            pos++;
        }
    }

    /**
     * This method sums-up the given weights
     * 
     * @param weights array of weight values NOT NULL
     * @return sum of weights
     * @throws IllegalArgumentException if a weight value was null or negative
     */
    private BigDecimal computeSumOfWeights(BigDecimal[] weights) {
        BigDecimal sumOfWeights = BigDecimal.ZERO;
        for (BigDecimal weight : weights) {
            if (weight == null) {
                throw new IllegalArgumentException("Weight must not be null, given: " + Arrays.asList(weights));
            }
            if (weight.signum() < 0) {
                throw new IllegalArgumentException("Weight cannot be negative, given: " + Arrays.asList(weights));
            }
            sumOfWeights = sumOfWeights.add(weight);
        }
        return sumOfWeights;
    }

    /**
     * Distributes this value according to the given weights, means [1.0,1.0,1.0] will have the same effect as [10.0,10.0,10.0].<br>
     * A potential remainder will be distributed to the items from the highest weight to the lowest, ignoring 0.
     * 
     * @param weights used to distribute the value
     * @return array of money instances (distributed value of this instance)
     */
    public Money[] distribute(double... weights) {
        int len = weights.length;
        BigDecimal[] bdWeights = new BigDecimal[len];
        for (int i = 0; i < len; i++) {
            bdWeights[i] = BigDecimal.valueOf(weights[i]);
        }
        return distribute(bdWeights);
    }

    @Override
    public int compareTo(Money o) {
        int res = 0;
        if (o == null) {
            // nulls always to the end
            res = -1;
        }
        else {
            // group by currency code
            res = this.currency.getCurrencyCode().compareTo(o.currency.getCurrencyCode());

            if (res == 0) {
                // order by value
                res = this.bigDecimalValue.compareTo(o.bigDecimalValue);
            }
        }
        return res;
    }

    @Override
    public int hashCode() {
        return stringValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof Money) {
            res = this.stringValue.equals(((Money) obj).stringValue);
        }
        return res;
    }

    /**
     * Returns the string representation of this money instance of the form 'value currencyCode', the returned string may be used with the corresponding
     * constructor to recreate the money instance
     * 
     * @return debug string for value
     */
    @Override
    public String toString() {
        return this.stringValue;
    }

}
