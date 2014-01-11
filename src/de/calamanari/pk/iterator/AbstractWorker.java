/*
 * Abstract Worker
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
package de.calamanari.pk.iterator;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Abstract Worker (a COMPOSITE-interface) represents the AGGREGATE (abstract base class), an iterator will be created
 * for.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractWorker {

    /**
     * Default comparator, only compares the names for bringing workers into a natural order.
     */
    public static final Comparator<AbstractWorker> BY_NAME_COMPARATOR = new Comparator<AbstractWorker>() {
        
        @Override
        public int compare(AbstractWorker o1, AbstractWorker o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    /**
     * Name of worker
     */
    private String name = null;

    /**
     * Creates a new Worker with the given name
     * @param name person's name
     */
    public AbstractWorker(String name) {
        this.setName(name);
    }

    /**
     * Returns the worker's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the worker's name
     * @param name person's name
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Argument 'name' must not be null.");
        }
        this.name = name;
    }

    /**
     * A concrete Worker return an iterator of all his direct and indirect subordinates (recursively)
     * @return iterator of subordinates, NEVER NULL
     */
    public abstract Iterator<? extends AbstractWorker> createSubordinatesIterator();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.getName() + ")";
    }
}
