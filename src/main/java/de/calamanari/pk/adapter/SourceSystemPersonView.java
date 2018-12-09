//@formatter:off
/*
 * Source system person view
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
package de.calamanari.pk.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Source system person view plays the role of an existing class. Its interface provides "the wrong" methods, thus it has to be adapted for being used in the
 * target scenario. <br>
 * Semantics as well as data structure is not sufficient.<br>
 * This is the ADAPTEE in ADAPTER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SourceSystemPersonView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourceSystemPersonView.class);

    /**
     * identifies this record
     */
    public final long number;

    /**
     * name of person
     */
    public final String firstName;

    /**
     * last name of person
     */
    public final String lastName;

    /**
     * detail information
     */
    public final String comment;

    /**
     * marks a record valid/invalid
     */
    private boolean invalid = false;

    /**
     * indicates that the underlying entity has been deleted
     */
    private boolean deleted = false;

    /**
     * Constructs new item, used in source system
     * 
     * @param number identifier
     * @param firstName person's first name
     * @param lastName person's last name
     * @param comment details
     */
    public SourceSystemPersonView(long number, String firstName, String lastName, String comment) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
    }

    /**
     * returns whether this record is invalid
     * 
     * @return true if record is invalid
     */
    public boolean isInvalid() {
        LOGGER.debug("isInvalid() called.");
        boolean res = invalid || deleted;
        LOGGER.debug("Instance is{} valid.", (res ? " in" : ""));
        return res;
    }

    /**
     * Sets this record invalid
     */
    public void setInvalid() {
        LOGGER.debug("setInvalid() called.");
        this.invalid = true;
    }

    /**
     * Sets this record valid
     */
    public void setValid() {
        LOGGER.debug("setValid() called.");
        this.invalid = false;
    }

    /**
     * deletes the person
     */
    public void delete() {
        LOGGER.debug("delete() called.");
        this.deleted = true;
    }
}
