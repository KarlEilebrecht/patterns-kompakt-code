//@formatter:off
/*
 * UpScaler
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

import de.calamanari.pk.ohbf.bloombox.bbq.BbqExpression;

/**
 * Interface for upscaling POLICY implementations.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface UpScaler {

    /**
     * Computes and sets the scaled result counts in the given query result
     * 
     * @param query reference to lookup results
     * @param result to be modified
     * @param stats the instance earlier obtained from {@link #createNewStatsInstance()}
     */
    public void adjustResultCounts(InternalQuery query, BloomBoxQueryResult result, PreparationQueryStats stats);

    /**
     * Called by the runner to deliver the raw counts from the bloom box, this is just to give the concrete scaler the opportunity to post-process or cache the
     * stats.
     * <p>
     * The default implementation does nothing.
     * 
     * @param stats the instance earlier obtained from {@link #createNewStatsInstance()}
     */
    default void handleQueryBundleResults(PreparationQueryStats stats) {
        // no-op
    }

    /**
     * Called <i>per query in a bundle</i> by the runner after the given query's expressions have been prepared
     * <p>
     * The upscaler may decide to add further expressions to the map that should be queried by the runner.
     * <p>
     * The default implementation does nothing.
     * 
     * @param query currently prepared query
     * @param bundleExpressionMap maps expression-ids to expressions
     * @param stats object earlier obtained from {@link #createNewStatsInstance()}
     */
    default void handleQueryExpressionsPrepared(InternalQuery query, Map<Long, BbqExpression> bundleExpressionMap, PreparationQueryStats stats) {
        // no-op
    }

    /**
     * FACTORY method to obtain a fresh instance. The runner will call this once per bundle.
     * <p>
     * The concrete implementation may decide to return custom subclass of {@link PreparationQueryStats}
     * 
     * @return stats, by default a new instance of {@link PreparationQueryStats}
     */
    default PreparationQueryStats createNewStatsInstance() {
        return new PreparationQueryStats();
    }

}
