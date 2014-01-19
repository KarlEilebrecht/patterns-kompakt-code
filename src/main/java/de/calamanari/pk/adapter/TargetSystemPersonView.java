/*
 * Target system person view
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
package de.calamanari.pk.adapter;

/**
 * Target system person view is an interface in the target system, which has to be provided for integration.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface TargetSystemPersonView {

    /**
     * Returns the person's id
     * @return identifier
     */
    public String getId();

    /**
     * Returns the persons full name
     * @return name
     */
    public String getName();

    /**
     * Returns a description of this person's details
     * @return person description
     */
    public String getDescription();

    /**
     * Determines whether this record is valid.
     * @return true if record is valid, otherwise false
     */
    public boolean isValid();

    /**
     * Sets the validity state of this record and returns the whether the operation was successful.
     * @param valid new validity state
     * @return true if operation was successful, otherwise false
     */
    public boolean setValid(boolean valid);

    /**
     * Removes the underlying entity. After this operation the record is no longer valid.
     */
    public void remove();

}
