//@formatter:off
/*
 * Abstract History Query Engine - demonstrates ACTIVE OBJECT
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
//@formatter:on
package de.calamanari.pk.activeobject;

import java.util.List;

/**
 * Abstract History Query Engine - some engine that performs synchronous queries which take some time. A limitation (provider contract) shall be that only a
 * maximum number of parallel requests are allowed to reduce load on the assumed backend system.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractHistoryQueryEngine {

    /**
     * Perform a synchronous query, which may take some time.
     * 
     * @param firstName person's first name (optional)
     * @param lastName person's last name (optional)
     * @param birthday person's birthday (mandatory)
     * @return data list of (String[4]=[firstName, lastName, birthday, data]) according to the query or empty list if not found
     */
    public abstract List<String[]> queryHistoryData(String firstName, String lastName, String birthday);

}
