//@formatter:off
/*
 * Counter Flyweight Carrying Item - demonstrates a FLYWEIGHT carrying item.
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
package de.calamanari.pk.flyweight;

import java.util.logging.Logger;

/**
 * Counter Flyweight Carrying Item - demonstrates a FLYWEIGHT carrying item.<br>
 * In a real-world scenario this could be a cell in a sheet carrying a cell renderer, which is the flyweight.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CounterFlyweightCarryingItem {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CounterFlyweightCarryingItem.class.getName());

    /**
     * the item's data
     */
    private final String itemData;

    /**
     * the counter flyweight this item carries to perform its operation
     */
    private final CounterFlyweight counterFlyweight;

    /**
     * Creates new item
     * 
     * @param itemData carried information
     * @param counterFlyweight carried flyweight instance
     */
    public CounterFlyweightCarryingItem(String itemData, CounterFlyweight counterFlyweight) {
        this.itemData = itemData;
        this.counterFlyweight = counterFlyweight;
    }

    /**
     * This is the item's operation it can only perform leveraging the flyweight.<br>
     * In a real-world example this could be the display operation of a cell which it performs calling its renderer.
     * 
     * @return counter info
     */
    public String getCounterInfo() {

        LOGGER.fine(this.getClass().getSimpleName() + ".getCounterInfo() called.");

        LOGGER.fine("Using flyweight to do the job.");
        // the itemdata gets passed to the flyweight, from the viewpoint of the flyweight this is extrinsic state
        return "Counter-Info: " + this.counterFlyweight.count(itemData);
    }

}
