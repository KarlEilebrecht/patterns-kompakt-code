//@formatter:off
/*
 * Abstract Voucher
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
package de.calamanari.pk.factorymethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Voucher is the abstract product in this FACTORY METHOD example scenario.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractVoucher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVoucher.class);

    /**
     * The last name of the voucher's owner
     */
    private String lastName;

    /**
     * The first name of the voucher's owner
     */
    private String firstName;

    /**
     * The voucher's value
     */
    private double value;

    /**
     * Creates a new voucher
     * 
     * @param firstName person's first name
     * @param lastName person's last name
     * @param value the vouchers value
     */
    public AbstractVoucher(String firstName, String lastName, double value) {
        if (firstName == null || firstName.trim().length() == 0 || lastName == null || lastName.trim().length() == 0 || value <= 0) {
            throw new IllegalArgumentException("Arguments first name and last name " + "must be specified, value must be > 0 (given: firstName=" + firstName
                    + ", lastName=" + lastName + ", value=" + value + ").");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.value = value;
    }

    /**
     * Returns the last name of the voucher's owner
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the voucher's owner
     * 
     * @param lastName name of the owner
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the voucher's owner.
     * 
     * @return first name of the owner
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the voucher's owner
     * 
     * @param firstName first name of the owner
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the value of this voucher
     * 
     * @return value voucher's value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of this voucher
     * 
     * @param value vouchers value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns this voucher's ID
     * 
     * @return id of voucher
     */
    public abstract String getId();

    /**
     * Returns the human readable code of this voucher, the user will enter when applying it to an order.
     * 
     * @return display code
     */
    public abstract String getVoucherDisplayCode();

    @Override
    public String toString() {
        LOGGER.debug("toString() on {} called.", this.getClass().getSimpleName());
        return this.getClass().getSimpleName() + "({id=" + this.getId() + ", displayCode=" + this.getVoucherDisplayCode() + ", firstName=" + this.getFirstName()
                + ", lastName=" + this.getLastName() + ", value=" + this.getValue() + "})";
    }
}
