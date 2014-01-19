/*
 * Worker Iterator
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Worker Iterator - the ITERATOR implementation in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class WorkerIterator implements Iterator<Worker> {

    /**
     * items to be returned in order
     */
    private List<Worker> allWorkers = new ArrayList<>();

    /**
     * Index of next item to be returned
     */
    private int position = 0;

    /**
     * Creates new Iterator returning all workers and their subordinates (recursively) alphabetically ordered.
     * @param workers (root elements)
     */
    public WorkerIterator(Set<Worker> workers) {
        for (Worker worker : workers) {
            if (!allWorkers.contains(worker)) {
                allWorkers.add(worker);
                findSubordinatesRecursively(worker);
            }
        }
        Collections.sort(allWorkers, AbstractWorker.BY_NAME_COMPARATOR);
    }

    /**
     * Supplementary method to collect the workers from the tree.<br>
     * All subordinates will be added to the list allWorkers.
     * @param worker root
     */
    private void findSubordinatesRecursively(Worker worker) {
        for (Worker subordinate : worker.subordinates) {
            if (!allWorkers.contains(subordinate)) {
                allWorkers.add(subordinate);
                findSubordinatesRecursively(subordinate);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return (position < allWorkers.size());
    }

    @Override
    public Worker next() {
        Worker res = allWorkers.get(position);
        position++;
        return res;
    }

    @Override
    public void remove() {
        position--;
        allWorkers.remove(position);
    }

}
