//@formatter:off
/*
 * Team Model - the MODEL in this MVC-example.
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
package de.calamanari.pk.modelviewcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Team Model - the MODEL in this MVC-example holding the member data of a team.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class TeamModel {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(TeamModel.class.getName());

    /**
     * maximum number of members (usually this should be configurable)
     */
    private static final int MAX_NUMBER_OF_MEMBERS = 5;

    /**
     * maintains the current members
     */
    private List<String> members = new ArrayList<>();

    /**
     * observer of this model
     */
    private volatile TeamModelObserver observer = null;

    /**
     * Creates new empty model
     */
    public TeamModel() {
        LOGGER.fine(this.getClass().getSimpleName() + " created");
    }

    /**
     * inform listener about model update
     */
    private void fireModelChanged() {
        if (observer != null) {
            LOGGER.fine(this.getClass().getSimpleName() + ".fireModelChange!");
            observer.handleModelChanged();
        }
    }

    /**
     * Sets the model observer, one at a time
     * 
     * @param observer model observer (replaces existing)
     */
    public void setModelObserver(TeamModelObserver observer) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setModelObserver(...) called");
        this.observer = observer;
    }

    /**
     * Adds the member to the list
     * 
     * @param member the member to add
     */
    public void add(String member) {
        LOGGER.fine(this.getClass().getSimpleName() + ".add('" + member + "') called");
        if (member != null && member.trim().length() > 0 && !members.contains(member)) {
            if (members.size() > MAX_NUMBER_OF_MEMBERS) {
                throw new IndexOutOfBoundsException("Only 5 members allowed.");
            }
            members.add(member);
            fireModelChanged();
        }
        else {
            LOGGER.fine("Request ignored.");
        }
    }

    /**
     * Returns the member at the given position
     * 
     * @param idx member position, 0-based
     * @return member or null if the member does not exist
     */
    public String getMember(int idx) {
        LOGGER.fine(this.getClass().getSimpleName() + ".getMember(" + idx + ") called");
        if (idx < members.size()) {
            return members.get(idx);
        }
        else {
            LOGGER.fine("Request ignored.");
        }
        return null;
    }

    /**
     * Removes the specified member from the list
     * 
     * @param idx member position, 0-based
     */
    public void remove(int idx) {
        LOGGER.fine(this.getClass().getSimpleName() + ".remove(" + idx + ") called");
        members.remove(idx);
        fireModelChanged();
    }

    /**
     * Returns the model's size
     * 
     * @return size
     */
    public int size() {
        return members.size();
    }

    /**
     * Team Model Observer - allows to observe model changes.
     */
    public interface TeamModelObserver {

        /**
         * inform listener
         */
        public void handleModelChanged();
    }

}
