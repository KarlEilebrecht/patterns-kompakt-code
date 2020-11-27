//@formatter:off
/*
 * Host Name Data - interface in NULL OBJECT demonstration
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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

import java.io.Serializable;
import java.util.Collection;

/**
 * Host Name Data - the interface defines the methods, both concrete object and null object must provide<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface HostNameData extends Iterable<String>, Serializable {

    /**
     * Returns the number of entries in host name data
     * 
     * @return number of entries
     */
    public int getNumberOfEntries();

    /**
     * returns the purpose (whatever this might be :-) )
     * 
     * @return purpose of list
     */
    public String getPurpose();

    /**
     * Returns whether this list contains the name
     * 
     * @param name the host name to look for
     * @return true if found
     */
    public boolean contains(String name);

    /**
     * Returns the position of the given entry
     * 
     * @param name the host name to look for
     * @return true if found otherwise -1
     */
    public int indexOf(String name);

    /**
     * Returns whether the list contains all of the given names
     * 
     * @param names collection of names to look for
     * @return true if all names of the given list where found in this list
     */
    public boolean containsAll(Collection<? extends String> names);

    /**
     * Returns an array with all the names from the list
     * 
     * @return array of names
     */
    public String[] toArray();

    /**
     * Returns the name at the given position
     * 
     * @param index position
     * @return name at the given index or null if not found
     */
    public String get(int index);

    /**
     * Adds the name to the list
     * 
     * @param name host name
     * @return true if the name was added, false otherwise
     */
    public boolean addHostName(String name);

    /**
     * Creates a copy of this object, so that the internal state of the copy will be independent.<br>
     * This is not a deep clone as the values will not be copied but referenced.
     * @return copy of this object
     */
    public <T extends HostNameData> T copy();

}
