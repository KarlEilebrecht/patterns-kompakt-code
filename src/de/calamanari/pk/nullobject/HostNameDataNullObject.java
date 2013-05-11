/*
 * Host Name Data Null Object - NULL OBJECT implementation for HostNameData
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
package de.calamanari.pk.nullobject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Host Name Data Null Object - NULL OBJECT implementation for HostNameList
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public final class HostNameDataNullObject implements HostNameData {

    /**
     * for serialization
     */
    private static final long serialVersionUID = 7701315306173959234L;

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(HostNameDataNullObject.class.getName());

    /**
     * The only instance of this NULL OBJECT
     */
    public static final HostNameDataNullObject INSTANCE = new HostNameDataNullObject();

    /**
     * an empty array - another NULL OBJECT
     */
    private static final String[] STRING_ARRAY_NULL_OBJECT = new String[0];

    /**
     * an empty iterator - another NULL OBJECT
     */
    private static final Iterator<String> STRING_ITERATOR_NULL_OBJECT = Arrays.asList(STRING_ARRAY_NULL_OBJECT)
            .iterator();

    /**
     * A NULL OBJECT is a VALUE OBJECT and can be implemented as a SINGLETON<br>
     * Constructor is private to prevent instantiation
     */
    private HostNameDataNullObject() {

    }

    @Override
    public Iterator<String> iterator() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".iterator() called.");
        return STRING_ITERATOR_NULL_OBJECT;
    }

    @Override
    public int getNumberOfEntries() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".getNumberOfEntries() called.");
        return 0;
    }

    @Override
    public String getPurpose() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".getPurpose() called.");
        return "<NONE>";
    }

    @Override
    public boolean contains(String name) {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".contains('" + name + "') called.");
        return false;
    }

    @Override
    public int indexOf(String name) {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".indexOf('" + name + "') called.");
        return -1;
    }

    @Override
    public boolean containsAll(Collection<? extends String> names) {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".containsAll(" + names + ") called.");
        return false;
    }

    @Override
    public String[] toArray() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".toArray() called.");
        return STRING_ARRAY_NULL_OBJECT;
    }

    @Override
    public String get(int index) {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".get(" + index + ") called.");
        return null;
    }

    @Override
    public boolean addHostName(String name) {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".addHostName('" + name + "') called.");
        return false;
    }

    @Override
    public Object clone() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".clone() called.");
        LOGGER.fine("Returning the singleton instance of NULL OBJECT, no duplicate!");
        return HostNameDataNullObject.INSTANCE;
    }

    /**
     * Do not create new instance during de-serialization
     * @return {@link #INSTANCE}
     */
    public Object readResolve() {
        LOGGER.fine(HostNameDataNullObject.class.getSimpleName() + ".readResolve() during de-serialization called.");
        LOGGER.fine("Returning the singleton instance of NULL OBJECT, no duplicate!");
        return HostNameDataNullObject.INSTANCE;
    }

}
