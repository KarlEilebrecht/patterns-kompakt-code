//@formatter:off
/*
 * Composite Test - demonstrates COMPOSITE pattern.
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
package de.calamanari.pk.composite;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Composite Test - demonstrates COMPOSITE pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CompositeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeTest.class);

    /**
     * some different nodes
     */
    private ArrayList<EnterpriseNode> testNodes = new ArrayList<>();

    @Before
    public void setUp() {
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

        // hint: set the log-level in logback.xml to DEBUG to watch COMPOSITE working.

        LOGGER.info("Test Composite ...");
        long startTimeNanos = System.nanoTime();

        for (EnterpriseNode enterpriseNode : testNodes) {

            // here we handle all nodes equally, we do not know their real nature
            LOGGER.debug(enterpriseNode.getDescription());

            // however sometimes an explicit type-check becomes necessary:
            if (enterpriseNode instanceof StaffMember) {
                StaffMember member = (StaffMember) enterpriseNode;
                LOGGER.debug("{} {} is a staff member!", member.getFirstName(), member.getLastName());
                if (enterpriseNode.getParentNode() == null) {
                    LOGGER.debug("No enterprise unit assigned to {} {}!", member.getFirstName(), member.getLastName());
                }
            }
            else if (enterpriseNode instanceof AbstractEnterpriseUnit) {
                AbstractEnterpriseUnit unit = (AbstractEnterpriseUnit) enterpriseNode;
                LOGGER.debug("{} is an organizational unit!", unit.getName());
                assertEquals(1, unit.getChildNodes().size());
            }

        }
        LOGGER.info("Test Composite successful! Elapsed time: {} s", TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos));
    }

}
