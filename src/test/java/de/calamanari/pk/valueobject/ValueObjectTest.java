//@formatter:off
/*
 * Value Object Test - demonstrates VALUE OBJECT pattern.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Value Object Test - demonstrates VALUE OBJECT pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ValueObjectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueObjectTest.class);

    @Test
    public void testValueObject() {

        // to demonstrate a value object we use fractions - you know from school :-)
        // here each fraction is immutable, calculating around with them causes
        // the creation of new fractions.

        LOGGER.info("Test Value Object ...");
        long startTimeNanos = System.nanoTime();

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(17);
        nf.setMinimumFractionDigits(17);

        Fraction fraction1 = new Fraction(4, 4);
        Fraction fraction2 = new Fraction(2, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Fraction fraction4 = new Fraction(25, 5);
        Fraction fraction5 = new Fraction(0, 1);

        assertEquals(new Fraction("(1/3)"), fraction3);

        assertEquals("(4/4)", fraction1.toString());

        // value objects typically provide an equals() method based on the current value
        // here we have the special case that the equals()-method uses the reduced fraction as its basis
        // making it possible to treat fractions equal() even if they don't look like
        assertEquals(fraction1, fraction2);

        assertEquals("(1/1)", fraction1.reduce().toString());
        assertEquals("(1/1)", fraction2.reduce().toString());

        assertEquals("(1/3)", fraction1.multiply(fraction3).toString());
        assertEquals("(5/3)", fraction3.multiply(fraction4).toString());

        assertEquals("(5/3)", (new Fraction("(-25/-15)")).reduce().toString());

        assertEquals("(1/3)", fraction3.multiply(fraction4).divide(fraction4).toString());

        assertEquals("(-14/3)", fraction3.subtract(fraction4).toString());

        assertEquals("(0/1)", fraction3.subtract(fraction3).toString());

        assertEquals(nf.format((double) 1 / 3), nf.format(fraction3.getDoubleValue()));
        assertEquals(nf.format((double) 1 / 3), nf.format((new Fraction("0.3333333333333333")).getDoubleValue()));

        assertEquals("(0/1)", (new Fraction("0")).toString());

        assertEquals("(1/5)", (new Fraction("0.2")).toString());

        assertEquals("(1/391)", (new Fraction("(1/17)")).divide(new Fraction(23)).toString());

        // immutable, we never modify
        assertNotSame(fraction3, fraction3.add(fraction1));

        // BUT BEWARE: if no modification takes place, we can/should return the SAME object instance
        assertSame(fraction3, fraction3.add(fraction5));

        // fractions are comparable by value ...
        Fraction[] fractions = new Fraction[] { fraction1, fraction2, fraction3, fraction4, fraction5 };
        assertEquals("[(4/4), (2/2), (1/3), (25/5), (0/1)]", Arrays.asList(fractions).toString());
        // ... but see the comment at the VIEW_COMPARATOR implementation
        Arrays.sort(fractions, Fraction.VIEW_COMPARATOR);
        assertEquals("[(0/1), (1/3), (2/2), (4/4), (25/5)]", Arrays.asList(fractions).toString());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Value Object successful! Elapsed time: {} s", elapsedTimeString);

    }

}
