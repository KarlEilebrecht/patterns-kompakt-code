//@formatter:off
/*
 * Visitor Test - demonstrates VISITOR pattern.
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
package de.calamanari.pk.visitor;

import static org.junit.Assert.assertEquals;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Visitor Test - demonstrates VISITOR pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class VisitorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorTest.class);

    @Test
    public void testVisitor() {

        // HINT: Adjust the log-level in lockback.xml to DEBUG to see the VISITOR working

        LOGGER.info("Test Visitor ...");
        long startTimeNanos = System.nanoTime();

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        CustomerHolding holding1 = new CustomerHolding("Conglomaxx", 0.5);
        CustomerCompany company1 = new CustomerCompany(holding1, "Gorgonzola Ltd.", 0.5);
        EffectiveDiscountVisitor visitor1 = new EffectiveDiscountVisitor();
        company1.accept(visitor1);
        assertEquals("1.00", nf.format(visitor1.getEffectiveDiscountPerc()));

        CustomerDivision division1 = new CustomerDivision(company1, "Cheese Office", 3);
        EffectiveDiscountVisitor visitor2 = new EffectiveDiscountVisitor();
        division1.accept(visitor2);
        assertEquals("3.50", nf.format(visitor2.getEffectiveDiscountPerc()));

        CustomerOrder order1 = new CustomerOrder("Max Power", null, 0);
        OrderItem.createOrderItem(order1, "Pencil", 10, 0.30, 10.0);
        OrderItem.createOrderItem(order1, "Paper 500", 20, 2.50, 25.0);
        OrderItem.createOrderItem(order1, "Power Tape 2m", 5, 6.80, 0.0);

        EffectiveDiscountVisitor visitor3 = new EffectiveDiscountVisitor();
        order1.accept(visitor3);
        assertEquals("14.71", nf.format(visitor3.getEffectiveDiscountPerc()));

        order1.setDivision(division1);
        EffectiveDiscountVisitor visitor4 = new EffectiveDiscountVisitor();
        order1.accept(visitor4);
        assertEquals("18.21", nf.format(visitor4.getEffectiveDiscountPerc()));

        order1.setSpecialDiscountPerc(1.5);
        EffectiveDiscountVisitor visitor5 = new EffectiveDiscountVisitor();
        order1.accept(visitor5);
        assertEquals("15.21", nf.format(visitor5.getEffectiveDiscountPerc()));

        order1.setSpecialDiscountPerc(5.0);
        OrderItem.createOrderItem(order1, "Extension Cord 10m", 25, 7.90, 0.0);
        EffectiveDiscountVisitor visitor6 = new EffectiveDiscountVisitor();
        order1.accept(visitor6);
        assertEquals("5.50", nf.format(visitor6.getEffectiveDiscountPerc()));

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Visitor successful! Elapsed time: {} s", elapsedTimeString);

    }

}
