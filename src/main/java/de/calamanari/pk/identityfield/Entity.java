//@formatter:off
/*
 * Entity - compound key carrying entity in IDENTITY FIELD pattern
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
package de.calamanari.pk.identityfield;

import java.util.logging.Logger;

/**
 * Entity - compound key carrying entity in IDENTITY FIELD pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Entity {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(Entity.class.getName());

    /**
     * The IDENTITY FIELD
     */
    private final CompoundKey id;

    /**
     * some entity field
     */
    private String x = null;

    /**
     * some entity field
     */
    private String y = null;

    /**
     * Creates new entity
     * 
     * @param id the compound key
     * @param x value
     * @param y value
     */
    public Entity(CompoundKey id, String x, String y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-field value
     * 
     * @return x
     */
    public String getX() {
        return x;
    }

    /**
     * Sets the x-field value
     * 
     * @param x value
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * Returns the y-field value
     * 
     * @return y value
     */
    public String getY() {
        return y;
    }

    /**
     * Sets the y-field value
     * 
     * @param y value
     */
    public void setY(String y) {
        this.y = y;
    }

    /**
     * Returns the id (IDENTITY FIELD)
     * 
     * @return compound key
     */
    public CompoundKey getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({id=" + id + ", x='" + x + "', y='" + y + "'})";
    }

    @Override
    public int hashCode() {
        LOGGER.fine(this.getClass().getSimpleName() + ".hashCode() called, delegating to compound key ...");
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        LOGGER.fine(this.getClass().getSimpleName() + ".equals(...) called ...");
        boolean res = false;
        if (obj instanceof Entity) {
            LOGGER.fine("Comparing compound keys");
            res = this.id.equals(((Entity) obj).id);
        }
        LOGGER.fine("Returning " + res);
        return res;
    }

}
