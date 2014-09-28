//@formatter:off
/*
 * Person
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
package de.calamanari.pk.bridge;

/**
 * A person object in this scenario (supplementary).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Person {

    /**
     * id of this person
     */
    private String id;

    /**
     * first name of this person
     */
    private String firstName;

    /**
     * last name of this person
     */
    private String lastName;

    /**
     * role in the company
     */
    private String role;

    /**
     * Creates a new person instance with the given parameters.
     * 
     * @param id identifier
     * @param firstName person's first name
     * @param lastName person's name
     * @param role company role
     */
    public Person(String id, String firstName, String lastName, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    /**
     * Sets the id of this person entry
     * 
     * @param id identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the person's id
     * 
     * @return person id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the person's last name
     * 
     * @return last name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the person's first name
     * 
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the person's last name
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
     * returns the person's role
     * 
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the person's role
     * 
     * @param role company role
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("({");
        sb.append("id=" + id);
        sb.append(", firstName=" + firstName);
        sb.append(", lastName=" + lastName);
        sb.append(", role=" + role);
        sb.append("})");
        return sb.toString();
    }

}
