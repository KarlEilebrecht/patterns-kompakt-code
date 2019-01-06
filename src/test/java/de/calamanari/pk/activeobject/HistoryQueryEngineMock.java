//@formatter:off
/*
 * History Query Engine Mock - demonstrates ACTIVE OBJECT
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * History Query Engine Mock - in this example this is a component implementation for executing queries that may take some time.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class HistoryQueryEngineMock extends AbstractHistoryQueryEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryQueryEngineMock.class);

    /**
     * Simulated query duration in milliseconds
     */
    private final long queryDurationMillis;

    /**
     * The simulated "back end" datastore
     */
    private final Map<String, List<String[]>> archive = new ConcurrentHashMap<>();

    /**
     * In this example the history data fill be fed at startup.
     * 
     * @param seedData list of String[4]=[firstName, lastName, birthday, data]
     * @param queryDurationMillis simulated time a query to this engine shall need
     */
    public HistoryQueryEngineMock(List<String[]> seedData, long queryDurationMillis) {
        this.queryDurationMillis = queryDurationMillis;
        for (String[] data : seedData) {
            put(data[0] + "##" + data[1] + "##" + data[2], data);
            put(data[1] + "##" + data[2], data);
            put(data[0] + "##" + data[2], data);
            put(data[2], data);
        }
    }

    /**
     * helper method for testing, adds the "back end" data related to the key
     * 
     * @param key identifier
     * @param data values to put
     */
    private void put(String key, String[] data) {
        List<String[]> list = archive.get(key);
        if (list == null) {
            list = new ArrayList<>();
            archive.put(key, list);
        }
        list.add(data);
    }

    @Override
    public List<String[]> queryHistoryData(String firstName, String lastName, String birthday) {
        LOGGER.debug("{}.queryHistoryData('{}', '{}', '{}') called.", this.getClass().getSimpleName(), firstName, lastName, birthday);
        StringBuilder sbKey = new StringBuilder();
        if (firstName != null) {
            sbKey.append(firstName);
            sbKey.append("##");
        }
        if (lastName != null) {
            sbKey.append(lastName);
            sbKey.append("##");
        }
        sbKey.append(birthday);
        List<String[]> result = archive.get(sbKey.toString());
        if (result == null) {
            result = new ArrayList<>();
        }
        MiscUtils.sleepThrowRuntimeException(queryDurationMillis);
        return result;
    }

}
