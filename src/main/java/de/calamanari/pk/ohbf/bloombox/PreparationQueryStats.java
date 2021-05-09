//@formatter:off
/*
 * PreparationQueryStats
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link PreparationQueryStats} bundle count query results uses as a basis for upscaling.
 * <p>
 * The upscaling concept of the {@link BloomBox} relies on counting sub-expression counts, treat this as a weighted tree and approximate the counts on higher
 * levels.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PreparationQueryStats implements Serializable {

    private static final long serialVersionUID = -3489636623742037051L;

    /**
     * Capacity of the store, the size of the 'sample' we query the raw counts from
     */
    private long numberOfRows = 0;

    /**
     * Maps expression-ids to the match counts
     */
    private Map<Long, Long> expressionLevelCounts = new HashMap<>();

    /**
     * expression-ids mapped to result containers for each user query
     */
    private Map<Long, BloomBoxQueryResult> mainQueryResultMap = new HashMap<>();

    /**
     * <code>parentAndExpressionId.ommittedChildExpressionId</code> mapped to expression-id of correction expression
     */
    private Map<String, Long> andCorrectionQueryMap = new HashMap<>();

    /**
     * @return expression-ids mapped to the match counts
     */
    public Map<Long, Long> getExpressionLevelCounts() {
        return expressionLevelCounts;
    }

    /**
     * @param expressionLevelCounts expression-ids mapped to the match counts
     */
    public void setExpressionLevelCounts(Map<Long, Long> expressionLevelCounts) {
        this.expressionLevelCounts = expressionLevelCounts;
    }

    /**
     * @return Capacity of the store, the size of the 'sample' we query the raw counts from
     */
    public long getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * @param numberOfRows Capacity of the store, the size of the 'sample' we query the raw counts from
     */
    public void setNumberOfRows(long numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * @return expression-ids mapped to result containers for each user query
     */
    public Map<Long, BloomBoxQueryResult> getMainQueryResultMap() {
        return mainQueryResultMap;
    }

    /**
     * @param mainQueryResultMap expression-ids mapped to result containers for each user query
     */
    public void setMainQueryResultMap(Map<Long, BloomBoxQueryResult> mainQueryResultMap) {
        this.mainQueryResultMap = mainQueryResultMap;
    }

    /**
     * @return <code>parentAndExpressionId.ommittedChildExpressionId</code> mapped to expression-id of correction expression
     */
    public Map<String, Long> getAndCorrectionQueryMap() {
        return andCorrectionQueryMap;
    }

    /**
     * @param andCorrectionQueryMap <code>parentAndExpressionId.ommittedChildExpressionId</code> mapped to expression-id of correction expression
     */
    public void setAndCorrectionQueryMap(Map<String, Long> andCorrectionQueryMap) {
        this.andCorrectionQueryMap = andCorrectionQueryMap;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [numberOfRows=" + numberOfRows + ", expressionLevelCounts=" + expressionLevelCounts + ", mainQueryResultMap="
                + mainQueryResultMap + ", andCorrectionQueryMap=" + andCorrectionQueryMap + "]";
    }

}
