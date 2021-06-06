//@formatter:off
/*
 * PbDataPointOccurrenceAware
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

package de.calamanari.pk.ohbf.bloombox;

/**
 * Elements implementing this interface are data point dictionary aware and they know about data point occurrences, they can report for collection.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface PbDataPointOccurrenceAware extends PbDataPointDictionaryAware {

    /**
     * Called to tell the element to inform the collector about data point usage.
     * 
     * @param collector destination for collecting data point usage
     */
    public void registerDataPointOccurrences(PbDataPointOccurrenceCollector collector);
}
