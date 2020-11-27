//@formatter:off
/*
 * Indexer Partition Meta Data
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
package de.calamanari.pk.util.itfa;

/**
 * Container for settings related to a single partition to be indexed
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerPartitionMetaData {

    /**
     * size of the current (main) partition
     */
    int partitionSize;

    /**
     * size of the current (main) partition in bytes
     */
    long partitionSizeInBytes;

    /**
     * number of slaves to be used for processing the main partition
     */
    int numberOfSlaves;

    /**
     * default sub-partition size
     */
    int defaultSubPartitionSize;

    /**
     * maximum number of character index entries to be created for a sub-partition
     */
    int maxNumberOfCharEntriesInSubPartition;

    /**
     * maximum number of line index entries to be created for a sub-partition
     */
    int maxNumberOfLineEntriesInSubPartition;

}
