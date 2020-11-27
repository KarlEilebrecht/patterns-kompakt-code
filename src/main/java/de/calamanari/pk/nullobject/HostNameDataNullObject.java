//@formatter:off
/*
 * Host Name Data Null Object - NULL OBJECT implementation for HostNameData
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Host Name Data Null Object - NULL OBJECT implementation for HostNameList
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class HostNameDataNullObject implements HostNameData {

    private static final long serialVersionUID = 7701315306173959234L;

    private static final Logger LOGGER = LoggerFactory.getLogger(HostNameDataNullObject.class);

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
    private static final Iterator<String> STRING_ITERATOR_NULL_OBJECT = Arrays.asList(STRING_ARRAY_NULL_OBJECT).iterator();

    /**
     * A NULL OBJECT is a VALUE OBJECT and can be implemented as a SINGLETON<br>
     * Constructor is private to prevent instantiation
     */
    private HostNameDataNullObject() {

    }

    @Override
    public Iterator<String> iterator() {
        LOGGER.debug("{}.iterator() called.", HostNameDataNullObject.class.getSimpleName());
        return STRING_ITERATOR_NULL_OBJECT;
    }

    @Override
    public int getNumberOfEntries() {
        LOGGER.debug("{}.getNumberOfEntries() called.", HostNameDataNullObject.class.getSimpleName());
        return 0;
    }

    @Override
    public String getPurpose() {
        LOGGER.debug("{}.getPurpose() called.", HostNameDataNullObject.class.getSimpleName());
        return "<NONE>";
    }

    @Override
    public boolean contains(String name) {
        LOGGER.debug("{}.contains('{}') called.", HostNameDataNullObject.class.getSimpleName(), name);
        return false;
    }

    @Override
    public int indexOf(String name) {
        LOGGER.debug("{}.indexOf('{}') called.", HostNameDataNullObject.class.getSimpleName(), name);
        return -1;
    }

    @Override
    public boolean containsAll(Collection<? extends String> names) {
        LOGGER.debug("{}.containsAll({}) called.", HostNameDataNullObject.class.getSimpleName(), names);
        return false;
    }

    @Override
    public String[] toArray() {
        LOGGER.debug("{}.toArray() called.", HostNameDataNullObject.class.getSimpleName());
        return STRING_ARRAY_NULL_OBJECT;
    }

    @Override
    public String get(int index) {
        LOGGER.debug("{}.get({}) called.", HostNameDataNullObject.class.getSimpleName(), index);
        return null;
    }

    @Override
    public boolean addHostName(String name) {
        LOGGER.debug("{}.addHostName('{}') called.", HostNameDataNullObject.class.getSimpleName(), name);
        return false;
    }

    @Override
    public <T extends HostNameData> T copy() {
        LOGGER.debug("{}.copy() called.", HostNameDataNullObject.class.getSimpleName());
        LOGGER.debug("Returning the singleton instance of NULL OBJECT, no duplicate!");
        @SuppressWarnings("unchecked")
        T res = (T) HostNameDataNullObject.INSTANCE;
        return res;
    }

    /**
     * Do not create new instance during de-serialization
     * 
     * @return {@link #INSTANCE}
     */
    public Object readResolve() {
        LOGGER.debug("{}.readResolve() during de-serialization called.", HostNameDataNullObject.class.getSimpleName());
        LOGGER.debug("Returning the singleton instance of NULL OBJECT, no duplicate!");
        return HostNameDataNullObject.INSTANCE;
    }

}
