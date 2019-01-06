//@formatter:off
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
//@formatter:on
package de.calamanari.pk.nullobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete Host Name Data - a real host name data implementation
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ConcreteHostNameData implements HostNameData {

    private static final long serialVersionUID = -8616761413583272646L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcreteHostNameData.class);

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
     * 
     * @param purpose what this instance is for
     */
    public ConcreteHostNameData(String purpose) {
        this(purpose, null);
    }

    /**
     * Creates new instance from the given list and purpose
     * 
     * @param purpose data purpose
     * @param names collection of names, may be null
     */
    public ConcreteHostNameData(String purpose, Collection<String> names) {
        this.internalList = new ArrayList<>(names == null ? new ArrayList<String>() : names);
        this.purpose = purpose;
    }

    @Override
    public String getPurpose() {
        LOGGER.debug("{}.getPurpose() called.", ConcreteHostNameData.class.getSimpleName());
        return this.purpose;
    }

    @Override
    public int getNumberOfEntries() {
        LOGGER.debug("{}.getNumberOfEntries() called.", ConcreteHostNameData.class.getSimpleName());
        return internalList.size();
    }

    @Override
    public boolean contains(String name) {
        LOGGER.debug("{}.contains('{}') called.", ConcreteHostNameData.class.getSimpleName(), name);
        return internalList.contains(name);
    }

    @Override
    public int indexOf(String name) {
        LOGGER.debug("{}.indexOf('{}') called.", ConcreteHostNameData.class.getSimpleName(), name);
        return internalList.indexOf(name);
    }

    @Override
    public boolean containsAll(Collection<? extends String> coll) {
        LOGGER.debug("{}.containsAll({}) called.", ConcreteHostNameData.class.getSimpleName(), coll);
        return internalList.containsAll(coll);
    }

    @Override
    public String[] toArray() {
        LOGGER.debug("{}.toArray() called.", ConcreteHostNameData.class.getSimpleName());
        return internalList.toArray(new String[internalList.size()]);
    }

    @Override
    public String get(int index) {
        LOGGER.debug("{}.get({}) called.", ConcreteHostNameData.class.getSimpleName(), index);
        return internalList.get(index);
    }

    @Override
    public boolean addHostName(String e) {
        LOGGER.debug("{}.addHostName('{}') called.", ConcreteHostNameData.class.getSimpleName(), e);
        return internalList.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        LOGGER.debug("{}.iterator() called.", ConcreteHostNameData.class.getSimpleName());
        return Collections.unmodifiableList(internalList).iterator();
    }

    @Override
    @SuppressWarnings({ "squid:S1182", "squid:S2975" })
    public Object clone() {
        LOGGER.debug("{}.clone() called.", ConcreteHostNameData.class.getSimpleName());
        @SuppressWarnings("unchecked")
        ArrayList<String> clonedInternalList = (ArrayList<String>) internalList.clone();
        return new ConcreteHostNameData(purpose, clonedInternalList);
    }

}
