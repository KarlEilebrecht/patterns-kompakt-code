//@formatter:off
/*
 * DpavProbabilityFetcher
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

import de.calamanari.pk.ohbf.bloombox.bbq.BinaryMatchExpression;

/**
 * The {@link DpavProbabilityFetcher} (Data Point Attribute Value Probability Fetcher) allows determining the probability of a DPAV as late as possible.
 * <p>
 * <b>Note:</b> This is by intention not a lambda because lambdas are not serializable and cannot cache data.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface DpavProbabilityFetcher extends Serializable {

    /**
     * Returns the probability for the given DPAV.
     * 
     * @param rootExpressionId id of the expression we are currently fetching data for
     * @param lpDpavId see {@link BinaryMatchExpression#getLpDpavId()}
     * @return probability or 0.0d if the given low-precision DPAV-id has no probability attached, <b><code>0 &lt;= value &lt;= 1.0</code></b>
     */
    double fetchDpavProbability(long rootExpressionId, int lpDpavId);

}
