/*
 * Person View Adapter
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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

import java.util.logging.Logger;

/**
 * Person view adapter implements the target system person view interface and acts as an ADAPTER for
 * SourceSystemPersonView items.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class PersonViewAdapter implements TargetSystemPersonView {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(PersonViewAdapter.class.getName());

    /**
     * The adaptee
     */
    private final SourceSystemPersonView sourceSystemPersonView;

    /**
     * Constructs a new adapter for the given source system person view.
     * @param sourceSystemPersonView adaptee
     */
    public PersonViewAdapter(SourceSystemPersonView sourceSystemPersonView) {
        if (sourceSystemPersonView == null) {
            throw new IllegalArgumentException("Argument 'sourceSystemPersonView' must not be null.");
        }
        this.sourceSystemPersonView = sourceSystemPersonView;
    }

    @Override
    public String getId() {
        LOGGER.fine("Id requested - returning stringified number from adaptee.");
        return "" + sourceSystemPersonView.number;
    }

    @Override
    public String getName() {
        LOGGER.fine("Name requested - returning first name and last name from adaptee.");
        return sourceSystemPersonView.firstName + " " + sourceSystemPersonView.lastName;
    }

    @Override
    public String getDescription() {
        LOGGER.fine("Description requested - returning comment from adaptee.");
        return sourceSystemPersonView.comment;
    }

    @Override
    public boolean isValid() {
        LOGGER.fine("Validity state requested - returning inverted invalid-state from adaptee.");
        return !sourceSystemPersonView.isInvalid();
    }

    @Override
    public boolean setValid(boolean valid) {
        LOGGER.fine("setValid(" + valid + ") called ...");
        if (valid) {
            sourceSystemPersonView.setValid();
        }
        else {
            sourceSystemPersonView.setInvalid();
        }
        // now check whether the operation succeeded
        boolean success = (sourceSystemPersonView.isInvalid() != valid);
        LOGGER.fine("Checking operation success ...");
        LOGGER.fine("setValid(" + valid + ") was" + (success ? "" : " not") + " successful.");
        return success;
    }

    @Override
    public void remove() {
        LOGGER.fine("remove() called.");
        sourceSystemPersonView.delete();
    }

}
