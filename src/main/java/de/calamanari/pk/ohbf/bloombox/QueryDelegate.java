//@formatter:off
/*
 * QueryDelegate
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
import java.util.List;

/**
 * A {@link QueryDelegate} decouples both the {@link BloomBoxQueryRunner} and the {@link BloomBoxDataStore} from the details of the query execution and the
 * state.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface QueryDelegate extends Serializable {

    /**
     * @return a flag per query that tells whether it is broken, this way a we can avoid executing an erratic query multiple times
     */
    public boolean[] getQueryInErrorFlags();

    /**
     * @return list of internal queries to be executed per record vector
     */
    public InternalQuery[] getQueries();

    /**
     * @return list of results (same orders as queries), results (counts) will be updated during execution
     */
    public List<BloomBoxQueryResult> getResults();

    /**
     * Called by the store to trigger the execution of all the queries on the current records long vector
     * 
     * @param vector source
     * @param startPos position where to start
     */
    public void execute(long[] vector, int startPos);

}
