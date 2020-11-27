//@formatter:off
/*
 * Staff member is the LEAF implementing the component interface 
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

/**
 * Staff member is the LEAF implementing the component interface
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class StaffMember implements EnterpriseNode {

    /**
     * Person's first name
     */
    private String firstName = null;

    /**
     * Person's last name
     */
    private String lastName = null;

    /**
     * the unit this member works for
     */
    private AbstractEnterpriseUnit enterpriseUnit = null;

    /**
     * Job title of this staff member
     */
    private String jobTitle = null;

    /**
     * Creates new member using this given arguments
     * 
     * @param firstName first name
     * @param lastName name
     * @param jobTitle the job of the person
     */
    public StaffMember(String firstName, String lastName, String jobTitle) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }

    /**
     * Returns the first name
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the person's first name
     * 
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the person's last name.
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the person's last name
     * 
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the enterprise unit this person works for
     * 
     * @param enterpriseUnit unit the member works for
     */
    public void setEnterpriseUnit(AbstractEnterpriseUnit enterpriseUnit) {
        this.enterpriseUnit = enterpriseUnit;
    }

    /**
     * Returns the job title of this staff member
     * 
     * @return job title
     */
    public String getJobTitle() {
        return this.jobTitle;
    }

    /**
     * Sets this staff member's job title
     * 
     * @param jobTitle person's job
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Returns the enterprise unit this person works for.
     * 
     * @return enterprise unit
     */
    public AbstractEnterpriseUnit getEnterpriseUnit() {
        return this.enterpriseUnit;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public EnterpriseNode getParentNode() {
        return this.enterpriseUnit;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + "('" + this.getName() + "' - " + this.jobTitle + ")";
    }
}
