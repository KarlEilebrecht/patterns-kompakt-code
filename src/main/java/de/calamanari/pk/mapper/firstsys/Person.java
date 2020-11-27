//@formatter:off
/*
 * Person - entity in the first subsystem
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
package de.calamanari.pk.mapper.firstsys;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Person - entity in the first subsystem
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Person {

    /**
     * id of person
     */
    private String personId = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * Date of the first order placed by this person
     */
    private Date firstOrderDate = null;

    /**
     * Creates new person entity
     * 
     * @param personId identifier
     * @param firstName person's first name
     * @param lastName person's last name
     * @param firstOrderDateISO (yyyy-MM-dd)
     */
    public Person(String personId, String firstName, String lastName, String firstOrderDateISO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.personId = personId;
        this.lastName = lastName;
        this.firstName = firstName;
        try {
            this.firstOrderDate = firstOrderDateISO == null ? null : sdf.parse(firstOrderDateISO);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Unable to parse firstOrderDateISO='" + firstOrderDateISO + "' as date (expected: yyyy-MM-dd).");
        }
    }

    /**
     * Returns id of person
     * 
     * @return personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Returns the person's last name
     * 
     * @return last name of person
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of person
     * 
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the person
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person
     * 
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the date of the first order placed by this person
     * 
     * @return firstOrderDate
     */
    public Date getFirstOrderDate() {
        return firstOrderDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return this.getClass().getSimpleName() + "({personId=" + personId + ", lastName=" + lastName + ", firstName=" + firstName + ", firstOrderDate="
                + (firstOrderDate == null ? null : "'" + sdf.format(firstOrderDate) + "'") + "})";
    }

}
