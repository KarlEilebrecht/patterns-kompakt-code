/*
 * Composite Test - demonstrates COMPOSITE pattern.
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
package de.calamanari.pk.composite.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.composite.AbstractEnterpriseUnit;
import de.calamanari.pk.composite.Company;
import de.calamanari.pk.composite.Division;
import de.calamanari.pk.composite.EnterpriseNode;
import de.calamanari.pk.composite.Holding;
import de.calamanari.pk.composite.StaffMember;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Composite Test - demonstrates COMPOSITE pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class CompositeTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CompositeTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * some different nodes
     */
    private ArrayList<EnterpriseNode> testNodes = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, CompositeTest.class, AbstractEnterpriseUnit.class, Holding.class,
                Company.class, Division.class, StaffMember.class);
    }

    @Before
    public void setUp() throws Exception {
        testNodes.clear();

        StaffMember staffMember = new StaffMember("Jack", "Miller", "General Manager");
        StaffMember staffMember2 = new StaffMember("Lucy", "Diamonds", "Controller");
        Division division = new Division("Secret Shopper Service");
        Holding holding = new Holding("MultiGlom Crap International");
        Company company = new Company("Crap IT-Systems");
        holding.addChildNode(company);
        company.setParent(holding);
        company.addChildNode(division);
        division.setParent(company);
        division.addChildNode(staffMember);
        staffMember.setEnterpriseUnit(division);
        testNodes.add(holding);
        testNodes.add(company);
        testNodes.add(division);
        testNodes.add(staffMember);
        testNodes.add(staffMember2);
    }

    @Test
    public void testComposite() {
        LOGGER.info("Test Composite ...");
        long startTimeNanos = System.nanoTime();

        for (EnterpriseNode enterpriseNode : testNodes) {

            // here we handle all nodes equally, we do not know their real nature
            LOGGER.info(enterpriseNode.getDescription());

            // however sometimes an explicit type-check becomes necessary:
            if (enterpriseNode instanceof StaffMember) {
                StaffMember member = (StaffMember) enterpriseNode;
                LOGGER.info(member.getFirstName() + " " + member.getLastName() + " is a staff member!");
                if (enterpriseNode.getParentNode() == null) {
                    LOGGER.info("No enterprise unit assigned for " + member.getFirstName() + " " + member.getLastName()
                            + "!");
                }
            }
            else if (enterpriseNode instanceof AbstractEnterpriseUnit) {
                AbstractEnterpriseUnit unit = (AbstractEnterpriseUnit) enterpriseNode;
                LOGGER.info(unit.getName() + " is an organizational unit!");
                assertEquals(1, unit.getChildNodes().size());
            }

        }
        LOGGER.info("Test Composite successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
