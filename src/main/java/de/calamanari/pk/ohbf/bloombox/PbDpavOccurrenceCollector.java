//@formatter:off
/*
 * PbDpavOccurrenceCollector
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

import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.bbq.BloomFilterQuery;

/**
 * A {@link PbDpavOccurrenceCollector} is used while scanning a complex nested expression to collect usages of DPAVs. This allows us to detect repeated usage of
 * the same DPAV-probabilities, see also comments in {@link PbDpavProbabilityManager}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface PbDpavOccurrenceCollector {

    /**
     * Registers a DPAV occurrence
     * 
     * @param expressionId the expression using this DPAV
     * @param lpDpavId key/value identifier
     */
    public void addDpavOccurrence(long expressionId, int lpDpavId);

    /**
     * Registers the given query and returns whether DPAVs should be collected to avoid collecting multiple times
     * 
     * @param query to start data point collection
     * @param queryName the name of the query we want to collect data points for
     * @return true if the query is not already registered
     */
    public boolean startCollection(BloomFilterQuery query, String queryName);

    /**
     * @return maps multi DPAV references to the query name, the value is the number of multi-refs, a value of 0 means no multi-DPAV-references
     */
    public Map<String, Integer> getMaxOccurrenceMap();

}
