/*
 * Customer Entity - one of the business entities in this example.
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
package de.calamanari.pk.transferobjectassembler;

import java.util.logging.Logger;

/**
 * Customer Entity - one of the business entities in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerEntity {
    
    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CustomerEntity.class.getName());

    /**
     * id of customer
     */
    private String customerId = null;

    /**
     * title field
     */
    private String title = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * phone number
     */
    private String phone = null;

    /**
     * email address
     */
    private String email = null;

    /**
     * flag to indicate allowence for promotion activity
     */
    private boolean promotionOptIn = false;

    // typically more attributes

    /**
     * Creates new customer entity
     */
    public CustomerEntity() {

    }

    /**
     * Creates new entity from the given data
     * @param customerId identifier
     * @param title person's title
     * @param lastName person's last name
     * @param firstName person's first name
     * @param phone telephone number
     * @param email email-address
     * @param promotionOptIn opt-in-flag for promotion events
     */
    public CustomerEntity(String customerId, String title, String lastName, String firstName, String phone,
            String email, boolean promotionOptIn) {
        this.customerId = customerId;
        this.title = title;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.promotionOptIn = promotionOptIn;
    }

    /**
     * Returns the customerId
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customerId
     * @param customerId identifier
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns customer title
     * @return title of customer
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the customer's title
     * @param title person's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the last name of customer
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the customer's last name
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the customer's first name
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the customer's first name
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the customer's phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer's phone number
     * @param phone telephone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the customer's email address
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email address
     * @param email email-address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns whether the customer has agreed to receive advertisement media
     * @return true if customer has agreed, otherwise (default) false
     */
    public boolean isPromotionOptIn() {
        return promotionOptIn;
    }

    /**
     * Sets the customer's promotion status, whether to participate in sales promotion actions or not
     * @param promotionOptIn true means the customer has agreed to receive advertisement media
     */
    public void setPromotionOptIn(boolean promotionOptIn) {
        this.promotionOptIn = promotionOptIn;
    }

    /**
     * Returns a data transfer object corresponding to this entity
     * @return dto
     */
    public CustomerDto toDto() {
        LOGGER.fine(this.getClass().getSimpleName() + ".toDto() called");
        return new CustomerDto(customerId, title, lastName, firstName, phone, email, promotionOptIn);
    }

    /**
     * Updates this entity from the given data transfer object
     * @param dto data transfer object to copy data from into this object
     */
    public void fromDto(CustomerDto dto) {
        this.customerId = dto.getCustomerId();
        this.title = dto.getTitle();
        this.lastName = dto.getLastName();
        this.firstName = dto.getFirstName();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.promotionOptIn = dto.isPromotionOptIn();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", title=" + title + ", lastName="
                + lastName + ", firstName=" + firstName + ", phone=" + phone + ", email=" + email + ", promotionOptIn="
                + promotionOptIn + "})";
    }

}
