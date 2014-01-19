/*
 * Component - supplementary interface in the DEPENDECY INJECTION example
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
package de.calamanari.pk.dependencyinjection;

/**
 * Component - supplementary interface in the DEPENDECY INJECTION example, interface to be implemented by Components of
 * our framework.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface Component {

    /**
     * Returns the component's data
     * @return data
     */
    public String getData();

    /**
     * Sets the component's data
     * @param data component data
     */
    public void setData(String data);

    /**
     * Prints the component's data using the injected print service
     */
    public void printData();

}
