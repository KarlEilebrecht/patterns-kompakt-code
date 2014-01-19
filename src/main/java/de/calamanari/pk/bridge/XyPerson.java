/*
 * XyPerson
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
package de.calamanari.pk.bridge;

/**
 * XyPerson - an object in this scenario (supplementary).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class XyPerson {

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
     * address id
     */
    private String addressId;

    /**
     * Street
     */
    private String street;

    /**
     * City
     */
    private String city;

    /**
     * Zip code
     */
    private String zipCode;

    /**
     * Creates new person with the given id
     * @param id person id
     */
    public XyPerson(String id) {
        this.id = id;
    }

    /**
     * Sets the id of this person
     * @param id identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns person id
     * @return id of person
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the first name of the person
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the person's first name
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the person's last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the person's last name
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the person's role
     * @return person's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Set's the person's role
     * @param role company role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the person's address id
     * @return id of address
     */
    public String getAddressId() {
        return addressId;
    }

    /**
     * Set's the person's address id
     * @param addressId identifier
     */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /**
     * Returns the street
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set's street info
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the city name
     * @return city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the zipcode
     * @return zipcode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zipcode
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
        sb.append(", addressId=" + addressId);
        sb.append(", street=" + street);
        sb.append(", city=" + city);
        sb.append(", zipCode=" + zipCode);
        sb.append("})");
        return sb.toString();
    }

}
