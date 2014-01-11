/*
 * Address
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
 * Address - an object in this scenario (supplementary).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Address {

    /**
     * id of this address
     */
    private String id;

    /**
     * id of the person this address belongs to
     */
    private String personId;

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
     * Creates new Address
     */
    public Address() {

    }

    /**
     * Creates a new Address using the given parameters
     * @param id address identifier
     * @param personId identifier of the related person
     * @param street address field
     * @param city address field
     * @param zipCode address field
     */
    public Address(String id, String personId, String street, String city, String zipCode) {
        this.id = id;
        this.personId = personId;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Sets the id of this address entity
     * @param id address id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns address id
     * @return address id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the related person id
     * @return person id
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Sets the person id (owner)
     * @param personId person identifier
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Returns the street
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the city
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
     * Returns the zip code
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code
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
        sb.append(", personId=" + personId);
        sb.append(", street=" + street);
        sb.append(", zipCode=" + zipCode);
        sb.append(", city=" + city);
        sb.append("})");
        return sb.toString();
    }

}
