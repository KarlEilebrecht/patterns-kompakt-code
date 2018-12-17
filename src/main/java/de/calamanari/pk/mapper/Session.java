//@formatter:off
/*
 * Session - supplementary class for MAPPER demonstration
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
package de.calamanari.pk.mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session - supplementary class for MAPPER demonstration<br>
 * According to Martin Fowler the mapper can't be invoked directly by either of the two components it is connected to, because they both don't even know of the
 * mapper.<br>
 * One solution is a third party <i>driving</i> the mapper. In this example a session is responsible for this task.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    /**
     * all mappers created during the session
     */
    private List<AbstractMapper> mappers = new ArrayList<>();

    /**
     * Creates new session
     */
    public Session() {
        LOGGER.debug("{} created");
    }

    /**
     * Adds a mapper to the session management
     * 
     * @param mapper session-managed mapper instance
     */
    public void add(AbstractMapper mapper) {
        LOGGER.debug("{}.add(...) called ", this.getClass().getSimpleName());
        LOGGER.debug("Triggering Mapper to map data forward");
        mapper.map();
        mappers.add(mapper);
    }

    /**
     * Write-back any changes and close session, instances obtained within this session are no longer valid
     */
    public void confirm() {
        LOGGER.debug("{}.confirm() called ", this.getClass().getSimpleName());
        flush();
        mappers.clear();
    }

    /**
     * Flush changes, instances obtained within this session remain valid
     */
    public void flush() {
        LOGGER.debug("{}.flush() called ", this.getClass().getSimpleName());
        for (AbstractMapper mapper : mappers) {
            LOGGER.debug("Triggering mapper to map data backwards");
            mapper.mapBack();
        }
    }

    /**
     * Discard any changes and close session, instances obtained within this session are no longer valid
     */
    public void discard() {
        LOGGER.debug("{}.clear() called ", this.getClass().getSimpleName());
        LOGGER.debug("Invalidating all manged mappers");
        mappers.clear();
    }

}
