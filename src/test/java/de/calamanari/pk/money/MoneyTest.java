/*
 * Money Test - demonstrates MONEY pattern.
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
package de.calamanari.pk.money;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.money.Money;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Money Test - demonstrates MONEY pattern and consequences when ignoring the basic problem.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MoneyTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(MoneyTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, MoneyTest.class, Money.class);
    }

    @Test
    public void testMoneyTechBasics() {

        LOGGER.info("Test Money Technical Basics ...");
        long startTimeNanos = System.nanoTime();

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(17);
        nf.setMinimumFractionDigits(17);

        double d1 = 0.00000000000000000;
        double d2 = 1.00000000000000000;
        double d3 = 0.30000000000000000;
        double dd = 0.10000000000000000;
        double dd2 = 0.20000000000000000;

        assertEquals("0.00000000000000000", nf.format(d1));
        assertEquals("1.00000000000000000", nf.format(d2));
        assertEquals("0.30000000000000000", nf.format(d3));

        for (int i = 0; i < 10; i++) {
            d1 = d1 + dd;
        }

        d3 = dd + dd2;

        // huh?
        assertFalse("1.00000000000000000".equals(nf.format(d1)));
        assertFalse("0.30000000000000000".equals(nf.format(d3)));

        // the reason is the binary representation
        // computers use internally.
        // Java implements the IEEE 754 floating point format (1985).
        // There are values that simply CANNOT be represented
        // EXACTLY this way, so the nearest value will be assumed.
        // For many (if not the most) applications this is
        // acceptable since the algorithm is clever to blur
        // the problem as well as possible.

        // The idea: use a data type that
        // uses an exact representation model and INTENTIONAL choose
        // precision (number of decimals) and rounding mode.
        // It is important to understand, that using these mechanisms
        // is not a silver bullet against "spooky problems",
        // it is a way to enforce and clearly document decisions
        // to ensure software behaves in a DEFINED WAY.
        // Whether the choice (precision and especially rounding mode ... )
        // was GOOD, hmm, that's another story :-)

        // now the same with big decimal (NOT using double)
        BigDecimal bd1 = new BigDecimal("0.00000000000000000");
        BigDecimal bd2 = new BigDecimal("1.00000000000000000");
        BigDecimal bd3 = null;
        BigDecimal bdd = new BigDecimal("0.10000000000000000");
        BigDecimal bdd2 = new BigDecimal("0.20000000000000000");
        assertEquals("0.00000000000000000", nf.format(bd1));
        assertEquals("1.00000000000000000", nf.format(bd2));
        for (int i = 0; i < 10; i++) {
            bd1 = bd1.add(bdd);
        }

        bd3 = bdd.add(bdd2);

        // aha, same test as above, but now expected to evaluate to true
        assertTrue("1.00000000000000000".equals(nf.format(bd1)));
        assertTrue("0.30000000000000000".equals(nf.format(bd3)));

        // But there are further problems:
        // A typical currency system defines different units
        // such as Dollars and Cents or Euros and Cents
        // In reality half a Cent cannot be expressed
        // (although it exists). Internally a calculation may work with
        // fractions of the smallest unit, but at the moment
        // humans come into play, values as 3.14532 Cents
        // will have to be handled in some way (truncate or round).

        // here you can see a typical effect:
        // imagine Harry, Laura, Ben and 4 Euros for them
        BigDecimal value = new BigDecimal(4);
        BigDecimal amount = null;
        Exception caughtEx = null;
        try {
            amount = value.divide(new BigDecimal(3));
        }
        catch (Exception ex) {
            caughtEx = ex;
        }

        assertTrue(caughtEx instanceof ArithmeticException);
        // huh?
        // Problem 1: 1/3 has no exact representation, its a surd number
        // Solution: As mentioned above: YOU must decide how precise you want to be

        // ok, assume 20 decimals will be adequate, use mercantile rounding (it's money!)
        amount = value.divide(new BigDecimal(3), 20, RoundingMode.HALF_EVEN);

        BigDecimal valueHarry = amount;
        BigDecimal valueLaura = amount;
        BigDecimal valueBen = amount;

        // now let's hand-over the money
        NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
        nf2.setMaximumFractionDigits(2);
        nf2.setMinimumFractionDigits(2);

        String sValueHarry = nf2.format(valueHarry);
        String sValueLaura = nf2.format(valueLaura);
        String sValueBen = nf2.format(valueBen);

        // now, Harry, Laura and Ben decide to buy
        // something together
        BigDecimal valueTogether = (new BigDecimal(sValueHarry)).add(new BigDecimal(sValueLaura)).add(
                new BigDecimal(sValueBen));

        // but in the shop they get a problem, the price is 4.00 Euros ...
        assertEquals("3.99", nf2.format(valueTogether));

        // Problem 2: What happened to the missing Cent?
        // As mentioned before this is rather a problem of unit of measurement than
        // a computer problem, the only solution is to give the Cent to someone
        // instead of taking fractions: rather distribute than divide

        // Hint: If you're going to work with BigDecimal please know that there are some
        // methods accepting a SCALE attribute while others accept PRECISION and some
        // ask for a MathContext object.
        // SCALE and PRECISION are not the same! Please review the API. Especially the MathContext
        // object can drive you crazy if you don't know what you're doing. MathContext objects
        // only know the PRECISION: the number of digits before PLUS after the decimal point.
        // Methods allowing to specify the SCALE (like BigDecimal.divide) will be your
        // friends if you try to define the number of digits AFTER the decimal point.

        LOGGER.info("Test Money Technical Basics successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testMoney() {
        LOGGER.info("Test Money ...");
        long startTimeNanos = System.nanoTime();

        Money money1 = new Money("10 EUR");
        Money money2 = new Money("10.12 EUR");
        Money money3 = new Money("11.18", "EUR");
        Money money4 = new Money(10.18, "USD");
        Money money5 = new Money(10.187912, "USD");

        Money[] moneyArray = new Money[] { money5, money4, money3, money2, money1 };
        assertEquals("[10.19 USD, 10.18 USD, 11.18 EUR, 10.12 EUR, 10.00 EUR]", Arrays.asList(moneyArray).toString());
        Arrays.sort(moneyArray);
        assertEquals("[10.00 EUR, 10.12 EUR, 11.18 EUR, 10.18 USD, 10.19 USD]", Arrays.asList(moneyArray).toString());

        assertEquals("10.00 EUR", money1.toString());
        assertEquals("10.12 EUR", money2.toString());
        assertEquals("11.18 EUR", money3.toString());
        assertEquals("10.18 USD", money4.toString());
        assertEquals("10.19 USD", money5.toString());

        Money sum = null;
        Exception caughtEx = null;
        try {
            sum = money3.add(money4);
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertTrue(caughtEx instanceof IllegalArgumentException);

        sum = money2.add(money3.negate());
        assertTrue(sum.getSignum() < 0);

        Money[] parts = sum.distribute(1, 1, 1);
        assertEquals("[-0.36 EUR, -0.35 EUR, -0.35 EUR]", Arrays.asList(parts).toString());

        parts = sum.negate().distribute(1, 1, 1, 0);
        assertEquals("[0.36 EUR, 0.35 EUR, 0.35 EUR, 0.00 EUR]", Arrays.asList(parts).toString());

        // back to the example related to Harry, Laura and Ben
        Money moneyExample = new Money("4 EUR");
        Money[] amounts = moneyExample.distribute(1, 1, 1); // three equal amounts

        Money valueHarry = amounts[0];
        Money valueLaura = amounts[1];
        Money valueBen = amounts[2];

        String sValueHarry = valueHarry.toString();
        String sValueLaura = valueLaura.toString();
        String sValueBen = valueBen.toString();

        // back at the shop:
        Money valueTogether = (new Money(sValueHarry)).add(new Money(sValueLaura)).add(new Money(sValueBen));

        // ok, now it works
        assertEquals("4.00 EUR", valueTogether.toString());

        LOGGER.info("Test Money successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
