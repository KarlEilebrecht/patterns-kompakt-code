//@formatter:off
/*
 * BloomBoxOption
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

import java.util.Map;

/**
 * List of options related to bloom box queries
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum BloomBoxOption {

    /**
     * boolean option, if set to true protocol should be written
     */
    PROTOCOL("protocol"),

    /**
     * boolean option, if true and supported by the underlying data store query will be executed with multiple threads
     */
    PARALLEL_QUERY("parallel");

    /**
     * name in map for this option
     */
    public final String optionName;

    /**
     * @param name identified option in option map, case-sensitive
     */
    BloomBoxOption(String name) {
        this.optionName = name;
    }

    /**
     * @param options map with options, null will be silently ignored
     * @return true if the assigned value is true, ignoring the value's case
     */
    public boolean isEnabled(Map<String, String> options) {
        return options != null && String.valueOf(options.get(optionName)).equalsIgnoreCase("true");
    }

    /**
     * @param options map with options, null will be silently ignored
     * @return value assigned to this option in the map
     */
    public String getValue(Map<String, String> options) {
        return options.get(optionName);
    }

}
