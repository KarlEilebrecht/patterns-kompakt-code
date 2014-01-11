/*
 * Concrete Host Name Data - a real host name list
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
package de.calamanari.pk.nullobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Concrete Host Name Data - a real host name data implementation
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ConcreteHostNameData implements HostNameData {

    /**
     * for serialization
     */
    private static final long serialVersionUID = -8616761413583272646L;

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ConcreteHostNameData.class.getName());

    /**
     * here is the data (no need to implement my own list :-) ) BTW: This is sometimes called a DELEGATE
     */
    private final ArrayList<String> internalList;

    /**
     * purpose of instance
     */
    private final String purpose;

    /**
     * Creates new data object
     * @param purpose what this instance is for
     */
    public ConcreteHostNameData(String purpose) {
        this(purpose, null);
    }

    /**
     * Creates new instance from the given list and purpose
     * @param purpose data purpose
     * @param names collection of names, may be null
     */
    public ConcreteHostNameData(String purpose, Collection<String> names) {
        this.internalList = new ArrayList<>(names == null ? new ArrayList<String>() : names);
        this.purpose = purpose;
    }

    @Override
    public String getPurpose() {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".getPurpose() called.");
        return this.purpose;
    }

    @Override
    public int getNumberOfEntries() {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".getNumberOfEntries() called.");
        return internalList.size();
    }

    @Override
    public boolean contains(String name) {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".contains('" + name + "') called.");
        return internalList.contains(name);
    }

    @Override
    public int indexOf(String name) {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".indexOf('" + name + "') called.");
        return internalList.indexOf(name);
    }

    @Override
    public boolean containsAll(Collection<? extends String> coll) {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".containsAll(" + coll + ") called.");
        return internalList.containsAll(coll);
    }

    @Override
    public String[] toArray() {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".toArray() called.");
        return internalList.toArray(new String[internalList.size()]);
    }

    @Override
    public String get(int index) {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".get(" + index + ") called.");
        return internalList.get(index);
    }

    @Override
    public boolean addHostName(String e) {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".addHostName('" + e + "') called.");
        return internalList.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".iterator() called.");
        return Collections.unmodifiableList(internalList).iterator();
    }

    @Override
    public Object clone() {
        LOGGER.fine(ConcreteHostNameData.class.getSimpleName() + ".clone() called.");
        ArrayList<String> clonedInternalList = (ArrayList<String>) internalList.clone();
        return new ConcreteHostNameData(purpose, clonedInternalList);
    }

}
