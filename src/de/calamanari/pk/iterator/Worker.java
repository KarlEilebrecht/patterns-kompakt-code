/*
 * Worker
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
package de.calamanari.pk.iterator;

import java.util.HashSet;
import java.util.Set;

/**
 * Worker represents the concrete AGGREGATE which can return an ITERATOR.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Worker extends AbstractWorker implements Comparable<Worker> {

    /**
     * Subordinates of this worker
     */
    protected Set<Worker> subordinates = new HashSet<>();

    /**
     * Creates a worker of the given name
     * @param name person's name
     */
    public Worker(String name) {
        super(name);
    }

    /**
     * Adds a subordinate to this worker
     * @param subordinate person who works for this worker
     */
    public void addDirectSubordinate(Worker subordinate) {
        this.subordinates.add(subordinate);
    }

    /**
     * Removes the given subordinate
     * @param subordinate to be removed
     * @return true if the given worker was a subordinate before, otherwise false
     */
    public boolean removeDirectSubordinate(AbstractWorker subordinate) {
        return this.subordinates.remove(subordinate);
    }

    @Override
    public WorkerIterator createSubordinatesIterator() {
        return new WorkerIterator(this.subordinates);
    }

    /**
     * This method only compares the names for bringing workers into a natural order.<br>
     * Two different workers with the same name assigned will return 0 while {@link #equals(Object)} will return false.
     * {@inheritDoc} 
     */
    @Override
    public int compareTo(Worker o) {
        return this.getName().compareTo(o.getName());
    }

}
