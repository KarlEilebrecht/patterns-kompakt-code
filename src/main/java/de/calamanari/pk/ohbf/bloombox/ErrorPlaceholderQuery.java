//@formatter:off
/*
 * ErrorPlaceholderQuery
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

import java.util.Collections;
import java.util.Map;

/**
 * A query that does nothing else than setting a defined error on any attempt to execute it.
 * <p>
 * The error concept handling concept of the bloom box works with problem reporting on query level, so we do not just abort and throw an exception at the
 * caller. Instead we want to put the problem description close to the related query (while other queries in a bundle may still run successfully). This
 * placeholder supports this tolerant approach.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ErrorPlaceholderQuery extends InternalQuery {

    private static final long serialVersionUID = -8416303316557334670L;

    /**
     * planned error message
     */
    private final String errorMessage;

    /**
     * @return configured error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param name query name
     * @param errorMessage problem explanation
     */
    public ErrorPlaceholderQuery(String name, String errorMessage) {
        super(name, null, Collections.emptyMap(), null);
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(long[] source, int startPos, Map<Long, Boolean> resultCache, BloomBoxQueryResult result) {
        result.setErrorMessage(errorMessage);
    }

    @Override
    public void execute(long[] source, int startPos, DppFetcher probabilities, Map<Long, Boolean> resultCache, BloomBoxQueryResult result) {
        result.setErrorMessage(errorMessage);
    }

    @Override
    public void prepareLpDataPointIds(PbDataPointDictionary dictionary) {
        // no-op
    }

    @Override
    public void registerDataPointOccurrences(PbDataPointOccurrenceCollector collector) {
        // no-op
    }

}
