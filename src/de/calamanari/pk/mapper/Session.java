/*
 * Session - supplementary class for MAPPER demonstration
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Session - supplementary class for MAPPER demonstration<br>
 * According to Martin Fowler the mapper can't be invoked directly by either of the two components it is connected to,
 * because they both don't even know of the mapper.<br>
 * One solution is a third party <i>driving</i> the mapper. In this example a session is responsible for this task.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Session {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Session.class.getName());

    /**
     * all mappers created during the session
     */
    private List<AbstractMapper> mappers = new ArrayList<>();

    /**
     * Creates new session
     */
    public Session() {
        LOGGER.fine(this.getClass().getSimpleName() + " created");
    }

    /**
     * Adds a mapper to the session management
     * @param mapper session-managed mapper instance
     */
    public void add(AbstractMapper mapper) {
        LOGGER.fine(this.getClass().getSimpleName() + ".add(...) called ");
        LOGGER.fine("Triggering Mapper to map data forward");
        mapper.map();
        mappers.add(mapper);
    }

    /**
     * Write-back any changes and close session, instances obtained within this session are no longer valid
     */
    public void confirm() {
        LOGGER.fine(this.getClass().getSimpleName() + ".confirm() called ");
        flush();
        mappers.clear();
    }

    /**
     * Flush changes, instances obtained within this session remain valid
     */
    public void flush() {
        LOGGER.fine(this.getClass().getSimpleName() + ".flush() called ");
        for (AbstractMapper mapper : mappers) {
            LOGGER.fine("Triggering mapper to map data backwards");
            mapper.mapBack();
        }
    }

    /**
     * Discard any changes and close session, instances obtained within this session are no longer valid
     */
    public void discard() {
        LOGGER.fine(this.getClass().getSimpleName() + ".clear() called ");
        LOGGER.fine("Invalidating all manged mappers");
        mappers.clear();
    }

    @Override
    protected void finalize() throws Throwable {
        discard();
        super.finalize();
    }

}
